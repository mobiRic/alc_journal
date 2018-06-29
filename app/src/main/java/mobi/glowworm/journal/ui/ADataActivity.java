package mobi.glowworm.journal.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.JournalDao;

/**
 * Abstract {@link android.app.Activity} containing helper methods
 * for working with the  {@link Db} and {@link JournalDao}.
 */
public class ADataActivity extends AppCompatActivity {

    @NonNull
    protected Db getDatabase() {
        return Db.getInstance(this);
    }

    @NonNull
    protected JournalDao getDao() {
        return Db.getInstance(this).dao();
    }
}
