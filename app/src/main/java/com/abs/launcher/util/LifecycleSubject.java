package com.abs.launcher.util;

/**
 * Created by ZY on 2017/4/2.
 */

public interface LifecycleSubject {
    void onStart();
    void onStop();
    void onPause();
    void onResume();

    public static class LifecycleSubjectAdapter implements LifecycleSubject {

        @Override
        public void onStart() {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onResume() {
        }
    }
}
