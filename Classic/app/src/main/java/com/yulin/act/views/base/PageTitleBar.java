package com.yulin.act.views.base;

import com.yulin.act.utils.Util;
import com.yulin.applib.bar.Bar;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.page.Page;
import com.yulin.applib.utils.Utils;
import com.yulin.classic.R;

/**
 * 定义、绑定、添加、初始化页面标题栏
 * 1. 在layout中使用TitleBar自定义标签定义标题栏，设置宽高、位置及背景色
 * 2. 在初始化页面时添加绑定页面及定义的TitleBar
 * 3. 在resume时添加并初始化标题栏菜单项
 * */
public abstract class PageTitleBar extends Page {

    private int mTitleBarId = -1;
    private boolean bNeedUpdate = false;
    private Bar mTitleBar = null;

    /**
     * 指定page使用的titleBar的资源id
     *
     * @param barId titleBar在布局文件中的资源id
     */
    public void bindPageTitleBar(int barId) {
        if (barId != mTitleBarId) {
            mTitleBarId = barId;
            mTitleBar = (Bar) findViewById(mTitleBarId);
            bNeedUpdate = true;
        }
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();

        if (getUserVisibleHint()) {
            onUpdateTitleBars();
        }
    }

    /**
     * 更新已绑定的bar
     */
    private void onUpdateTitleBars() {
        if (!bNeedUpdate) {
            return;
        }
        bNeedUpdate = false;
        if (getContentView() == null) {
            return;
        }

        if (mTitleBarId > 0) {
            if (!getUserVisibleHint()) {
                return;
            }

            if (mTitleBar == null)
                mTitleBar = (Bar) findViewById(mTitleBarId);

            // 沉浸式状态栏设置
            if (Utils.isKitkatUpVersion()) {
                final int statusBarHeight = Util.getStatusBarHeight();
                mTitleBar.getLayoutParams().height = (int) (statusBarHeight + Util.getRDimension(R.dimen.len_H_titlebar));
                mTitleBar.setItemPaddings(0, statusBarHeight, 0, 0);
            }

            if (mTitleBar != null) {
                BarMenuContainer barMenuContainer = new BarMenuContainer();
                boolean isAdded = onCreatePageTitleBarMenu(barMenuContainer);
                if (isAdded) {
                    mTitleBar.clearBarMenu();
                    mTitleBar.addMenuItems(barMenuContainer.getItems());
                    mTitleBar.setOnBarMenuSelectedListener(new Bar.OnBarMenuSelectedListener() {
                        @Override
                        public void onItemSelected(int index, BarMenuItem item) {
                            onPageTitleBarMenuItemSelected(item);
                        }
                    });
                    mTitleBar.notifyBarSetChanged();
                }
            }
        }
    }

    /**
     * 添加菜单项
     *
     * @param menu 向空BarMenu中添加BarMenuItem，添加完毕后返回true。
     * @return 如果返回false，表示不向BarMenu中添加BarMenuItem，返回true表示添加成功，并允许初始化。
     */
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        return false;
    }

    /**
     * 标题菜单选中事件回调
     *
     * @param menuItem 点击的menuItem菜单项
     */
    protected void onPageTitleBarMenuItemSelected(BarMenuItem menuItem) {
    }

    /**
     * 获取page绑定的titleBar的资源id
     */
    public int getBindBarId() {
        return mTitleBarId;
    }

    /**
     * page不指定titleBar的资源id
     */
    public void unbindPageTitleBar() {
        mTitleBarId = -1;
    }

    public Bar getTitleBar() {
        return mTitleBar;
    }

}
