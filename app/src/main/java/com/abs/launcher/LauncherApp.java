package com.abs.launcher;

import android.app.Application;

/**
 * Created by zy on 17-8-16.
 */

public class LauncherApp extends Application {

    private LauncherModel mLauncherModel;

    private static LauncherApp sInstance;

    public static LauncherApp getApp() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public LauncherModel getModel() {
        return mLauncherModel;
    }
}
