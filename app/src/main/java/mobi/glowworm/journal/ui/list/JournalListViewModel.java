package mobi.glowworm.journal.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.model.JournalEntry;

public class JournalListViewModel extends ViewModel {

    private final LiveData<List<JournalEntry>> liveJournalList;

    public JournalListViewModel(Db db, String userId) {
        super();

        liveJournalList = db.dao().loadAllJournalsForUser(userId);
    }

    public LiveData<List<JournalEntry>> getJournalList() {
        return liveJournalList;
    }
}
