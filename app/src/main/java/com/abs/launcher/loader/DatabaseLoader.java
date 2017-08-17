package com.abs.launcher.loader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.abs.launcher.DbManager;
import com.abs.launcher.model.AppInfo;
import com.abs.launcher.model.AppWidgetInfo;
import com.abs.launcher.model.HomeAppInfo;

import java.util.ArrayList;

/**
 * Created by zy on 17-8-18.
 */

public class DatabaseLoader {

    Context context;

    ArrayList<AppInfo> applications;
    ArrayList<HomeAppInfo> shortcuts;
    ArrayList<AppWidgetInfo> widgets;

    public DatabaseLoader(Context context) {
        this.context = context;
    }

    public void load() {

    }

    private void loadApplications() {
        Cursor cursor = context.getContentResolver().query(DbManager.Applications.getUri(), null, null, null, null);
        DbManager.ApplicationIndex index = new DbManager.ApplicationIndex(cursor);
        while (cursor.moveToNext()) {
            try {
                AppInfo info = new AppInfo();
                info.id = cursor.getLong(index.idIndex);
                info.intent = Intent.parseUri(cursor.getString(index.intentIndex), 0);
                info.isShortcut = info.intent.getComponent() == null;
                info.title = cursor.getString(index.titleIndex);
                info.system = cursor.getInt(index.systemIndex) == 1;
                info.storage = cursor.getInt(index.storageIndex);
                applications.add(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFavorites() {

    }

    private void loadDockbar() {

    }

    private void loadScreen() {

    }

    public void loadFavoriteItem() {

    }
}
