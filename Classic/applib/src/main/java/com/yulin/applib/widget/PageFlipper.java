package com.yulin.applib.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ViewFlipper;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.applib.page.PageIntent;
import com.yulin.applib.page.i.OnPageViewListener;

/**
 * 界面切换视图
 * 管理一组Page的View类型控件,类似@PageSwitcher，
 * 但是不支持左右滑动切换，优点是可以灵活嵌入到界面中
 * Page有各自不完整的生命周期(onCreateView, onPageResume, onPagePause)
 * 由于PageFlipper中的Page不入栈，所有Page触发的其他Page或者Dialog将不能使用FragmentManager，getFragmentManager()将始终为null
 * emoney
 *
 */
public class PageFlipper extends ViewFlipper implements OnPageViewListener {
	private List<Page> mLstPages = new ArrayList<Page>();
	
	private Page mParentPage = null;
	public PageFlipper(Context context) {
		super(context);
	}

	public PageFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onViewDestroy() {
	}

	@Override
	public void onViewResume() {
		if(getCurrentPage() != null 
				&& getCurrentPage().needPauseAndResumeWhenSwitch())
		{
			getCurrentPage().dispatchPageResume(false);
		}
	}
	
	@Override
	public void onViewPause() {
		if(getCurrentPage() != null 
				&& getCurrentPage().needPauseAndResumeWhenSwitch())
		{
			getCurrentPage().dispatchPagePause(false);
		}
	}

	/**
	 * 添加page
	 * @param page
	 */
	public void addPage(Page page)
	{
		if(page == null)
		{
			return;
		}
		mLstPages.add(page);
	}
	
	public int getPageCount()
	{
		return mLstPages.size();
	}

	public Page getPage(int index)
	{
		int size = mLstPages.size();
		if(size == 0)
		{
			return null;
		}
		if(index >= 0 && index < size)
		{
			return mLstPages.get(index);
		}
		return null;
	}
	
	public Page getCurrentPage()
	{
		return getPage(getCurrentItem());
	}
	
	public int getCurrentItem()
	{
		return getDisplayedChild();
	}
	
	public void setCurrentItem(int index)
	{
		int lastIndex = getCurrentItem();
		if(lastIndex != index)
		{
			getPage(lastIndex).setUserVisibleHint(false);
			getPage(lastIndex).dispatchPagePause(false);
			if(mParentPage != null)
			{
				PageIntent intent = mParentPage.getPageIntent();
				mParentPage.getPageManager().popPageTopOf(intent);
			}
			setDisplayedChild(index);
			getPage(index).setUserVisibleHint(true);
			getPage(index).dispatchPageResume(false);
		}
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
		if(getCurrentPage() != null)
		{
			getCurrentPage().dispatchPageResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void registToPage(Page page) {
		mParentPage = page;
		postCreate();
	}
	
	@Override
	public void registToModule(Module module) {
		// TODO Auto-generated method stub
		postCreate();
	}
	
	private void postCreate()
	{
		LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
		for(int i = 0; i < mLstPages.size(); i++)
		{
			Page page = mLstPages.get(i);
			page.setParent(mParentPage);
			addView(page.convertToView(mParentPage, inflater, null, null));
		}
	}
	public void clearPages()
	{
		removeAllViews();
		removeAllViewsInLayout();
		mLstPages.clear();
	}
}
