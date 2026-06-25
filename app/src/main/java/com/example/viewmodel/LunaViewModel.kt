package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LunaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LunaRepository

    init {
        val database = LunaDatabase.getDatabase(application)
        repository = LunaRepository(database)
    }

    // Flows from Repository
    val profile: StateFlow<Profile?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val periodLogs: StateFlow<List<PeriodLog>> = repository.periodLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moodLogs: StateFlow<List<MoodLog>> = repository.moodLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntries: StateFlow<List<JournalEntry>> = repository.journalEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<Bookmark>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicalJournalEntries: StateFlow<List<MedicalJournalEntry>> = repository.medicalJournalEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicalReminders: StateFlow<List<MedicalReminder>> = repository.medicalReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val behaviourLogs: StateFlow<List<BehaviourLog>> = repository.behaviourLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cupCareLogs: StateFlow<List<CupCareLog>> = repository.cupCareLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supportNotes: StateFlow<List<SupportNote>> = repository.supportNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val symptomLogs: StateFlow<List<SymptomLog>> = repository.symptomLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Pin Authenticated State for Secure App Lock
    private val _isPinAuthenticated = MutableStateFlow(false)
    val isPinAuthenticated: StateFlow<Boolean> = _isPinAuthenticated.asStateFlow()

    // SEO / Deep Link Routing state
    private val _deepLinkRoute = MutableStateFlow<String?>(null)
    val deepLinkRoute: StateFlow<String?> = _deepLinkRoute.asStateFlow()

    fun handleDeepLink(path: String) {
        _deepLinkRoute.value = path
    }

    fun clearDeepLinkRoute() {
        _deepLinkRoute.value = null
    }

    // Crisis screening state
    private val _showCrisisScreen = MutableStateFlow(false)
    val showCrisisScreen: StateFlow<Boolean> = _showCrisisScreen.asStateFlow()

    fun triggerCrisisDialog() {
        _showCrisisScreen.value = true
    }

    fun dismissCrisisDialog() {
        _showCrisisScreen.value = false
    }

    private fun checkAndTriggerCrisis(text: String?) {
        if (!text.isNullOrEmpty() && CycleUtils.detectCrisisText(text)) {
            _showCrisisScreen.value = true
        }
    }

    // Temporary Guest Profile / Current User Profile for guest mode simulation
    private val _isGuestUser = MutableStateFlow(false)
    val isGuestUser: StateFlow<Boolean> = _isGuestUser.asStateFlow()

    fun setGuestMode(enabled: Boolean) {
        _isGuestUser.value = enabled
        viewModelScope.launch {
            // Save a placeholder profile to bypass full login/auth, marking it guest
            val existing = repository.getProfileSync()
            if (existing == null) {
                repository.saveProfile(
                    Profile(
                        id = 1,
                        displayName = "Guest Companion",
                        acceptedDisclaimer = true,
                        goals = listOf("Track my period", "Learn about menstrual cups")
                    )
                )
            }
        }
    }

    fun authenticatePin(input: String): Boolean {
        val currentProfile = profile.value
        return if (currentProfile != null && currentProfile.securityPinEnabled) {
            val matches = currentProfile.securityPin == input
            if (matches) {
                _isPinAuthenticated.value = true
            }
            matches
        } else {
            _isPinAuthenticated.value = true
            true
        }
    }

    fun lockApp() {
        _isPinAuthenticated.value = false
    }

    fun onboardUser(
        name: String,
        birthYear: Int?,
        cycleLength: Int,
        periodLength: Int,
        goals: List<String>,
        pin: String = "",
        userMode: String = "SELF_TRACKING",
        genderMode: String = "FEMALE",
        bodyRelevantMode: String = "MENSTRUATES",
        supportRelationship: String? = null,
        consentConfirmed: Boolean = false,
        sharedTrackingConsent: Boolean = false,
        behaviourFocuses: List<String> = emptyList(),
        medicineReminders: Boolean = false,
        waterReminders: Boolean = true
    ) {
        viewModelScope.launch {
            val hasPin = pin.isNotEmpty()
            val newProfile = Profile(
                id = 1,
                displayName = name,
                birthYear = birthYear,
                averageCycleLength = cycleLength,
                averagePeriodLength = periodLength,
                goals = goals,
                acceptedDisclaimer = true,
                securityPinEnabled = hasPin,
                securityPin = pin,
                userMode = userMode,
                genderMode = genderMode,
                bodyRelevantMode = bodyRelevantMode,
                supportRelationship = supportRelationship,
                consentConfirmed = consentConfirmed,
                sharedTrackingConsent = sharedTrackingConsent,
                behaviourFocuses = behaviourFocuses,
                medicineReminders = medicineReminders,
                waterReminders = waterReminders
            )
            repository.saveProfile(newProfile)
            _isPinAuthenticated.value = !hasPin
        }
    }

    fun updateBehaviourFocuses(focuses: List<String>) {
        viewModelScope.launch {
            val current = repository.getProfileSync() ?: Profile()
            repository.saveProfile(current.copy(behaviourFocuses = focuses))
        }
    }

    fun updateSharedTrackingConsent(consent: Boolean) {
        viewModelScope.launch {
            val current = repository.getProfileSync() ?: Profile()
            repository.saveProfile(current.copy(sharedTrackingConsent = consent))
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }

    fun addBehaviourLog(
        mood: String,
        stressLevel: Int,
        anxietyLevel: Int,
        sleepHours: Double,
        sleepQuality: Int,
        painLevel: Int,
        energyLevel: Int,
        hydrationLevel: String,
        foodCraving: String,
        caffeineIntake: String,
        movement: String,
        studyWorkPressure: Int,
        relationshipStress: Int,
        socialMediaOverload: Int,
        flowLevel: String,
        symptoms: List<String>,
        notes: String?
    ) {
        val entry = BehaviourLog(
            date = java.time.LocalDate.now().toString(),
            mood = mood,
            stressLevel = stressLevel,
            anxietyLevel = anxietyLevel,
            sleepHours = sleepHours,
            sleepQuality = sleepQuality,
            painLevel = painLevel,
            energyLevel = energyLevel,
            hydrationLevel = hydrationLevel,
            foodCraving = foodCraving,
            caffeineIntake = caffeineIntake,
            movement = movement,
            studyWorkPressure = studyWorkPressure,
            relationshipStress = relationshipStress,
            socialMediaOverload = socialMediaOverload,
            flowLevel = flowLevel,
            symptoms = symptoms,
            notes = notes,
            flags = emptyList()
        )
        val computedFlags = CycleUtils.calculateBehaviourFlags(entry)
        val finalEntry = entry.copy(flags = computedFlags)

        checkAndTriggerCrisis(notes)
        if (mood.uppercase() == "VERY_LOW" || mood.lowercase() == "very low") {
            triggerCrisisDialog()
        }

        viewModelScope.launch {
            repository.insertBehaviourLog(finalEntry)
        }
    }

    fun deleteBehaviourLog(id: Int) {
        viewModelScope.launch {
            repository.deleteBehaviourLog(id)
        }
    }

    fun addCupCareLog(
        insertedAt: String,
        emptiedAt: String,
        cleanedToday: Boolean,
        discomfortLevel: Int,
        leakageIssue: Boolean,
        notes: String?
    ) {
        viewModelScope.launch {
            repository.insertCupCareLog(
                CupCareLog(
                    insertedAt = insertedAt,
                    emptiedAt = emptiedAt,
                    cleanedToday = cleanedToday,
                    discomfortLevel = discomfortLevel,
                    leakageIssue = leakageIssue,
                    notes = notes
                )
            )
        }
    }

    fun deleteCupCareLog(id: Int) {
        viewModelScope.launch {
            repository.deleteCupCareLog(id)
        }
    }

    fun addSupportNote(
        relationship: String,
        noteTitle: String,
        noteBody: String
    ) {
        viewModelScope.launch {
            repository.insertSupportNote(
                SupportNote(
                    relationship = relationship,
                    noteTitle = noteTitle,
                    noteBody = noteBody
                )
            )
        }
    }

    fun deleteSupportNote(id: Int) {
        viewModelScope.launch {
            repository.deleteSupportNote(id)
        }
    }

    fun updateCycleSettings(cycleLength: Int, periodLength: Int) {
        viewModelScope.launch {
            val current = repository.getProfileSync() ?: Profile()
            repository.saveProfile(
                current.copy(
                    averageCycleLength = cycleLength,
                    averagePeriodLength = periodLength
                )
            )
        }
    }

    fun updateNotificationPreferences(
        periodReminders: Boolean,
        moodReminders: Boolean,
        cupReminders: Boolean,
        selfCareReminders: Boolean,
        reminderTime: String
    ) {
        viewModelScope.launch {
            val current = repository.getProfileSync() ?: Profile()
            repository.saveProfile(
                current.copy(
                    periodReminders = periodReminders,
                    moodReminders = moodReminders,
                    cupReminders = cupReminders,
                    selfCareReminders = selfCareReminders,
                    reminderTime = reminderTime
                )
            )
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val current = repository.getProfileSync() ?: Profile()
            repository.saveProfile(current.copy(isDarkMode = enabled))
        }
    }

    // Period Logs
    fun addPeriodLog(
        startDate: String,
        endDate: String?,
        flowLevel: String,
        symptoms: List<String>,
        notes: String?
    ) {
        viewModelScope.launch {
            repository.insertPeriodLog(
                PeriodLog(
                    startDate = startDate,
                    endDate = endDate,
                    flowLevel = flowLevel,
                    symptoms = symptoms,
                    notes = notes
                )
            )
        }
    }

    fun deletePeriodLog(id: Int) {
        viewModelScope.launch {
            repository.deletePeriodLog(id)
        }
    }

    // Mood Logs
    fun addMoodLog(
        mood: String,
        energy: Int,
        stress: Int,
        sleepQuality: Int?,
        notes: String?
    ) {
        checkAndTriggerCrisis(notes)
        if (mood == "Very low") {
            triggerCrisisDialog()
        }
        viewModelScope.launch {
            repository.insertMoodLog(
                MoodLog(
                    date = CycleUtils.getTodayString(),
                    mood = mood,
                    energy = energy,
                    stress = stress,
                    sleepQuality = sleepQuality,
                    notes = notes
                )
            )
        }
    }

    fun deleteMoodLog(id: Int) {
        viewModelScope.launch {
            repository.deleteMoodLog(id)
        }
    }

    // Journal
    fun addJournalEntry(
        title: String,
        body: String,
        moodTag: String?,
        cyclePhase: String?
    ) {
        checkAndTriggerCrisis(title)
        checkAndTriggerCrisis(body)
        viewModelScope.launch {
            repository.insertJournalEntry(
                JournalEntry(
                    date = CycleUtils.getTodayString(),
                    title = title,
                    body = body,
                    moodTag = moodTag,
                    cyclePhase = cyclePhase
                )
            )
        }
    }

    fun deleteJournalEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
        }
    }

    // Bookmarks
    fun toggleBookmark(slug: String) {
        viewModelScope.launch {
            val list = bookmarks.value
            val exists = list.any { it.articleSlug == slug }
            if (exists) {
                repository.removeBookmark(slug)
            } else {
                repository.addBookmark(slug)
            }
        }
    }

    // Medical Journal
    fun addMedicalJournalEntry(
        entryDate: String,
        category: String,
        title: String,
        notes: String = "",
        symptoms: List<String> = emptyList(),
        painLevel: Int = 0,
        mood: String = "Okay",
        flowLevel: String = "None",
        medicinesTaken: String = "",
        doctorVisit: Boolean = false,
        appointmentDate: String? = null,
        doctorAdvice: String? = null
    ) {
        checkAndTriggerCrisis(title)
        checkAndTriggerCrisis(notes)
        if (mood == "Very low") {
            triggerCrisisDialog()
        }
        viewModelScope.launch {
            repository.insertMedicalJournalEntry(
                MedicalJournalEntry(
                    entryDate = entryDate,
                    category = category,
                    title = title,
                    notes = notes,
                    symptoms = symptoms,
                    painLevel = painLevel,
                    mood = mood,
                    flowLevel = flowLevel,
                    medicinesTaken = medicinesTaken,
                    doctorVisit = doctorVisit,
                    appointmentDate = appointmentDate,
                    doctorAdvice = doctorAdvice
                )
            )
        }
    }

    fun deleteMedicalJournalEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteMedicalJournalEntry(id)
        }
    }

    // Medical Reminders
    fun addMedicalReminder(
        title: String,
        reminderType: String,
        reminderTime: String,
        repeatRule: String = "Daily",
        enabled: Boolean = true,
        notes: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ) 
    {
        viewModelScope.launch {
            repository.insertMedicalReminder(
                MedicalReminder(
                    title = title,
                    reminderType = reminderType,
                    reminderTime = reminderTime,
                    repeatRule = repeatRule,
                    enabled = enabled,
                    notes = notes,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }
    }

    fun deleteMedicalReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteMedicalReminder(id)
        }
    }

    fun clearAllUserData() {
        viewModelScope.launch {
            repository.clearAllData()
            _isPinAuthenticated.value = false
            _isGuestUser.value = false
        }
    }

    // ==========================================
    // NAVIGATION AUTO-TRACKING
    // ==========================================
    private val _navigationHistory = MutableStateFlow<List<NavigationTrack>>(emptyList())
    val navigationHistory: StateFlow<List<NavigationTrack>> = _navigationHistory.asStateFlow()

    private val _isAutoTrackingEnabled = MutableStateFlow(true)
    val isAutoTrackingEnabled: StateFlow<Boolean> = _isAutoTrackingEnabled.asStateFlow()

    fun setAutoTrackingEnabled(enabled: Boolean) {
        _isAutoTrackingEnabled.value = enabled
    }

    fun trackNavigation(tabName: String) {
        if (!_isAutoTrackingEnabled.value) return
        val current = _navigationHistory.value
        if (current.lastOrNull()?.tabName != tabName) {
            _navigationHistory.value = current + NavigationTrack(tabName)
            android.util.Log.d("LunaCareNav", "Automatically tracked navigation event to tab: $tabName")
        }
    }

    fun clearNavigationHistory() {
        _navigationHistory.value = emptyList()
    }

    // ==========================================
    // SYMPTOM LOGGING METHODS
    // ==========================================
    fun addSymptomLog(
        date: String,
        symptomName: String,
        severity: Int,
        notes: String? = null
    ) {
        viewModelScope.launch {
            val log = SymptomLog(
                date = date,
                symptomName = symptomName,
                severity = severity,
                notes = notes
            )
            repository.insertSymptomLog(log)
        }
    }

    fun deleteSymptomLog(id: Int) {
        viewModelScope.launch {
            repository.deleteSymptomLog(id)
        }
    }
}

data class NavigationTrack(
    val tabName: String,
    val timestamp: Long = System.currentTimeMillis()
)
