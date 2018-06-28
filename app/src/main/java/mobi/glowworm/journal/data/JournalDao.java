package mobi.glowworm.journal.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mobi.glowworm.journal.data.model.JournalEntry;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal_entry WHERE user_id = :userId ORDER BY date DESC")
    List<JournalEntry> loadAllJournalsForUser(int userId);

    @Insert
    void insertJournal(JournalEntry journal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(JournalEntry journal);

    @Delete
    void deleteJournal(JournalEntry journal);

    @Query("SELECT * FROM journal_entry WHERE id = :journalId")
    JournalEntry loadJournalById(int journalId);
}
