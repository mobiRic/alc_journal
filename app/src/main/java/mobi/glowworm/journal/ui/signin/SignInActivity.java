package mobi.glowworm.journal.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

import java.util.Collections;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.ui.ABaseActivity;
import mobi.glowworm.journal.ui.list.JournalListActivity;

/**
 * A basic welcome screen prompting the user to sign in to the app.
 * <p>
 * This screen is launched by the
 * {@link mobi.glowworm.journal.ui.list.JournalListActivity}
 * when no signed in user is found.
 */
public class SignInActivity extends ABaseActivity {

    private static final int RC_FIREBASE_AUTH = 1337;

    private View btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // redirect to journal list if we have a valid user
        if (isUserSignedIn()) {
            launchAndFinish(JournalListActivity.class);
            return;
        }

        setContentView(R.layout.activity_sign_in);
        btnSignIn = findViewById(R.id.btn_signin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RC_FIREBASE_AUTH: {
                if ((resultCode == RESULT_OK) && isUserSignedIn()) {
                    launchAndFinish(JournalListActivity.class);
                } else {
                    btnSignIn.setVisibility(View.VISIBLE);
                }
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_FIREBASE_AUTH);
    }

    public void onSignInClick(View view) {
        btnSignIn.setVisibility(View.INVISIBLE);
        signIn();
    }
}
