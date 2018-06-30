package mobi.glowworm.journal.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import mobi.glowworm.journal.data.Db;

public class JournalListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Db db;
    private final String userId;

    public JournalListViewModelFactory(@NonNull Db db, String userId) {
        this.db = db;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new JournalListViewModel(db, userId);
    }
}