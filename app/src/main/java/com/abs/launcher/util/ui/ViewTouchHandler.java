package com.abs.launcher.util.ui;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by zy on 17-9-26.
 */

public class ViewTouchHandler {

    private ViewTouchConfig mConfig;
    private OnTouchStateChangedListener mTouchStateListener;
    private OnInterceptTouchEventListener mInterceptListener;
    private OnTouchEventListener mTouchEventListener;
    private int mTouchSlop;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private VelocityTracker mVelocityTracker;

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLL = 1;
    private static final int TOUCH_STATE_FLING = 2;
    private int mTouchState = TOUCH_STATE_REST;

    private PointF mTouchPoint = new PointF();

    public ViewTouchHandler(Context context) {
        this(context, null);
    }

    public ViewTouchHandler(Context context, ViewTouchConfig config) {
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        mTouchSlop = viewConfig.getScaledTouchSlop();
        mMaxFlingVelocity = viewConfig.getScaledMaximumFlingVelocity();
        mMinFlingVelocity = viewConfig.getScaledMinimumFlingVelocity();
        mConfig = config;
    }

    /**
     * directly change the touch state
     * */
    public void setTouchState(int state) {
        mTouchState = state;
    }

    public boolean handleInterceptTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchPoint.set(event.getX(), event.getY());
                if (mInterceptListener != null) {
                    mInterceptListener.onInterceptDown();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - mTouchPoint.x);
                float dy = Math.abs(event.getY() - mTouchPoint.y);
                int direction = mConfig == null ? 0 : (mConfig.getScrollDirection() & ViewTouchConfig.SCROLL_DIRECTION_MASK);
                if (direction == 0) {
                    break;
                }
                boolean isMixedScroll = (mConfig.getScrollDirection() & ViewTouchConfig.MIXED_SCROLL_FLAG) != 0;
                if (direction == ViewTouchConfig.SCROLL_DIRECTION_HORIZONTAL) {
                    if (Math.abs(dx) > mTouchSlop && (!isMixedScroll || dx > dy)) {
                        mTouchState = TOUCH_STATE_SCROLL;
                        mTouchPoint.set(dx, dy);
                    }
                } else {
                    if (Math.abs(dy) > mTouchSlop && (!isMixedScroll || dy > dx)) {
                        mTouchState = TOUCH_STATE_SCROLL;
                        mTouchPoint.set(event.getX(), event.getY());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    public boolean handleTouchEvent(MotionEvent event) {

        return false;
    }

    private void trackMovement(MotionEvent event) {
        if (mConfig == null || !mConfig.isVelocityConcerned()) {
            return;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker(MotionEvent event) {
        if (mConfig == null || !mConfig.isVelocityConcerned()) {
            return;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public interface OnTouchStateChangedListener {
        int STATE_IDLE = TOUCH_STATE_REST;
        int STATE_SCROLL = TOUCH_STATE_SCROLL;
        int STATE_FLING = TOUCH_STATE_FLING;
        void onTouchStateChanged(int state);
    }

    public interface OnInterceptTouchEventListener {
        void onInterceptDown();
        void onInterceptMove();
        void onInterceptFling();
        void onInterceptUpOrCancel();
    }

    public interface OnTouchEventListener {
        void onDown();
        void onMove();
        void onFling();
        void onUpOrCancel();
    }

    public interface ViewTouchConfig {
        int SCROLL_DIRECTION_NONE = 0;
        int SCROLL_DIRECTION_HORIZONTAL = 1;
        int SCROLL_DIRECTION_VERTICAL = 2;
        int SCROLL_DIRECTION_HORIZONTAL_MIXED = 5;
        int SCROLL_DIRECTION_VERTICAL_MIXED = 6;
        int MIXED_SCROLL_FLAG = 4;
        int SCROLL_DIRECTION_MASK = 3;
        int getScrollDirection();
        boolean isVelocityConcerned();
        boolean hasClickEvent();
    }
}
