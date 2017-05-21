package com.abs.launcher.util;

import android.graphics.Bitmap;

import com.abs.launcher.util.cache.CompositCache;

/**
 * Created by zy on 17-5-19.
 */

public class BitmapLoader extends AbsObjectLoader<String, Bitmap, BitmapLoader.AbsAttachment> {

    public BitmapLoader(Callback callback) {
        super(callback);
        setCache(1, new CompositCache<String, Bitmap>(new FileBitmapLoader(), new NetBitmapLoader()));
    }

    public BitmapLoader setBitmapCache(BitmapCache cache) {
        setCache(0, cache);
        return this;
    }

    public static class AbsAttachment extends AbsObjectLoader.AbsAttachment<String, Bitmap> {

        public AbsAttachment(String token) {
            super(token);
        }

        @Override
        public boolean isValid() {
            return super.isValid() && !object.isRecycled();
        }
    }

    public interface Callback<M extends AbsAttachment> extends AbsCallback<M> {
    }

}
