package com.yulin.applib.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.applib.page.PageIntent;
import com.yulin.applib.page.i.OnPageViewListener;

/**
 * 界面切换视图 用于组织一组界面的显示方式，支持左右滑动切换 缺点是不能灵活的嵌入到View视图中，见@PageFlipper emoney
 *
 */
public class PageSwitcher extends ViewPager implements OnPageViewListener {

    private List<Page> mLstPages = new ArrayList<Page>();
    private PageGalleryAdapter mAdapter = null;
    private int mCurrPageIndex = 0;
    private FragmentManager mFragmentManager = null;


    private Page mParentPage = null;

    private boolean mIsSwitchable = true;

    private int updateIndex = -1;

    public PageSwitcher(Context context) {
        super(context);
        initPageChangeListener();
    }

    public PageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (!isSwitchable()) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isSwitchable()) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }

    private void initPageChangeListener() {
        setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int i) {

                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageScrollStateChanged(i);
                }

            }

            @Override
            public void onPageSelected(int index) {
                // mLstPages.get(mCurrPageIndex).setUserVisibleHint(false);
                // mLstPages.get(mCurrPageIndex).dispatchPagePause();
                PageIntent intent = mParentPage.getPageIntent();
                mParentPage.getPageManager().popPageTopOf(intent);
                mCurrPageIndex = index;
                // mLstPages.get(mCurrPageIndex).setUserVisibleHint(true);
                // mLstPages.get(mCurrPageIndex).dispatchPageResume();
                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageSelected(mCurrPageIndex);
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (mPageSwitchListener != null) {
                    mPageSwitchListener.onPageScrolled(i, v, i2);
                }
            }
        });
    }

    /**
     * 添加page
     * 
     * @param page
     */
    public void addPage(Page page) {
        if (page == null) {
            return;
        }
        if (mParentPage != null) {
            page.setParent(mParentPage);
        }
        mLstPages.add(page);
    }


    public void replacePage(int index, Page page) {
        if (page == null) {
            return;
        }

        if (index >= getPageCount()) {
            return;
        }

        mLstPages.set(index, page);
        updateIndex = index;

        notifyPageSetChanged();
    }



    public int getPageCount() {
        return mLstPages.size();
    }

    public Page getPage(int index) {
        int size = mLstPages.size();
        if (size == 0) {
            return null;
        }
        if (index >= 0 && index < size) {
            return mLstPages.get(index);
        }
        return null;
    }

    /**
     * 获取当前可见的page
     * 
     * @return
     */
    public Page getCurrentPage() {
        return getPage(mCurrPageIndex);
    }

    public int getCurrentItem() {
        return mCurrPageIndex;
    }

    /**
     * 通知控件，page已发生变化
     */
    public void notifyPageSetChanged() {
        registToPage(mParentPage);
    }

    class PageGalleryAdapter extends FragmentPagerAdapter {

        public PageGalleryAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return mLstPages.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mLstPages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // 得到缓存的fragment
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            // 得到tag，这点很重要
            String fragmentTag = fragment.getTag();

            if (position == updateIndex) {
                // 如果这个fragment需要更新

                FragmentTransaction ft = mFragmentManager.beginTransaction();
                // 移除旧的fragment
                ft.remove(fragment);
                // 换成新的fragment
                fragment = mLstPages.get(position);
                // 添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();

                // 复位更新标志
                updateIndex = -1;
            }

            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            super.destroyItem(container, position, object);
        }

    }

    private OnPageSwitchListener mPageSwitchListener = null;

    public void setOnPageSwitchListener(OnPageSwitchListener listener) {
        mPageSwitchListener = listener;
    }

    public static interface OnPageSwitchListener {
        public void onPageScrollStateChanged(int i);

        public void onPageSelected(int index);

        public void onPageScrolled(int i, float v, int i2);
    }

    @Override
    public void onViewDestroy() {
        // TODO Auto-generated method stub

    }

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
        if (mParentPage != null) {
            for (int i = 0; i < mLstPages.size(); i++) {
                mLstPages.get(i).setParent(mParentPage);
            }
        }
        if (mAdapter == null) {
            mAdapter = new PageGalleryAdapter(mFragmentManager);
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        setOffscreenPageLimit(mLstPages.size());
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

    public void clearPages() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < mLstPages.size(); i++) {
            Page page = mLstPages.get(i);
            ft.remove(page);
        }
        ft.commit();
        mFragmentManager.executePendingTransactions();

        mLstPages.clear();
        mCurrPageIndex = 0;
    }
    
    public void removePage(int index) {
        if (index < mLstPages.size()) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Page page = mLstPages.get(index);
            ft.remove(page);
            
            ft.commit();
            mFragmentManager.executePendingTransactions();
            
            mLstPages.remove(index);
            
            if (mCurrPageIndex == index) {
                mCurrPageIndex = 0;
            }
        }
    }
    
    // @Override
    // public void dispatchNewIntent(PageIntent intent) {
    // // TODO Auto-generated method stub
    // for(int i = 0; i < mLstPages.size(); i++)
    // {
    // Page uibase = mLstPages.get(i);
    // uibase.dispatchNewIntent(intent);
    // }
    // }
    //
    // @Override
    // public void dispatchData(Bundle bundle) {
    // // TODO Auto-generated method stub
    // for(int i = 0; i < mLstPages.size(); i++)
    // {
    // Page uibase = mLstPages.get(i);
    // uibase.dispatchData(bundle);
    // }
    // }
}
