package com.yulin.applib.widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.applib.page.i.OnPageViewListener;

/**
 * 界面切换视图 用于组织一组界面的显示方式，支持左右滑动循环切换 emoney
 *
 */
public class LoopPageSwitcherEx extends ViewPager implements OnPageViewListener {

    private Map<Integer, Page> mMapPages = new HashMap<Integer, Page>();
    private PageGalleryAdapter mAdapter = null;
    private FragmentManager mFragmentManager = null;

    private Page mParentPage = null;

    private boolean mIsSwitchable = true;

    private boolean mIsPreload = false;// 是否预加载

    private Page mPreloadPage = null;
    private int mRealPageCount = 0;
    private int mPageCount = 0;
    private int mCurrPageIndex = 0;
    private int mLastPageIndex = -1;

    public LoopPageSwitcherEx(Context context) {
        super(context);
        initPageChangeListener();
    }

    public LoopPageSwitcherEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (!isSwitchable()) {
            return false;
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isSwitchable()) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

    private void initPageChangeListener() {
        addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrPageIndex = position;
                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageSelected(toRealPosition(position, mRealPageCount));

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mLastPageIndex != mCurrPageIndex && positionOffsetPixels == 0) {
                    // if (mMapPages.containsKey(mLastPageIndex)) {
                    // mMapPages.get(mLastPageIndex).setUserVisibleHint(false);
                    // }

                    // if (mLastPageIndex != -1) {
                    // PageIntent intent = mParentPage.getPageIntent();
                    // mParentPage.getPageManager().popPageTopOf(intent);
                    // }
                    //
                    //
                    // if (mMapPages.containsKey(mCurrPageIndex) && mParentPage.getUserVisibleHint()
                    // && mMapPages.get(mCurrPageIndex) != null) {
                    // mMapPages.get(mCurrPageIndex).setUserVisibleHint(true);
                    // }
                    //

                    if (mPageCount > 1 && mIsPreload && mParentPage.getUserVisibleHint()) {
                        int nextPage = (mCurrPageIndex + 1);
                        int prevPage = (mCurrPageIndex - 1);
                        mPreloadPage = null;
                        if ((mLastPageIndex == -1 || mLastPageIndex == prevPage) && mMapPages.containsKey(nextPage)) {
                            mPreloadPage = mMapPages.get(nextPage);
                        } else if (mLastPageIndex == nextPage && mMapPages.containsKey(prevPage)) {
                            mPreloadPage = mMapPages.get(prevPage);
                        } else {
                            mPreloadPage = null;
                        }


                        if (mPreloadPage != null) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (mPreloadPage != null) {
                                        mPreloadPage.dispatchPageResume(false);
                                        mPreloadPage = null;
                                    }
                                }
                            }, 100);
                        }
                    }



                    mLastPageIndex = mCurrPageIndex;
                }


                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageScrolled(toRealPosition(position, mRealPageCount), positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageScrollStateChanged(state);
                }
            }

        });

    }


    /**
     * 获取当前可见的page
     * 
     * @return
     */
    public Page getCurrentPage() {
        if (mMapPages.containsKey(mCurrPageIndex)) {
            return mMapPages.get(mCurrPageIndex);
        }
        return null;
    }

    public int getPageCount() {
        return mRealPageCount;
    }


    /**
     * 
     * @param count 要求 count == 1 或 count >= 4
     */
    public void setPageCount(int count) {
        if (count <= 0) {
            return;
        }
        mRealPageCount = count;
        if (count == 1) {
            mPageCount = 1;
            mCurrPageIndex = 0;
            mIsPreload = false;
        } else {
            mPageCount = count * 200;
            mCurrPageIndex = count * 100;
        }

    }

    @Override
    public void setCurrentItem(int item) {
        if (mPageCount == 1) {
            mCurrPageIndex = 0;
        } else {
            mCurrPageIndex = mRealPageCount * 100 + item;
        }
        super.setCurrentItem(mCurrPageIndex);
    }

    public static int toRealPosition(int position, int realPageCount) {
        if (position < 0) {
            return 0;
        }

        if (realPageCount <= 0) {
            return 0;
        }

        return position % realPageCount;
    }

    /**
     * 通知控件，page已发生变化
     */
    public void notifyPageSetChanged() {
        registToPage(mParentPage);
    }

    class PageGalleryAdapter extends FragmentStatePagerAdapter {


        public PageGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            if (mPageFactory == null) {
                throw new IllegalArgumentException("you should call setPageFactory first!");
            }
            int tRealPosition = position % getRealCount();
            Page page = mPageFactory.createPage(tRealPosition);
            page.setParent(mParentPage);
            mMapPages.put(position, page);
            return page;
        }

        @Override
        public int getCount() {
            return mPageCount;
        }


        public int getRealCount() {
            return mRealPageCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mMapPages.remove(position);
        }
    }

    private OnPageSwitchListenerEx mPageSwitchListener = null;

    public void setOnPageSwitchListener(OnPageSwitchListenerEx listener) {
        mPageSwitchListener = listener;
    }

    public static interface OnPageSwitchListenerEx {
        public void onPageScrollStateChanged(int status);

        public void onPageSelected(int index);

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    }

    @Override
    public void onViewDestroy() {}

    @Override
    public void onViewResume() {
        if (getCurrentPage() != null && getCurrentPage().isAdded() && getCurrentPage().needPauseAndResumeWhenSwitch()) {
            getCurrentPage().dispatchPageResume(false);
        }
    }

    @Override
    public void onViewPause() {
        if (getCurrentPage() != null && getCurrentPage().isAdded() && getCurrentPage().needPauseAndResumeWhenSwitch()) {
            getCurrentPage().dispatchPagePause(false);

            Iterator<Page> it = mMapPages.values().iterator();
            while (it.hasNext()) {
                Page page = (Page) it.next();
                if (page != null) {
                    page.dispatchPagePause(false);
                }
            }
        }
    }

    @Override
    public void registToPage(Page page) {
        mParentPage = page;
        mFragmentManager = page.getChildFragmentManager();
        postCreate();
    }

    @Override
    public void registToModule(Module module) {
        // TODO Auto-generated method stub
        mFragmentManager = module.getSupportFragmentManager();
        postCreate();
    }

    private void postCreate() {
        if (mIsPreload) {
            if (mPageCount == 1) {
                setOffscreenPageLimit(1);
            } else {
                setOffscreenPageLimit(2);
            }
        }
        if (isSwitchable()) {
            setSwitchable(mRealPageCount > 1);
        }
        if (mAdapter == null) {
            mAdapter = new PageGalleryAdapter(mFragmentManager);
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置是否支持左右滑动切换
     * 
     * @param switchable
     */
    public void setSwitchable(boolean switchable) {
        mIsSwitchable = switchable;
    }

    /**
     * 是否支持左右滑动切换
     * 
     * @return
     */
    public boolean isSwitchable() {
        return mIsSwitchable;
    }


    /**
     * 设置是否支持预加载
     * 
     * @param preload
     */
    public void setPreload(boolean preload) {
        mIsPreload = preload;
    }

    /**
     * 是否支持预加载
     * 
     * @return
     */
    public boolean isPreload() {
        return mIsPreload;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getCurrentPage() != null && getCurrentPage().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getCurrentPage() != null && getCurrentPage().onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onViewResult(int requestCode, int resultCode, Bundle data) {
        // TODO Auto-generated method stub
        if (getCurrentPage() != null) {
            getCurrentPage().dispatchPageResult(requestCode, resultCode, data);
        }
    }

    private IPageFactoryEx mPageFactory = null;

    public static interface IPageFactoryEx {
        public Page createPage(int position);
    }

    public void setPageFactory(IPageFactoryEx factory) {
        mPageFactory = factory;
    }
}
