package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LunaRepository(private val db: LunaDatabase) {
    private val profileDao = db.profileDao()
    private val periodLogDao = db.periodLogDao()
    private val moodLogDao = db.moodLogDao()
    private val journalEntryDao = db.journalEntryDao()
    private val bookmarkDao = db.bookmarkDao()
    private val medicalJournalEntryDao = db.medicalJournalEntryDao()
    private val medicalReminderDao = db.medicalReminderDao()
    private val behaviourLogDao = db.behaviourLogDao()
    private val cupCareLogDao = db.cupCareLogDao()
    private val supportNoteDao = db.supportNoteDao()
    private val symptomLogDao = db.symptomLogDao()
    private val userCredentialsDao = db.userCredentialsDao()
    private val loginSecurityEventDao = db.loginSecurityEventDao()
    private val accountSecurityStateDao = db.accountSecurityStateDao()

    val profile: Flow<Profile?> = profileDao.getProfile()

    val periodLogs: Flow<List<PeriodLog>> = periodLogDao.getAllPeriodLogs().map { list ->
        list.map { log ->
            log.copy(notes = log.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    val moodLogs: Flow<List<MoodLog>> = moodLogDao.getAllMoodLogs().map { list ->
        list.map { log ->
            log.copy(notes = log.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    val journalEntries: Flow<List<JournalEntry>> = journalEntryDao.getAllJournalEntries().map { list ->
        list.map { entry ->
            entry.copy(body = EncryptionHelper.decryptSensitiveText(entry.body) ?: "")
        }
    }

    val bookmarks: Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()

    val medicalJournalEntries: Flow<List<MedicalJournalEntry>> = medicalJournalEntryDao.getAllEntries().map { list ->
        list.map { entry ->
            entry.copy(
                notes = EncryptionHelper.decryptSensitiveText(entry.notes) ?: "",
                doctorAdvice = entry.doctorAdvice?.let { EncryptionHelper.decryptSensitiveText(it) },
                medicinesTaken = EncryptionHelper.decryptSensitiveText(entry.medicinesTaken) ?: ""
            )
        }
    }

    val medicalReminders: Flow<List<MedicalReminder>> = medicalReminderDao.getAllReminders().map { list ->
        list.map { reminder ->
            reminder.copy(notes = reminder.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    val behaviourLogs: Flow<List<BehaviourLog>> = behaviourLogDao.getAllBehaviourLogs().map { list ->
        list.map { log ->
            log.copy(notes = log.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    val cupCareLogs: Flow<List<CupCareLog>> = cupCareLogDao.getAllCupCareLogs().map { list ->
        list.map { log ->
            log.copy(notes = log.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    val supportNotes: Flow<List<SupportNote>> = supportNoteDao.getAllSupportNotes().map { list ->
        list.map { note ->
            note.copy(noteBody = EncryptionHelper.decryptSensitiveText(note.noteBody) ?: "")
        }
    }

    val symptomLogs: Flow<List<SymptomLog>> = symptomLogDao.getAllSymptomLogs().map { list ->
        list.map { log ->
            log.copy(notes = log.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
        }
    }

    // New Session & Credentials Flows / Methods
    val loggedInCredentialsFlow: Flow<UserCredentials?> = userCredentialsDao.getLoggedInCredentialsFlow()

    suspend fun getCredentialsByHash(emailHash: String): UserCredentials? {
        return userCredentialsDao.getCredentialsByHash(emailHash)
    }

    suspend fun getLoggedInCredentialsSync(): UserCredentials? {
        return userCredentialsDao.getLoggedInCredentialsSync()
    }

    suspend fun insertCredentials(credentials: UserCredentials) {
        userCredentialsDao.insertCredentials(credentials)
    }

    suspend fun loginUser(emailHash: String) {
        userCredentialsDao.logoutAll() // enforce single logged in user session locally
        userCredentialsDao.loginUser(emailHash)
    }

    suspend fun logoutAll() {
        userCredentialsDao.logoutAll()
    }

    suspend fun deleteAllCredentials() {
        userCredentialsDao.deleteAllCredentials()
    }

    // Security events
    val securityEventsFlow: Flow<List<LoginSecurityEvent>> = loginSecurityEventDao.getAllSecurityEvents()

    suspend fun insertSecurityEvent(event: LoginSecurityEvent) {
        loginSecurityEventDao.insertSecurityEvent(event)
    }

    // Account security state
    suspend fun getSecurityState(emailHash: String): AccountSecurityState? {
        return accountSecurityStateDao.getSecurityState(emailHash)
    }

    suspend fun insertSecurityState(state: AccountSecurityState) {
        accountSecurityStateDao.insertSecurityState(state)
    }

    suspend fun deleteSecurityState(emailHash: String) {
        accountSecurityStateDao.deleteSecurityState(emailHash)
    }

    suspend fun getProfileSync(): Profile? = profileDao.getProfileSync()

    suspend fun saveProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    suspend fun insertPeriodLog(log: PeriodLog) {
        val encryptedLog = log.copy(notes = log.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        periodLogDao.insertPeriodLog(encryptedLog)
    }

    suspend fun deletePeriodLog(id: Int) {
        periodLogDao.deletePeriodLog(id)
    }

    suspend fun getPeriodLogById(id: Int): PeriodLog? {
        val raw = periodLogDao.getPeriodLogById(id) ?: return null
        return raw.copy(notes = raw.notes?.let { EncryptionHelper.decryptSensitiveText(it) })
    }

    suspend fun insertMoodLog(log: MoodLog) {
        val encryptedLog = log.copy(notes = log.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        moodLogDao.insertMoodLog(encryptedLog)
    }

    suspend fun deleteMoodLog(id: Int) {
        moodLogDao.deleteMoodLog(id)
    }

    suspend fun insertJournalEntry(entry: JournalEntry) {
        val encrypted = entry.copy(body = EncryptionHelper.encryptSensitiveText(entry.body) ?: "")
        journalEntryDao.insertJournalEntry(encrypted)
    }

    suspend fun deleteJournalEntry(id: Int) {
        journalEntryDao.deleteJournalEntry(id)
    }

    suspend fun insertMedicalJournalEntry(entry: MedicalJournalEntry) {
        val encrypted = entry.copy(
            notes = EncryptionHelper.encryptSensitiveText(entry.notes) ?: "",
            doctorAdvice = entry.doctorAdvice?.let { EncryptionHelper.encryptSensitiveText(it) },
            medicinesTaken = EncryptionHelper.encryptSensitiveText(entry.medicinesTaken) ?: ""
        )
        medicalJournalEntryDao.insertEntry(encrypted)
    }

    suspend fun deleteMedicalJournalEntry(id: Int) {
        medicalJournalEntryDao.deleteEntry(id)
    }

    suspend fun insertMedicalReminder(reminder: MedicalReminder) {
        val encrypted = reminder.copy(notes = reminder.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        medicalReminderDao.insertReminder(encrypted)
    }

    suspend fun deleteMedicalReminder(id: Int) {
        medicalReminderDao.deleteReminder(id)
    }

    suspend fun addBookmark(slug: String) {
        bookmarkDao.insertBookmark(Bookmark(articleSlug = slug))
    }

    suspend fun removeBookmark(slug: String) {
        bookmarkDao.deleteBookmarkBySlug(slug)
    }

    suspend fun insertBehaviourLog(log: BehaviourLog) {
        val encrypted = log.copy(notes = log.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        behaviourLogDao.insertBehaviourLog(encrypted)
    }

    suspend fun deleteBehaviourLog(id: Int) {
        behaviourLogDao.deleteBehaviourLog(id)
    }

    suspend fun insertCupCareLog(log: CupCareLog) {
        val encrypted = log.copy(notes = log.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        cupCareLogDao.insertCupCareLog(encrypted)
    }

    suspend fun deleteCupCareLog(id: Int) {
        cupCareLogDao.deleteCupCareLog(id)
    }

    suspend fun insertSupportNote(note: SupportNote) {
        val encrypted = note.copy(noteBody = EncryptionHelper.encryptSensitiveText(note.noteBody) ?: "")
        supportNoteDao.insertSupportNote(encrypted)
    }

    suspend fun deleteSupportNote(id: Int) {
        supportNoteDao.deleteSupportNote(id)
    }

    suspend fun insertSymptomLog(log: SymptomLog) {
        val encrypted = log.copy(notes = log.notes?.let { EncryptionHelper.encryptSensitiveText(it) })
        symptomLogDao.insertSymptomLog(encrypted)
    }

    suspend fun deleteSymptomLog(id: Int) {
        symptomLogDao.deleteSymptomLog(id)
    }

    suspend fun clearAllData() {
        db.clearAllTables()
    }
}
