package com.abs.launcher.util;

import android.graphics.Bitmap;

import com.abs.launcher.util.cache.AbsCache;

import java.util.Map;

/**
 * Created by zy on 17-5-19.
 */

public class NetBitmapLoader extends AbsCache<String, Bitmap> {


    @Override
    public Map<String, Bitmap> getAll() {
        return null;
    }

    @Override
    public void clear() {
        // ignore
    }

    @Override
    protected void onPut(String key, Bitmap object) {
        // ignore
    }

    @Override
    protected Bitmap onGet(String key) {
        return null;
    }

    @Override
    protected Bitmap onRemove(String key) {
        return null;
    }
}
