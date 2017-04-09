package com.abs.launcher.util;

import java.lang.ref.WeakReference;

/**
 * Created by ZY on 2017/4/2.
 */

public class SelfReference<T> {
    private WeakReference<T> mRef;

    public synchronized T push(T obj) {
        T previous = get();
        mRef = new WeakReference<T>(obj);
        return previous;
    }

    public synchronized void pop(T obj) {
        if (obj == get()) {
            mRef = null;
        }
    }

    public synchronized T get() {
        return mRef == null ? null : mRef.get();
    }
}
