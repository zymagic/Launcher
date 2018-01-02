package com.abs.launcher.util.component;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zy on 17-8-16.
 */

public class LifecycleActivity extends Activity {

    private LifecycleObservable mObservable;

    public LifecycleObservable getLifecycle() {
        return mObservable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObservable = new LifecycleObservable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mObservable.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mObservable.onResume();
    }

    @Override
    protected void onPause() {
        mObservable.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mObservable.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mObservable.onDestroy();
        super.onDestroy();
    }
}
