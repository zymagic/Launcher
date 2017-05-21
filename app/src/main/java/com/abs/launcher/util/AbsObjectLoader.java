package com.abs.launcher.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import com.abs.launcher.util.cache.AbsCache;
import com.abs.launcher.util.cache.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zy on 17-5-19.
 */

public class AbsObjectLoader<K, T, M extends AbsObjectLoader.AbsAttachment<K, T>> implements Handler.Callback {

    private SparseArray<AbsCache<K, T>> mCaches = new SparseArray<>(3);

    private HashMap<K, ArrayList<M>> mPendingTasks = new HashMap<>();

    private AbsCallback<M> mCallback;

    private Handler mHandler;

    private Executor mExecutor;

    public AbsObjectLoader(AbsCallback<M> callback) {
        mCallback = callback;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mExecutor = Executors.newFixedThreadPool(5);
    }

    /**
     * set cache with different level.
     * <p>level 0 cache will be regard as memory cache and load sync, level &gt; 0 will load async<p/>
     * max cache level is 3
     * */
    public AbsObjectLoader setCache(int level, AbsCache<K, T> cache) {
        mCaches.put(level, cache);
        return this;
    }

    public AbsObjectLoader setWorkingPoolSize(int size) {
        mExecutor = Executors.newFixedThreadPool(size);
        return this;
    }

    public <MM extends M> MM syncLoad(MM attachment) {
        if (attachment == null || attachment.token == null) {
            return null;
        }
        if (!checkPendingList(attachment)) {
            return null;
        }
        return loadFromLevel(attachment, 0);
    }

    public <MM extends M> MM loadAsync(final MM attachment) {
        if (attachment == null || attachment.token == null) {
            return null;
        }
        if (!checkPendingList(attachment)) {
            return null;
        }
        T object = loadFromCache(attachment.token, 0);
        attachment.object = object;
        if (isValidObject(object)) {
            notifyResult(attachment);
            return attachment;
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                loadFromLevel(attachment, 1);
            }
        });
        return null;
    }

    private <MM extends M> MM loadFromLevel(MM attachment, int levelStart) {
        K token = attachment.token;
        T object = null;
        int cacheLevel = levelStart;
        for (int i = levelStart; i <= 3; i++) {
            cacheLevel = i;
            object = loadFromCache(token, i);
            if (isValidObject(object)) {
                break;
            }
        }
        if (isValidObject(object)) {
            for (int i = cacheLevel - 1; i >= 0; i--) {
                putIntoCache(token, object, i);
            }
        }
        attachment.object = object;
        notifyResult(attachment);
        if (isValidObject(object)) {
            return attachment;
        }
        return null;
    }

    public T getFromCache(K key) {
        return loadFromCache(key, 0);
    }

    public void cancel() {
        mPendingTasks.clear();
        mHandler.removeMessages(0);
    }

    public void destroy() {
        mHandler.removeMessages(0);
        mPendingTasks.clear();
        int size = mCaches.size();
        for (int i = 0; i < size; i++) {
            Cache<K, T> cache = mCaches.get(mCaches.keyAt(i));
            cache.clear();
        }
        mCaches.clear();
    }

    protected boolean isValidObject(T object) {
        return object != null;
    }

    protected T loadFromCache(K token, int level) {
        AbsCache<K, T> cache = mCaches.get(level);
        if (cache != null) {
            return cache.get(token);
        }
        return null;
    }

    protected void putIntoCache(K token, T object, int level) {
        AbsCache<K, T> cache = mCaches.get(level);
        if (cache != null) {
            cache.put(token, object);
        }
    }

    protected boolean checkPendingList(M absAttachment) {
        ArrayList<M> list = mPendingTasks.get(absAttachment.token);
        if (list != null && list.size() > 0) {
            list.remove(absAttachment);
            list.add(absAttachment);
            return false;
        } else {
            if (list == null) {
                list = new ArrayList<>();
                mPendingTasks.put(absAttachment.token, list);
            }
            list.add(absAttachment);
            return true;
        }
    }

    protected void notifyResult(M attach) {
        if (Looper.myLooper() != mHandler.getLooper()) {
            mHandler.obtainMessage(0, attach).sendToTarget();
            return;
        }
        ArrayList<M> list = mPendingTasks.remove(attach.token);
        if (list != null && list.size() > 0) {
            for (M attachment : list) {
                attachment.object = attach.object;
                if (isValidObject(attach.object)) {
                    attachment.onLoaded();
                } else {
                    attachment.onFailure();
                }
            }
        }
        if (mCallback != null) {
            if (isValidObject(attach.object)) {
                mCallback.onLoaded(attach);
            } else {
                mCallback.onFailure(attach);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.obj == null) {
            return false;
        }
        notifyResult((M) msg.obj);
        return true;
    }

    public static class AbsAttachment<K, T> {
        public K token;
        public T object;
        public AbsAttachment(K token) {
            this.token = token;
        }

        public void onLoaded() {}

        public void onFailure() {}

        public boolean isValid() {
            return object != null;
        }
    }

    public interface AbsCallback<M extends AbsAttachment> {
        void onLoaded(M attachment);
        void onFailure(M attachment);
    }
}
