package mobi.glowworm.journal;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }
    }

}
