package mobi.glowworm.journal.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import mobi.glowworm.journal.data.model.JournalEntry;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal_entry WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<JournalEntry>> loadAllJournalsForUser(int userId);

    @Insert
    long insertJournal(@NonNull JournalEntry journal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(@NonNull JournalEntry journal);

    @Delete
    void deleteJournal(@NonNull JournalEntry journal);

    @Query("SELECT * FROM journal_entry WHERE id = :journalId")
    LiveData<JournalEntry> loadJournalById(int journalId);
}
