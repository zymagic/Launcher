package com.abs.launcher.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by ZY on 2017/4/2.
 */

public class LifecycleObserver {

    ArrayList<LifecycleSubjectRef> mObservers = new ArrayList<>();

    private ReferenceQueue<LifecycleSubject> mReferenceQueue = new ReferenceQueue<>();

    public void register(LifecycleSubject obj) {
        mObservers.add(new LifecycleSubjectRef(obj));
    }

    public void remove(LifecycleSubject obj) {
        mReferenceQueue.poll();
    }

    public void onStart() {

    }

    public void onStop() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {
        mObservers.clear();
    }

    private class LifecycleSubjectRef extends WeakReference<LifecycleSubject> {

        public LifecycleSubjectRef(LifecycleSubject referent) {
            super(referent, mReferenceQueue);
        }

    }
}
