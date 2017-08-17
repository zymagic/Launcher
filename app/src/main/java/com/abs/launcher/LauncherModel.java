package com.abs.launcher;

import com.abs.launcher.loader.LoaderTask;

import java.lang.ref.WeakReference;

/**
 * Created by ZY on 2017/4/2.
 */

public class LauncherModel {

    private WeakReference<Callback> mCallbackRef;
    private DataModel mData;

    public void setCallback(Callback cbk) {
        mCallbackRef = new WeakReference<Callback>(cbk);
    }

    protected Callback getCallback() {
        return mCallbackRef == null ? null : mCallbackRef.get();
    }

    public LoaderTask.Builder buildLoaderTask() {
        return new LoaderTask.Builder(this);
    }

    public interface Callback {
        void startBindingInHome();
        void finishBindingInHome();
        void bindItemAdded(ItemInfo info);
        void bindItemRemoved(ItemInfo info);
        void bindItemUpdated(ItemInfo info);
    }
}
