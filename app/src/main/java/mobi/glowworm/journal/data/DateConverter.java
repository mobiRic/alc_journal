package mobi.glowworm.journal.data;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Converter class required for storing {@link Date} objects
 * as timestamps in the Room {@link Db}.
 * <p>
 * Adapted from https://developer.android.com/training/data-storage/room/referencing-data
 */
public class DateConverter {

    @Nullable
    @TypeConverter
    public static Date toDate(@Nullable Long millis) {
        if (millis == null) {
            return null;
        }

        return new Date(millis);
    }

    @Nullable
    @TypeConverter
    public static Long fromDate(@Nullable Date date) {
        if (date == null) {
            return null;
        }

        return date.getTime();
    }
}
