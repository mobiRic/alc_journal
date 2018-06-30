package mobi.glowworm.journal.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.JournalDao;
import mobi.glowworm.journal.ui.signin.SignInActivity;

/**
 * Base {@link android.app.Activity} containing helper methods
 * common to all parts of the application.
 */
public class ABaseActivity extends AppCompatActivity {

    /**
     * The user id for a local user who has not logged into the app.
     * <p>
     * Once a user signs in with Google Authentication this id will
     * be updated with the user's email address.
     */
    public static final String USER_NOT_SIGNED_IN = "";

    @Nullable
    private AlertDialog dialog;

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
        if (isSignInRequired() && !isUserSignedIn()) {
            signOut();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dialog != null) {
            dialog.dismiss();
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
     * Users are identified by their email addresses.
     * <p>
     * Due to support for Google Sign In, which requires an email address,
     * this ID will be unique per user.
     *
     * @return current user's email address if signed in
     */
    @SuppressWarnings("ConstantConditions") // Google users all have email addresses
    @NonNull
    protected String getCurrentUserId() {
        return getCurrentUser().getEmail();
    }

    /**
     * Asserts there is a currently signed in user before returning it.
     *
     * @return current {@link FirebaseUser}
     * @throws IllegalStateException if there is not user signed in
     */
    @NonNull
    protected FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User is not signed into the app.");
        }
        return currentUser;
    }

    /**
     * This method determines if a given activity requires an authenticated
     * user.
     * <p>
     * Override this method and return <code>false</code> to allow an
     * unauthenticated user to use the activity.
     *
     * @return <code>true</code> by default to enforce users to be signed in.
     */
    protected boolean isSignInRequired() {
        return true;
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

    protected void alert(@StringRes final int title, @StringRes final int message, @DrawableRes final int icon,
                         @StringRes final int positiveBtn, @Nullable final DialogInterface.OnClickListener positiveListener,
                         @StringRes final int negativeBtn, @Nullable final DialogInterface.OnClickListener negativeListener) {
        if (dialog != null) {
            dialog.dismiss();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ABaseActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setTitle(title);
                builder.setIcon(icon);
                builder.setPositiveButton(positiveBtn, positiveListener);
                builder.setNegativeButton(negativeBtn, negativeListener);
                builder.setMessage(message);
                dialog = builder.show();
            }
        });
    }
}
