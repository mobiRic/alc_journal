package mobi.glowworm.journal;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import mobi.glowworm.journal.data.Db;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        // initialise Room
        Db.init(this);
    }

}
