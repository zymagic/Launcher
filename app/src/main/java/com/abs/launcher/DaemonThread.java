package com.abs.launcher;

import android.os.Handler;
import android.os.HandlerThread;

import java.lang.ref.WeakReference;

/**
 * Created by ZY on 2017/4/2.
 */

public class DaemonThread {
    private static final HandlerThread WORK_THREAD = new HandlerThread("Launcher-Daemon");
    private static final Handler HANDLER;
    static {
        WORK_THREAD.start();
        HANDLER = new Handler(WORK_THREAD.getLooper());
    }

    public static void execute(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void stop(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }
}
