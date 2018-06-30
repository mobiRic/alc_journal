package mobi.glowworm.journal.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;

import java.util.Collections;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.JournalDao;

/**
 * Abstract {@link android.app.Activity} containing helper methods
 * for working with the  {@link Db} and {@link JournalDao}.
 * <p>
 * Also includes common methods for user authentication.
 */
public class ADataActivity extends AppCompatActivity {

    private static final int RC_FIREBASE_AUTH = 1337;

    @NonNull
    protected Db getDatabase() {
        return Db.getInstance(this);
    }

    @NonNull
    protected JournalDao getDao() {
        return Db.getInstance(this).dao();
    }

    /**
     * Helper method to determine if a user is successfully authenticated
     * to use the app.
     *
     * @return <code>true</code> if there is a currently signed in user
     */
    protected boolean isUserSignedIn() {
        return false;
    }

    /**
     * Helper method to start the Firebase Auth UI sign in process.
     * <p>
     * Execution will continue in {@link #onActivityResult(int, int, Intent)}
     */
    protected void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_FIREBASE_AUTH);
    }

    /**
     * Helper method to sign a user out, and restart the sign in process.
     */
    protected void signOut() {

    }
}
