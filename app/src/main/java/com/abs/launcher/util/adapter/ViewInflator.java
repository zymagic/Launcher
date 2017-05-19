package com.abs.launcher.util.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zy on 17-5-19.
 */

public class ViewInflator implements AbsCommonListAdapter.MultiTypeViewCreator {

    private SparseIntArray mResMap = new SparseIntArray();
    private LayoutInflater mInflater;

    public ViewInflator(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public ViewInflator setLayoutResId(int id) {
        mResMap.put(0, id);
        return this;
    }

    public ViewInflator setLayoutResId(int type, int id) {
        mResMap.put(type, id);
        return this;
    }

    @Override
    public View createView(int position, int type, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return mResMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mResMap.size() > 1) {
            throw new IllegalStateException("getItemViewType must be overridden if more than one item view type is defined");
        }
        return 0;
    }
}
