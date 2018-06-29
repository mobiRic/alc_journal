package mobi.glowworm.journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.model.JournalEntry;

public class DetailViewModel extends ViewModel {

    private final LiveData<JournalEntry> liveJournal;

    public DetailViewModel(Db db, int journalId) {
        super();

        liveJournal = db.dao().loadJournalById(journalId);
    }

    public LiveData<JournalEntry> getJournal() {
        return liveJournal;
    }
}
