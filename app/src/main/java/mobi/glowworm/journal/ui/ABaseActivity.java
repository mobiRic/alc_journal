package mobi.glowworm.journal.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.JournalDao;
import mobi.glowworm.journal.ui.signin.SignInActivity;

/**
 * Base {@link android.app.Activity} containing helper methods
 * common to all parts of the application.
 */
public class ABaseActivity extends AppCompatActivity {

    @NonNull
    protected Db getDatabase() {
        return Db.getInstance(this);
    }

    @NonNull
    protected JournalDao getDao() {
        return Db.getInstance(this).dao();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // ensure only logged in users can access journals
        if (!(this instanceof SignInActivity)) {
            if (!isUserSignedIn()) {
                signOut();
            }
        }
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
     * Helper method to sign a user out, and restart the sign in process.
     */
    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        onSignedOut();
                    }
                });
    }

    protected void onSignedOut() {
        launchAndClearTask(SignInActivity.class);
    }

    protected void launchAndFinish(@NonNull Class<? extends Activity> activity) {
        launchAndFinish(new Intent(this, activity));
    }

    protected void launchAndFinish(@NonNull Intent i) {
        launch(i);
        finish();
    }

    protected void launch(@NonNull Intent i) {
        startActivity(i);
    }

    protected void launch(@NonNull Class<? extends Activity> activity) {
        launch(new Intent(this, activity));
    }

    protected void launchAndClearTask(@NonNull Class<? extends Activity> activity) {
        Intent i = new Intent(this, activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchAndFinish(i);
    }
}
