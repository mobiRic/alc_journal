package mobi.glowworm.journal.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import mobi.glowworm.journal.ui.list.JournalListActivity;
import mobi.glowworm.journal.ui.signin.SignInActivity;

/**
 * A blank loading screen to facilitate the correct flow based
 * on having a signed in user or not.
 */
public class LoadingActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (isUserSignedIn()) {
            launchUnlocked();
        } else {
            launchLocked();
        }
    }

    @Override
    protected boolean isSignInRequired() {
        return false;
    }

    private void launchLocked() {
        launchAndFinish(SignInActivity.class);
    }

    private void launchUnlocked() {
        launchAndFinish(JournalListActivity.class);
    }
}
