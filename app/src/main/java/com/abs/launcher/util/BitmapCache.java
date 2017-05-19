package com.abs.launcher.util;

import android.graphics.Bitmap;

import com.abs.launcher.util.cache.SoftCache;

public class BitmapCache extends SoftCache<String, Bitmap> {

    public BitmapCache() {
        super();
    }

    public BitmapCache(int size) {
        super(size);
    }

    @Override
    protected void onClear(Bitmap object) {
        if (object != null && !object.isRecycled()) {
            object.recycle();
        }
    }
}
