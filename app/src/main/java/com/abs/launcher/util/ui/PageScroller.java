package com.abs.launcher.util.ui;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by zy on 17-9-26.
 */

public class PagedScroller {

    private int mOrientation;
    private boolean mIsMixedScroll;
    private int mTouchSlop;
    private int mMaxVelocity;
    private VelocityTracker mVelocityTracker;

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLL = 1;
    private static final int TOUCH_STATE_FLING = 2;
    private int mTouchState = TOUCH_STATE_REST;

    private PointF mTouchPoint = new PointF();
    private AbsScroller mScroller;
    private Pager mPager;

    public PagedScroller(Context context, Pager pager) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.mPager = pager;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public boolean handleInterceptTouchEvent(MotionEvent event) {
        if (mScroller == null) {
            return false;
        }
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchPoint.set(event.getX(), event.getY());
                if (mScroller.isFinished()) {
                    mTouchState = TOUCH_STATE_REST;
                } else {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - mTouchPoint.x);
                float dy = Math.abs(event.getY() - mTouchPoint.y);
                if (mOrientation == 0) {
                    if (Math.abs(dx) > mTouchSlop && (!mIsMixedScroll || dx > dy)) {
                        mTouchState = TOUCH_STATE_SCROLL;
                        mTouchPoint.set(dx, dy);
                    }
                } else {
                    if (Math.abs(dy) > mTouchSlop && (!mIsMixedScroll || dy > dx)) {
                        mTouchState = TOUCH_STATE_REST;
                    }
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        return false;
    }

    public boolean handleTouchEvent(MotionEvent event) {
        return false;
    }

    public void computeScroll() {

    }

    public interface Pager {
        int getPageWidth();
        void offsetScroll(int delta);
    }

    public interface OnScrollStateChangeListener {
        int STATE_IDLE = TOUCH_STATE_REST;
        int STATE_TOUCH_SCROLL = TOUCH_STATE_SCROLL;
        int STATE_FLING = TOUCH_STATE_FLING;

        void onScrollStateChanged(int state);
    }

    public interface OnScrollListener extends OnScrollStateChangeListener {
        void onScroll(int x);
    }
}
