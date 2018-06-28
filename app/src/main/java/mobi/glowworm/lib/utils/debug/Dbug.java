/*
 * Copyright (C) 2011 Glowworm Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package mobi.glowworm.lib.utils.debug;

import android.util.Log;

/**
 * A Basic logging class that allows ProGuard to strip out all logging artifacts,
 * including string concatenation which is left in the app when using the default logging SDK.
 * <p/>
 * Works well with the following line inserted into <code>proguard-project.pro</code> file:
 * <ul>
 * <li>-assumenosideeffects class mobi.glowworm.lib.utils.debug.Dbug { public * ; }</li>
 * </ul>
 * <p>
 * Adapted from <a href="https://gist.github.com/mobiRic/9786897">mobiRic/Dbug.java on GitHub Gist</a>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Dbug {

    /**
     * Tag for logcat output.
     */
    public static final String TAG = Dbug.class.getSimpleName();

    /**
     * Convenient debug log method that uses the application's default log tag.
     * <p>
     * Based on techniques by Binu John on his blog post "Varargs and debug print"
     * (no longer available).
     *
     * @param text Array of Objects that create the debug message. This is preferable to
     *             concatenating many strings into a single parameter as that cannot be optimised
     *             away by ProGuard.
     */
    public static void log(Object... text) {
        logWithTag(TAG, text);
    }

    /**
     * Convenient debug log method that allows for custom log tags.
     *
     * @param tag  Custom tag to appear in logcat
     * @param text Array of Objects that create the debug message. This is preferable to
     *             concatenating many strings into a single parameter as that cannot be optimised
     *             away by ProGuard.
     * @see #log(Object...)
     */
    public static void logWithTag(String tag, Object... text) {

        // manually concatenate the various arguments
        StringBuilder sb = new StringBuilder();
        if (text != null) {
            for (Object object : text) {
                if (object != null) {
                    sb.append(object.toString());
                } else {
                    sb.append("<null>");
                }
            }
        }

        Log.d(tag, sb.toString());
    }

}
