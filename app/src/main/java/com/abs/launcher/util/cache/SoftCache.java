package com.abs.launcher.util.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zy on 17-5-19.
 * <br/>
 * Cache object by softReference with a specified key
 */

public class SoftCache<K, T> extends AbsCache<K, T> {
    private HashMap<K, MySoftRef<K, T>> mCache;
    private ReferenceQueue<T> mQueue;

    public SoftCache() {
        mCache = new HashMap<>();
        mQueue = new ReferenceQueue<>();
    }

    public SoftCache(int initialSize) {
        mCache = new HashMap<>(initialSize);
        mQueue = new ReferenceQueue<>();
    }

    protected void onPut(K key, T object) {
        shrink();
        mCache.put(key, new MySoftRef<K, T>(object, mQueue, key));
    }

    protected T onGet(K key) {
        shrink();
        MySoftRef<K, T> obj = mCache.get(key);
        return obj == null ? null : obj.get();
    }

    protected T onRemove(K key) {
        shrink();
        MySoftRef<K, T> obj = mCache.remove(key);
        return obj == null ? null : obj.get();
    }

    public Map<K, T> getAll() {
        shrink();
        Map<K, T> map = new HashMap<>(mCache.size());
        for (Map.Entry<K, MySoftRef<K, T>> entry : mCache.entrySet()) {
            map.put(entry.getKey(), entry.getValue().get());
        }
        return map;
    }

    private void shrink() {
        MySoftRef ref = null;
        while ((ref = (MySoftRef) mQueue.poll()) != null) {
            mCache.remove(ref.mKey);
        }
    }

    public void clear() {
        shrink();
        for (Map.Entry<K, MySoftRef<K, T>> entry : mCache.entrySet()) {
            entry.getValue().clear();
        }
        mCache.clear();
        System.gc();
        System.runFinalization();
    }

    protected void onClear(T object) {

    }

    private class MySoftRef<K, TT extends T> extends SoftReference<T> {

        private K mKey;

        public MySoftRef(TT referent, ReferenceQueue<? super T> q, K key) {
            super(referent, q);
            mKey = key;
        }

        @Override
        public void clear() {
            T object = get();
            if (object != null) {
                SoftCache.this.onClear(object);
            }
            super.clear();
        }
    }

}
