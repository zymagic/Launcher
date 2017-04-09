package com.abs.launcher;

import android.app.Application;

/**
 * Created by ZY on 2017/4/2.
 */

public class AppInterface {

    static AppInterface sInstance;
    static Application sApplication;

    LauncherModel model;

    static AppInterface getApp() {
        return sInstance;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public LauncherModel getModel() {
        return model;
    }
}
