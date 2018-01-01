package com.abs.launcher.util.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zy on 17-5-19.
 */

public abstract class AbsCommonListAdapter<T> extends BaseAdapter implements BitmapLoader.Callback<AbsCommonListAdapter.Attachment> {

    protected ArrayList<T> mData = new ArrayList<T>();

    protected Context mContext;

    private ViewCreator mViewCreator;

    private MultiTypeViewCreator mMultiTypeViewCreator;

    private BitmapLoader mBitmapLoader;

    private ImageProfile<T> mImageProfile;

    private Map<String, Bitmap> mTempBitmapCache = new HashMap<>();

    private Collection<Attachment<T>> mRemoteKeys = new ArrayList<>();

    private int mItemCount;

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
        calculateItemCount();
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        calculateItemCount();
        notifyDataSetChanged();
    }

    private void calculateItemCount() {
        if (mImageProfile == null || mImageProfile.getImageColumnsCount() <= 1) {
            mItemCount = mData.size();
            return;
        }
        int columnSize = mImageProfile.getImageColumnsCount();
        mItemCount = mData.size() > 0 ? (int) Math.ceil(mData.size() * 1.0f / columnSize) : 0;
    }

    public AbsCommonListAdapter setImageProfile(ImageProfile<T> t) {
        this.mImageProfile = t;
        return this;
    }

    @Override
    public int getCount() {
        return mItemCount;
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
        ViewHolder<T> holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = mViewCreator.createView(position, type, parent);
            holder = createViewHolder(model, position, type, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder<T>) convertView.getTag();
        }
        if (mImageProfile != null) {
            Map<String, Bitmap> cache = mTempBitmapCache;
            Collection<Attachment<T>> remoteKeys = mRemoteKeys;
            Collection<String> bitmapKeys = mImageProfile.getBitmapKeys(model);
            for (int i = mImageProfile.getImageColumnsCount() - 1; i >= 0; i--) {
                cache.clear();
                remoteKeys.clear();
                for (String key : bitmapKeys) {
                    String token = mImageProfile.getBitmapToken(model, key);
                    if (TextUtils.isEmpty(token)) {
                        continue;
                    }
                    Bitmap bmp = mBitmapLoader.getFromCache(token);
                    if (bmp != null && !bmp.isRecycled()) {
                        cache.put(token, bmp);
                    } else {
                        remoteKeys.add(new Attachment<T>(token, key, model, holder));
                    }
                }
                holder.bindView(model, position, i, cache);
                for (Attachment<T> attach : remoteKeys) {
                    mBitmapLoader.loadAsync(attach);
                }
            }
            remoteKeys.clear();
            cache.clear();
        } else {
            holder.bindView(model, position, type, null);
        }
        return convertView;
    }

    @Override
    public void onLoaded(Attachment attachment) {
            if (attachment.posToken.equals(attachment.holder.token)) {
                attachment.holder.showBitmap((Bitmap) attachment.object, attachment.key);
            }
    }

    @Override
    public void onFailure(Attachment attachment) {

    }

    protected abstract ViewHolder<T> createViewHolder(T model, int position, int offset, View itemView);

    public static abstract class ViewHolder<T> {
        protected View itemView;
        Object token;
        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        protected abstract void bindView(T model, int position, int offset, Map<String, Bitmap> bitmaps);

        protected abstract void showBitmap(Bitmap bitmap, String key);
    }

    public interface ViewCreator {
        View createView(int position, int type, ViewGroup parent);
    }

    public interface MultiTypeViewCreator extends ViewCreator {
        int getViewTypeCount();
        int getItemViewType(int position);
    }

    public interface ImageProfile<T> {
        int getImageColumnsCount();
        Collection<String> getBitmapKeys(T model);
        String getBitmapToken(T model, String key);
    }

    public static class Attachment<T> extends BitmapLoader.AbsAttachment {

        String key;

        Object posToken;

        ViewHolder<T> holder;

        public Attachment(String token, String key, Object posToken, ViewHolder<T> holder) {
            super(token);
            this.posToken = posToken;
            this.holder = holder;
        }
    }
}
