package com.yulin.act.page.main.main.view;

import android.databinding.DataBindingUtil;

import com.yulin.act.page.main.activity.view.ActivityPage;
import com.yulin.act.page.main.category.view.CategoryPage;
import com.yulin.act.page.main.me.view.MePage;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.Bar;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.ToolBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.widget.PageSwitcher;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ActivityMainBinding;

public class MainActivity extends Module {

    /*private int mCurrentIndex;

    private PageSwitcher mPageSwitcher;

    private ActivityPage mActivityPage;
    private CategoryPage mCategoryPage;
    private MePage mMePage;*/

    @Override
    protected void initModule() {
        super.initModule();

        DataBindingUtil.setContentView(this, R.layout.activity_frame);

        CategoryPage.startPage(this);

        /*ActivityMainBinding activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ToolBar toolBar = activityBinding.activityMainLayoutBottomBar;
        mPageSwitcher = activityBinding.activityMainPagerSwitcher;

        if (toolBar != null) {
            toolBar.setItemTextColor(Util.getRColor(R.color.t4));
            toolBar.setItemSelectedTextColor(Util.getRColor(R.color.c2));
            toolBar.setItemTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s4));
            toolBar.setOnBarMenuSelectedListener(new Bar.OnBarMenuSelectedListener() {
                @Override
                public void onItemSelected(int index, BarMenuItem arg1) {
                    mPageSwitcher.setCurrentItem(index, false);

                    mCurrentIndex = index;
                }
            });
            toolBar.notifyBarSetChanged();
        }

        if (mPageSwitcher != null) {
            mPageSwitcher.setSwitchable(false);

            BarMenuTextItem mItemActivity = new BarMenuTextItem(0, "阅读");
            mActivityPage = new ActivityPage();
            mActivityPage.registBar(toolBar, mItemActivity);
            mPageSwitcher.addPage(mActivityPage);

            BarMenuTextItem mItemCategory = new BarMenuTextItem(0, "分类");
            mCategoryPage = new CategoryPage();
            mCategoryPage.registBar(toolBar, mItemCategory);
            mPageSwitcher.addPage(mCategoryPage);

            BarMenuTextItem mItemMe = new BarMenuTextItem(0, "我");
            mMePage = new MePage();
            mMePage.registBar(toolBar, mItemMe);
            mPageSwitcher.addPage(mMePage);

            registViewWithPage(mPageSwitcher);
        }*/
    }

}
