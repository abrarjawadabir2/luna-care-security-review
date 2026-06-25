package com.example.data

import kotlinx.coroutines.flow.Flow

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

    val profile: Flow<Profile?> = profileDao.getProfile()
    val periodLogs: Flow<List<PeriodLog>> = periodLogDao.getAllPeriodLogs()
    val moodLogs: Flow<List<MoodLog>> = moodLogDao.getAllMoodLogs()
    val journalEntries: Flow<List<JournalEntry>> = journalEntryDao.getAllJournalEntries()
    val bookmarks: Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()
    val medicalJournalEntries: Flow<List<MedicalJournalEntry>> = medicalJournalEntryDao.getAllEntries()
    val medicalReminders: Flow<List<MedicalReminder>> = medicalReminderDao.getAllReminders()
    val behaviourLogs: Flow<List<BehaviourLog>> = behaviourLogDao.getAllBehaviourLogs()
    val cupCareLogs: Flow<List<CupCareLog>> = cupCareLogDao.getAllCupCareLogs()
    val supportNotes: Flow<List<SupportNote>> = supportNoteDao.getAllSupportNotes()
    val symptomLogs: Flow<List<SymptomLog>> = symptomLogDao.getAllSymptomLogs()

    suspend fun getProfileSync(): Profile? = profileDao.getProfileSync()

    suspend fun saveProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    suspend fun insertPeriodLog(log: PeriodLog) {
        periodLogDao.insertPeriodLog(log)
    }

    suspend fun deletePeriodLog(id: Int) {
        periodLogDao.deletePeriodLog(id)
    }

    suspend fun getPeriodLogById(id: Int): PeriodLog? = periodLogDao.getPeriodLogById(id)

    suspend fun insertMoodLog(log: MoodLog) {
        moodLogDao.insertMoodLog(log)
    }

    suspend fun deleteMoodLog(id: Int) {
        moodLogDao.deleteMoodLog(id)
    }

    suspend fun insertJournalEntry(entry: JournalEntry) {
        journalEntryDao.insertJournalEntry(entry)
    }

    suspend fun deleteJournalEntry(id: Int) {
        journalEntryDao.deleteJournalEntry(id)
    }

    suspend fun insertMedicalJournalEntry(entry: MedicalJournalEntry) {
        medicalJournalEntryDao.insertEntry(entry)
    }

    suspend fun deleteMedicalJournalEntry(id: Int) {
        medicalJournalEntryDao.deleteEntry(id)
    }

    suspend fun insertMedicalReminder(reminder: MedicalReminder) {
        medicalReminderDao.insertReminder(reminder)
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
        behaviourLogDao.insertBehaviourLog(log)
    }

    suspend fun deleteBehaviourLog(id: Int) {
        behaviourLogDao.deleteBehaviourLog(id)
    }

    suspend fun insertCupCareLog(log: CupCareLog) {
        cupCareLogDao.insertCupCareLog(log)
    }

    suspend fun deleteCupCareLog(id: Int) {
        cupCareLogDao.deleteCupCareLog(id)
    }

    suspend fun insertSupportNote(note: SupportNote) {
        supportNoteDao.insertSupportNote(note)
    }

    suspend fun deleteSupportNote(id: Int) {
        supportNoteDao.deleteSupportNote(id)
    }

    suspend fun insertSymptomLog(log: SymptomLog) {
        symptomLogDao.insertSymptomLog(log)
    }

    suspend fun deleteSymptomLog(id: Int) {
        symptomLogDao.deleteSymptomLog(id)
    }

    suspend fun clearAllData() {
        db.clearAllTables()
    }
}
