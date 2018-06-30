package mobi.glowworm.lib.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import mobi.glowworm.journal.R;

/**
 * Helper class for showing an About page from the About Libraries framework.
 */
public class AboutPage {

    /**
     * Launch a default About page for the app.
     *
     * @param context Context needed to start the activity
     */
    public static void launchAboutPage(@NonNull Context context) {
        new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withActivityTitle(context.getString(R.string.action_about))
                .withAboutAppName(context.getString(R.string.app_name))
                .withAboutDescription(context.getString(R.string.about_desc))
                // TODO fix theming
//                .withActivityTheme(R.style.AppTheme)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(context);
    }

}
