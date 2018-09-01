package com.android.gt6707a.bakingtime;

import android.app.Application;
import timber.log.Timber;

public class BakingTimeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
