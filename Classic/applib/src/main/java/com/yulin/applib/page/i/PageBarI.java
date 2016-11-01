package com.yulin.applib.page.i;

import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuItem;

/**
 * Bar控件的创建与事件触发接口
 * 通过bindBar绑定到Page中
 * 一般多用于@TitleBar
 *
 * @version 1.0
 */
public interface PageBarI {

	/**
	 * 创建bar菜单
	 * @param barId
	 * @param barMenu 菜单
	 */
	boolean onCreatePageBarMenu(int barId, BarMenuContainer barMenu);
	
	/**
	 * bar菜单项点击事件
	 * @param barId
	 * @param menuItem 菜单项
	 */
	void onPageBarMenuItemSelected(int barId, BarMenuItem menuItem);

}
