package mobi.glowworm.journal.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.JournalDao;
import mobi.glowworm.lib.utils.debug.Dbug;

/**
 * Base {@link android.app.Activity} containing helper methods
 * common to all parts of the application.
 */
public class ABaseActivity extends AppCompatActivity {

    private static final int RC_FIREBASE_AUTH = 1337;

    @NonNull
    protected Db getDatabase() {
        return Db.getInstance(this);
    }

    @NonNull
    protected JournalDao getDao() {
        return Db.getInstance(this).dao();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_FIREBASE_AUTH) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Dbug.log(user.getDisplayName());
                Dbug.log(user.getEmail());
                Dbug.log(user.getPhotoUrl());
                Dbug.log(user.getUid());
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        signOut(false);
    }

    /**
     * Helper method to determine if a user is successfully authenticated
     * to use the app.
     *
     * @return <code>true</code> if there is a currently signed in user
     */
    protected boolean isUserSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setLogo(R.mipmap.ic_launcher)
                        .setTheme(R.style.AppTheme)
                        .setTosUrl("https://superapp.example.com/terms-of-service.html")
                        .setPrivacyPolicyUrl("https://superapp.example.com/privacy-policy.html")
                        .build(),
                RC_FIREBASE_AUTH);

    }

    /**
     * Helper method to sign a user out, and restart the sign in process.
     */
    protected void signOut(final boolean autoSignIn) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (autoSignIn) {
                            signIn();
                        }
                    }
                });
    }
}
