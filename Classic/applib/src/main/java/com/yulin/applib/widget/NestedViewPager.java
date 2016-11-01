package com.yulin.applib.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;


/**
 * 嵌套的ViewPager
 * 如果父类是ViewPager，则不拦截子ViewPager的事件
 * emoney
 *
 */
public class NestedViewPager extends ViewPager{

    private static final String LOG_TAG = "CNestedViewPager";

    private float mLastMotionX = 0f;
    private ViewParent mParent = null;
    private boolean mCanReqeustDisallowInterceptTouchEvent = true;
    private boolean mMute = false;
    private boolean mFlag = false;

    public NestedViewPager(Context context) {
        super(context);
    }

    public NestedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mParent = getParent();
        while(mParent != null && !(mParent instanceof ViewPager)) {
            mParent = mParent.getParent();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mParent = null;
    }

    public void setRequestDisallowInterceptTouchEventEnable(boolean enable) {
        mCanReqeustDisallowInterceptTouchEvent = enable;
    }

    @Override
    public void setCurrentItem(int item) {
        if (mMute) {
            super.setCurrentItem(item, false);
        } else {
            super.setCurrentItem(item);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mMute) {
            return false;
        }

        final PagerAdapter adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return super.onInterceptTouchEvent(ev);
        }

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mFlag) {
                    mLastMotionX = ev.getX();
                    if (mParent != null) {
                        mParent.requestDisallowInterceptTouchEvent(true);
                        mFlag = true;
                    }
                } else {
                    final int count = adapter.getCount();
                    final int currentItem = getCurrentItem();
                    final float delta = ev.getX() - mLastMotionX;
                    mLastMotionX = ev.getX();
                    if (delta != 0) {
                        //判断边界，如果是在第一页或者最后一页
                        //需要根据滑动方向判断是否允许父级的View拦截Touch事件
                        if ((currentItem == 0 && delta > 0)                     //在第一页继续想翻到前一页
                                || (currentItem == count-1) && (delta < 0)) {   //在最后一页继续想翻到下一页
                            if (mParent != null) {
                                //只允许上一层的ViewPager拦截Touch事件
                                //其他父级的View仍然不允许拦截Touch事件
                                //这是为了保证上一层的ViewPager能够正常处理Touch事件
                                //而不会被它的父级的ViewPager拦截掉
                                mParent.requestDisallowInterceptTouchEvent(false);
                                ViewParent grandParent = mParent.getParent();
                                if (grandParent != null) {
                                    grandParent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mFlag = false;
                if (mParent != null) {
                    mParent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mMute) {
            return false;
        }

        if (!mCanReqeustDisallowInterceptTouchEvent) {
            return super.onTouchEvent(ev);
        }

        final PagerAdapter adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return super.onTouchEvent(ev);
        }

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = ev.getX();
                if (mParent != null) {
                    mParent.requestDisallowInterceptTouchEvent(true);
                    mFlag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                final int count = adapter.getCount();
                final int currentItem = getCurrentItem();
                final float delta = ev.getX() - mLastMotionX;
                mLastMotionX = ev.getX();
                if (delta != 0) {
                    //判断边界，如果是在第一页或者最后一页
                    //需要根据滑动方向判断是否允许父级的View拦截Touch事件
                    if ((currentItem == 0 && delta > 0)                     //在第一页继续想翻到前一页
                            || (currentItem == count-1) && (delta < 0)) {   //在最后一页继续想翻到下一页
                        if (mParent != null) {
                            //只允许上一层的ViewPager拦截Touch事件
                            //其他父级的View仍然不允许拦截Touch事件
                            //这是为了保证上一层的ViewPager能够正常处理Touch事件
                            //而不会被它的父级的ViewPager拦截掉
                            mParent.requestDisallowInterceptTouchEvent(false);
                            ViewParent grandParent = mParent.getParent();
                            if (grandParent != null) {
                                grandParent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mFlag = false;
                if (mParent != null) {
                    mParent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setMute(boolean mute) {
        mMute = mute;
    }

}
