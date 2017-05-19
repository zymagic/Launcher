package com.abs.launcher.util.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.abs.launcher.util.BitmapLoader;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zy on 17-5-19.
 */

public abstract class AbsCommonListAdapter<T> extends BaseAdapter implements BitmapLoader.Callback {

    protected ArrayList<T> mData = new ArrayList<T>();

    protected Context mContext;

    private ViewCreator mViewCreator;

    private MultiTypeViewCreator mMultiTypeViewCreator;

    private BitmapLoader mBitmapLoader;

    public AbsCommonListAdapter(Context context, ViewCreator viewCreator) {
        this.mContext = context;
        this.mViewCreator = viewCreator;
        if (viewCreator instanceof MultiTypeViewCreator) {
            mMultiTypeViewCreator = (MultiTypeViewCreator) viewCreator;
        }
        mBitmapLoader = new BitmapLoader(this);
    }

    public void addData(Collection<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiTypeViewCreator != null) {
            int count = mMultiTypeViewCreator.getViewTypeCount();
            return count < 1 ? 1 : count;
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeViewCreator != null) {
            return mMultiTypeViewCreator.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T model = getItem(position);
        ViewHolder<T> holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = mViewCreator.createView(position, type, parent);
            holder = createViewHolder(model, position, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder<T>) convertView.getTag();
        }

        return convertView;
    }

    public abstract ViewHolder<T> createViewHolder(T model, int position, View itemView);

    public static class ViewHolder<T> {
        protected View itemView;
        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    public interface ViewCreator {
        View createView(int position, int type, ViewGroup parent);
    }

    public interface MultiTypeViewCreator extends ViewCreator {
        int getViewTypeCount();
        int getItemViewType(int position);
    }

    public interface ImageLoader<T> {
        int getImageCount(int position, T model, int type);
    }
}
