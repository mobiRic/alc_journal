package mobi.glowworm.journal.ui.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import mobi.glowworm.journal.data.Db;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Db db;
    private final int journalId;

    public DetailViewModelFactory(@NonNull Db db, int journalId) {
        this.db = db;
        this.journalId = journalId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(db, journalId);
    }
}