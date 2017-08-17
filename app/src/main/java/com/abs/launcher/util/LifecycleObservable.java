package com.abs.launcher.util;

import java.util.WeakHashMap;

/**
 * Created by ZY on 2017/4/2.
 */

public class LifecycleObservable {

    WeakHashMap<LifecycleSubject, Object> mObservers = new WeakHashMap<>();

    public void register(LifecycleSubject obj) {
        mObservers.put(obj, this);
    }

    public void remove(LifecycleSubject obj) {
        mObservers.remove(obj);
    }

    public void onStart() {
        for (LifecycleSubject subject : mObservers.keySet()) {
            subject.onStart();
        }
    }

    public void onStop() {
        for (LifecycleSubject subject : mObservers.keySet()) {
            subject.onStop();
        }
    }

    public void onResume() {
        for (LifecycleSubject subject : mObservers.keySet()) {
            subject.onResume();
        }
    }

    public void onPause() {
        for (LifecycleSubject subject : mObservers.keySet()) {
            subject.onPause();
        }
    }

    public void onDestroy() {
        for (LifecycleSubject subject : mObservers.keySet()) {
            subject.onDestroy();
        }
        mObservers.clear();
    }

}
