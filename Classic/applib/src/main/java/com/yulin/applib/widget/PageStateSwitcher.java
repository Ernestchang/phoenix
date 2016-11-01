package com.yulin.applib.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.applib.page.PageIntent;
import com.yulin.applib.page.i.OnPageViewListener;

/**
 * 界面切换视图
 * 用于组织一组界面的显示方式，支持左右滑动切换
 * emoney
 *
 */
public class PageStateSwitcher extends ViewPager implements OnPageViewListener {

	private PageGalleryAdapter mAdapter = null;
	private int mCurrPageIndex = 0;
	private int mLastPageIndex = -1;
	private FragmentManager mFragmentManager = null;
	
	private Page mParentPage = null;
	private int mPageCount = 3;
	
	private boolean mIsSwitchable = true;
	private boolean mIsPreload = false;//是否预加载
	
	private Map<Integer, Page> mMapPages = new HashMap<Integer, Page>();
	public PageStateSwitcher(Context context) {
		super(context);
		initPageChangeListener();
	}

	public PageStateSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPageChangeListener();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(!isSwitchable())
		{
			return false;
		}
		else
		{
			return super.onTouchEvent(event);
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(!isSwitchable())
		{
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}
	
	private void initPageChangeListener()
	{
		setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageScrollStateChanged(int i) {
				
				if(mPageSwitchListener != null)
				{
					mPageSwitchListener.onPageScrollStateChanged(i);
				}
				
			}
			
			@Override
			public void onPageSelected(int index) {
				mCurrPageIndex = index;

				if(mPageSwitchListener != null)
				{
					mPageSwitchListener.onPageSelected(mCurrPageIndex);
				}
			}
			
			@Override
			public void onPageScrolled(int i, float v, int i2) {
				if(mPageSwitchListener != null)
				{
					mPageSwitchListener.onPageScrolled(i, v, i2);
				}
				
				if(mLastPageIndex != mCurrPageIndex && i2 == 0)
				{
					if(mMapPages.containsKey(mLastPageIndex))
					{
//						mMapPages.get(mLastPageIndex).setUserVisibleHint(false);
//						mMapPages.get(mLastPageIndex).dispatchPagePause(false);
					}
					if(mLastPageIndex != -1)
					{
						PageIntent intent = mParentPage.getPageIntent();
						mParentPage.getPageManager().popPageTopOf(intent);
					}
					
					
					if(mMapPages.containsKey(mCurrPageIndex) && mParentPage.getUserVisibleHint())
					{
//						mMapPages.get(mCurrPageIndex).setUserVisibleHint(true);
//						mMapPages.get(mCurrPageIndex).dispatchPageResume(false);
					}
					
					if(mIsPreload && mParentPage.getUserVisibleHint())
					{
						int nextPage = (mCurrPageIndex + 1)%mPageCount;
						int prevPage = (mCurrPageIndex - 1 + mPageCount)%mPageCount;
						if(nextPage != prevPage)
						{
							if(mLastPageIndex != prevPage && mMapPages.containsKey(prevPage))
							{
								mMapPages.get(prevPage).dispatchPageResume(false);
							}
							if(mLastPageIndex != nextPage && mMapPages.containsKey(nextPage))
							{
								mMapPages.get(nextPage).dispatchPageResume(false);
							}
						}
						else
						{
							if(mLastPageIndex != nextPage && mMapPages.containsKey(nextPage))
							{
								mMapPages.get(nextPage).dispatchPageResume(false);
							}
						}
					}
					mLastPageIndex = mCurrPageIndex;
				}
			}
		});
	}
	public int getPageCount()
	{
		return mPageCount;
	}

	public Page getPage(int index)
	{
		if(mMapPages.containsKey(index))
		{
			return mMapPages.get(index);
		}
		return null;
	}
	public Page getCurrentPage()
	{
		if(mMapPages.containsKey(mCurrPageIndex))
		{
			return mMapPages.get(mCurrPageIndex);
		}
		return null;
	}
	public int getCurrentItem()
	{
		return mCurrPageIndex;
	}
	
	/**
	 * 通知控件，page已发生变化
	 */
	public void notifyPageSetChanged()
	{
		registToPage(mParentPage);
	}
	

	public void setPageCount(int count)
	{
		mPageCount = count;
	}
	class PageGalleryAdapter extends FragmentStatePagerAdapter
	{

		public PageGalleryAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			if(mPageFactory == null)
			{
				throw new IllegalArgumentException("you should call setPageFactory first!");
			}
			Page page = mPageFactory.createPage(position);
			page.setParent(mParentPage);
			mMapPages.put(position, page);
			return page;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPageCount;
		}
		
	}
	
	private OnPageSwitchListener mPageSwitchListener = null;
	public void setOnPageSwitchListener(OnPageSwitchListener listener)
	{
		mPageSwitchListener = listener;
	}
	public static interface OnPageSwitchListener
	{
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
		if(getCurrentPage() != null 
				&& getCurrentPage().isAdded()
				&& getCurrentPage().needPauseAndResumeWhenSwitch())
		{
			getCurrentPage().dispatchPageResume(false);
		}
	}
	
	@Override
	public void onViewPause() {
		if(getCurrentPage() != null 
				&& getCurrentPage().isAdded()
				&& getCurrentPage().needPauseAndResumeWhenSwitch())
		{
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
	
	private void postCreate()
	{
		if(isSwitchable())
		{
			setSwitchable(mPageCount > 1);
		}
		if(mAdapter == null)
		{
			mAdapter = new PageGalleryAdapter(mFragmentManager);
			setAdapter(mAdapter);
		}
		else
		{
			mAdapter.notifyDataSetChanged();
		}
		if(mIsPreload)
		{
			if(mPageCount <= 3)
			{
				setOffscreenPageLimit(1);
			}
			else
			{
				setOffscreenPageLimit(2);
			}
		}
	}
	
	/**
	 * 设置是否支持左右滑动切换
	 * @param switchable
	 */
	public void setSwitchable(boolean switchable)
	{
		mIsSwitchable = switchable;
	}
	
	/**
	 * 是否支持左右滑动切换
	 * @return
	 */
	public boolean isSwitchable()
	{
		return mIsSwitchable;
	}

	/**
	 * 设置是否支持预加载
	 * @param preload
	 */
	public void setPreload(boolean preload)
	{
		mIsPreload = preload;
	}

	/**
	 * 是否支持预加载
	 * @return
	 */
	public boolean isPreload()
	{
		return mIsPreload;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(getCurrentPage() != null && getCurrentPage().onKeyDown(keyCode, event))
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(getCurrentPage() != null && getCurrentPage().onKeyUp(keyCode, event))
		{
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onViewResult(int requestCode, int resultCode, Bundle data) {
		// TODO Auto-generated method stub
		if(getCurrentPage() != null)
		{
			getCurrentPage().dispatchPageResult(requestCode, resultCode, data);
		}
	}
	private IPageFactory mPageFactory = null;
	public static interface IPageFactory
	{
		public Page createPage(int position);
	}
	public void setPageFactory(IPageFactory factory)
	{
		mPageFactory = factory;
	}
}
