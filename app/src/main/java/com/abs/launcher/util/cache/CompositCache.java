package com.abs.launcher.util.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by zy on 17-5-19.
 */

public class CompositCache<K, T> extends AbsCache<K, T> {

    ArrayList<AbsCache<K, T>> mInnerCaches;

    public CompositCache(AbsCache<K, T> ... caches) {
        if (caches == null || caches.length == 0) {
            mInnerCaches = new ArrayList<>();
        } else {
            mInnerCaches = new ArrayList<>(caches.length);
            mInnerCaches.addAll(Arrays.asList(caches));
        }
    }

    @Override
    protected void onPut(K key, T object) {
        for (int i = mInnerCaches.size() - 1; i >= 0; i-- ) {
            mInnerCaches.get(i).put(key, object);
        }
    }

    @Override
    protected T onGet(K key) {
        int size = mInnerCaches.size();
        int level = 0;
        T object = null;
        for (int i = 0; i < size; i++) {
            level = i;
            object = mInnerCaches.get(i).get(key);
            if (object != null) {
                break;
            }
        }
        if (object != null) {
            for (int i = level - 1; i >= 0; i--) {
                mInnerCaches.get(i).put(key, object);
            }
        }
        return object;
    }

    @Override
    protected T onRemove(K key) {
        T ret = null;
        for (int i = mInnerCaches.size() - 1; i >= 0; i--) {
            T object = mInnerCaches.get(i).remove(key);
            if (object != null) {
                ret = object;
            }
        }
        return ret;
    }

    @Override
    public Map<K, T> getAll() {
        return null;
    }

    @Override
    public void clear() {
        int size = mInnerCaches.size();
        for (int i = 0; i < size; i++) {
            mInnerCaches.get(i).clear();
        }
    }

}
