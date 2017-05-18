package com.abs.launcher.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by ZY on 2017/4/2.
 */

public class AppInfo {
    Intent intent;
    boolean system;
    boolean isShortcut;
    int category;
    int storage;

    Drawable icon;
    String title;

    int lastUpdateTime;
    int lastCalledTime;
    int calledNums;
}
