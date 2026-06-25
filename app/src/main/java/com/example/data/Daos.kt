package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<Profile?>

    @Query("SELECT * FROM profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileSync(): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)
}

@Dao
interface PeriodLogDao {
    @Query("SELECT * FROM period_logs ORDER BY startDate DESC")
    fun getAllPeriodLogs(): Flow<List<PeriodLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriodLog(log: PeriodLog)

    @Query("DELETE FROM period_logs WHERE id = :id")
    suspend fun deletePeriodLog(id: Int)

    @Query("SELECT * FROM period_logs WHERE id = :id LIMIT 1")
    suspend fun getPeriodLogById(id: Int): PeriodLog?
}

@Dao
interface MoodLogDao {
    @Query("SELECT * FROM mood_logs ORDER BY date DESC")
    fun getAllMoodLogs(): Flow<List<MoodLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodLog(log: MoodLog)

    @Query("DELETE FROM mood_logs WHERE id = :id")
    suspend fun deleteMoodLog(id: Int)
}

@Dao
interface JournalEntryDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournalEntry(id: Int)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE articleSlug = :slug")
    suspend fun deleteBookmarkBySlug(slug: String)
}

@Dao
interface MedicalJournalEntryDao {
    @Query("SELECT * FROM medical_journal_entries ORDER BY entryDate DESC")
    fun getAllEntries(): Flow<List<MedicalJournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MedicalJournalEntry)

    @Query("DELETE FROM medical_journal_entries WHERE id = :id")
    suspend fun deleteEntry(id: Int)
}

@Dao
interface MedicalReminderDao {
    @Query("SELECT * FROM medical_reminders ORDER BY reminderTime ASC")
    fun getAllReminders(): Flow<List<MedicalReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: MedicalReminder)

    @Query("DELETE FROM medical_reminders WHERE id = :id")
    suspend fun deleteReminder(id: Int)
}

@Dao
interface BehaviourLogDao {
    @Query("SELECT * FROM behaviour_logs ORDER BY date DESC")
    fun getAllBehaviourLogs(): Flow<List<BehaviourLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviourLog(log: BehaviourLog)

    @Query("DELETE FROM behaviour_logs WHERE id = :id")
    suspend fun deleteBehaviourLog(id: Int)
}

@Dao
interface CupCareLogDao {
    @Query("SELECT * FROM cup_care_logs ORDER BY createdAt DESC")
    fun getAllCupCareLogs(): Flow<List<CupCareLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCupCareLog(log: CupCareLog)

    @Query("DELETE FROM cup_care_logs WHERE id = :id")
    suspend fun deleteCupCareLog(id: Int)
}

@Dao
interface SupportNoteDao {
    @Query("SELECT * FROM support_notes ORDER BY createdAt DESC")
    fun getAllSupportNotes(): Flow<List<SupportNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportNote(note: SupportNote)

    @Query("DELETE FROM support_notes WHERE id = :id")
    suspend fun deleteSupportNote(id: Int)
}
