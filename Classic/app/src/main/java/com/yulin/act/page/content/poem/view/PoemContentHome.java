package com.yulin.act.page.content.poem.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.yulin.act.config.DataModule;
import com.yulin.act.model.PoemContent;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuCustomItem;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.applib.page.PageIntent;
import com.yulin.applib.widget.LoopPageSwitcherEx;
import com.yulin.classic.R;
import com.yulin.classic.databinding.LayoutTitlebarCenterPoemTitleBinding;
import com.yulin.classic.databinding.PagePoemHomeBinding;

import java.util.ArrayList;

/**
 * Created by liulei0905 on 2016/11/10.
 * 显示诗词内容
 */

public class PoemContentHome extends PageImpl {

    private static final String EXTRA_CONTENT_IDS = "extra_content_ids";
    private static final String EXTRA_CURRENT_INDEX = "extra_current_index";

    private int mCurrentIndex;

    private ArrayList<Integer> mListContentIds;
    private PoemContent mPoemContent;

    private LoopPageSwitcherEx mPageSwitcher;

    public static void startPage(Module module, ArrayList<Integer> listContentIds, int currentIndex) {
        Bundle extras = new Bundle();
        extras.putIntegerArrayList(EXTRA_CONTENT_IDS, listContentIds);
        extras.putInt(EXTRA_CURRENT_INDEX, currentIndex);

        PageIntent pageIntent = new PageIntent(null, PoemContentHome.class);
        pageIntent.setSupportAnimation(false);
        pageIntent.setArguments(extras);

        module.startPage(DataModule.G_CURRENT_FRAME, pageIntent);
    }

    @Override
    protected void receiveData(Bundle extras) {
        if (extras != null) {
            if (extras.containsKey(EXTRA_CONTENT_IDS)) {
                mListContentIds = extras.getIntegerArrayList(EXTRA_CONTENT_IDS);
            }
            if (extras.containsKey(EXTRA_CURRENT_INDEX)) {
                mCurrentIndex = extras.getInt(EXTRA_CURRENT_INDEX);
            }
        }
    }

    @Override
    protected void initPage() {
        super.initPage();

        PagePoemHomeBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_poem_home, null, false);
        setContentView(pageBinding.getRoot());
        mPageSwitcher = pageBinding.pagePoemHomePageSwitcher;

        initViews();

        bindPageTitleBar(R.id.page_poem_home_title_bar);
    }

    @Override
    protected void initData() {
        int size = mListContentIds.size();
        mPageSwitcher.setPageCount(size);

        registViewWithPage(mPageSwitcher);
        mPageSwitcher.setCurrentItem(mCurrentIndex);
    }

    @Override
    protected void onPagePause() {
        if (mPageSwitcher != null) {
            mPageSwitcher.onViewPause();
        }

        super.onPagePause();
    }

    @Override
    protected void onPageDestroy() {
        if (mPageSwitcher != null) {
            mPageSwitcher.onViewDestroy();
        }
        mPageSwitcher = null;

        super.onPageDestroy();
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        View leftView = View.inflate(Util.getContext(), R.layout.layout_titlebar_left_back, null);
        BarMenuCustomItem leftMenu = new BarMenuCustomItem(0, leftView);
        leftMenu.setTag(TitleBar.Position.LEFT);
        menu.addItem(leftMenu);

        LayoutTitlebarCenterPoemTitleBinding centerLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(Util.getContext()), R.layout.layout_titlebar_center_poem_title, null, false);
        mPoemContent = new PoemContent();
        centerLayoutBinding.setModel(mPoemContent);
        BarMenuCustomItem centerMenu = new BarMenuCustomItem(1, centerLayoutBinding.getRoot());
        centerMenu.setTag(TitleBar.Position.CENTER);
        menu.addItem(centerMenu);

        return true;
    }

    @Override
    protected void onPageTitleBarMenuItemSelected(BarMenuItem menuItem) {
        super.onPageTitleBarMenuItemSelected(menuItem);

        int menuId = menuItem.getItemId();

        if (menuId == 0 && mPageChangeFlag == 0) {
            mPageChangeFlag = -1;
            finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return true;
    }

    private void initViews() {
        mPageSwitcher.setSwitchable(true);
        mPageSwitcher.setPreload(true);
        mPageSwitcher.setPageFactory(new LoopPageSwitcherEx.IPageFactoryEx() {

            @Override
            public Page createPage(int position) {
                final PoemContentPage page;

                page = new PoemContentPage();
                page.setContentId(mListContentIds.get(position));
                page.needPringLog(false);
                page.setSupportAnimation(false);
                page.setPageIndex(position);
                page.setOnTitlebarContentChanged(new OnTitleBarContentChanged() {
                    @Override
                    public void changeTitleBarContent(String title, String author, int pageIndex) {
                        if (mCurrentIndex == pageIndex) {
                            mPoemContent.setTitle(title);
                            mPoemContent.setAuthor(author);
                        }
                    }
                });

                return page;
            }
        });
        mPageSwitcher.setOnPageSwitchListener(new LoopPageSwitcherEx.OnPageSwitchListenerEx() {

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;

                if (mListContentIds != null && mListContentIds.size() > position) {
                    PoemContentPage currentPage = (PoemContentPage) mPageSwitcher.getCurrentPage();
                    if (currentPage != null) {
                        mPoemContent.setTitle(currentPage.getPoemContent().getTitle());
                        mPoemContent.setAuthor(currentPage.getPoemContent().getAuthor());
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public interface OnTitleBarContentChanged {
        void changeTitleBarContent(String title, String author, int pageIndex);
    }

}
