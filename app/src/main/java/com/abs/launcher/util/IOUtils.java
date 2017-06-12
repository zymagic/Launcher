package com.abs.launcher.util;

import android.database.Cursor;

import java.io.Closeable;

/**
 * Created by zy on 17-6-12.
 */

public class IOUtils {
    public static final void close(Cursor c) {
        try {
            c.close();
        } catch (Exception e) {
            // ignore;
        }
    }
    public static final void close(Closeable c) {
        try {
            c.close();
        } catch (Exception e) {
            // ignore
        }
    }
}
