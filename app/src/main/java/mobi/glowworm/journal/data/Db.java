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

    private static Db INSTANCE;

    synchronized public static void init(@NonNull Context context) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                Db.class,
                DATABASE_NAME)
                .build();
    }

    @NonNull
    public static Db getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Room must be initialized before use.");
        }

        return INSTANCE;
    }

    public abstract JournalDao dao();

}