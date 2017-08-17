package com.abs.launcher.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.abs.launcher.ItemInfo;

/**
 * Created by ZY on 2017/4/2.
 */

public class AppInfo {
    public long id = ItemInfo.NO_ID;
    public Intent intent;
    public boolean system;
    public boolean isShortcut;
    public int category;
    public int storage;

    public Drawable icon;
    public String title;

    public long lastUpdateTime;
    public long lastCalledTime;
    public int calledNums;


}
