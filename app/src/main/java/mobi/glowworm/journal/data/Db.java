package mobi.glowworm.journal.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import mobi.glowworm.journal.data.model.JournalEntry;

@Database(entities = {JournalEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class Db extends RoomDatabase {

    private static final String DATABASE_NAME = "ijenali";

    volatile private static Db INSTANCE;

    @NonNull
    public static Db getInstance(Context context) {

        // lazy loading
        if (INSTANCE == null) {

            // synchronized double locking
            synchronized (Db.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Db.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract JournalDao dao();

}