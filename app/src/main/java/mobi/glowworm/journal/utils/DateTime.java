package mobi.glowworm.journal.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;

import mobi.glowworm.journal.R;

/**
 * Utility class for converting between various date formats.
 */
@SuppressLint("SimpleDateFormat") // this app is opinionated on the date format used
public class DateTime {

    /**
     * Helper method to return a day formatter.
     *
     * @param context Context required to get the format string
     * @return {@link SimpleDateFormat} representing a day
     */
    public static SimpleDateFormat getFormatDay(Context context) {
        return new SimpleDateFormat(context.getString(R.string.format_day_date));
    }

    /**
     * Helper method to return a time formatter.
     *
     * @param context Context required to get the format string
     * @return {@link SimpleDateFormat} representing a time
     */
    public static SimpleDateFormat getFormatTime(Context context) {
        return new SimpleDateFormat(context.getString(R.string.format_time));
    }
}
