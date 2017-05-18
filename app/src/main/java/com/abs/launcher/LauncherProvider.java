package com.abs.launcher;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by zy on 17-5-18.
 */

public class LauncherProvider extends ContentProvider {

    private static final String DATABASE_NAME = "launcher.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteOpenHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DataBaseHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_FAVORITE + "(" +
                    DbManager.generateSqlCreation(DbManager.Favorites.class) +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_ALL_APPS + "(" +
                    DbManager.generateSqlCreation(DbManager.Applications.class) +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_HIDDEN_APPS + "(" +
                    DbManager.generateSqlCreation(DbManager.HiddenApplications.class) +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
