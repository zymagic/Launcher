package com.abs.launcher.loader;

import android.os.Process;

import com.abs.launcher.DaemonThread;
import com.abs.launcher.LauncherModel;
import com.abs.launcher.util.SelfReference;

/**
 * Created by ZY on 2017/4/2.
 */

public class LoaderTask implements Runnable {

    private boolean mStop = false;

    private LoaderTaskParams mParams;
    private LauncherModel model;

    private static SelfReference<LoaderTask> REF = new SelfReference<>();

    private LoaderTask(LauncherModel model, LoaderTaskParams params) {
        this.mParams = params;
    }

    void start() {
        LoaderTask previous = REF.get();
        if (previous != null) {
            previous.stop();
        }
        DaemonThread.execute(this);
    }

    void stop() {
        mStop = true;
        DaemonThread.stop(this);
    }

    @Override
    public final void run() {
        REF.push(this);
        try {
            load();
        } finally {
            REF.pop(this);
        }
    }

    protected void load() {
        android.os.Process.setThreadPriority(mParams.isLauching ? Process.THREAD_PRIORITY_DEFAULT : Process.THREAD_PRIORITY_BACKGROUND);
        keep_running: {

        }
    }

    public static class Builder {
        private LoaderTaskParams mParams = new LoaderTaskParams();

        private LauncherModel mModel;

        public Builder(LauncherModel model) {
            this.mModel = model;
        }

        void start() {
            new LoaderTask(mModel, mParams).start();
        }
    }

    private static class LoaderTaskParams {
        boolean reloadWorkspace;
        boolean isFirstLoad;
        boolean isLauching;
    }
}
