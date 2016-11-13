package com.yulin.act.page.menu.list.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.yulin.act.config.DataModule;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.page.menu.list.vm.ListMenuViewModel;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuCustomItem;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.PageIntent;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageListMenuBinding;

import rx.Observer;

public class ListMenuPage extends PageImpl {

    private static final String EXTRA_CATEGORY_ID = "extra_category_id";
    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    private boolean mIsMenuLoadComplete;
    private int mCategoryId;
    private String mCategoryName;

    private ListMenuViewModel mListMenuViewModel;

    public static void startPage(Module module, int categoryId, String categoryName) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_CATEGORY_ID, categoryId);
        extras.putString(EXTRA_CATEGORY_NAME, categoryName);

        PageIntent pageIntent = new PageIntent(null, ListMenuPage.class);
        pageIntent.setSupportAnimation(false);
        pageIntent.setArguments(extras);
        module.startPage(DataModule.G_CURRENT_FRAME, pageIntent);
    }

    @Override
    protected void receiveData(Bundle extras) {
        if (extras != null) {
            if (extras.containsKey(EXTRA_CATEGORY_ID)) {
                mCategoryId = extras.getInt(EXTRA_CATEGORY_ID);
            }
            if (extras.containsKey(EXTRA_CATEGORY_NAME)) {
                mCategoryName = extras.getString(EXTRA_CATEGORY_NAME);
            }
        }
    }

    @Override
    protected void initPage() {
        super.initPage();

        PageListMenuBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_list_menu, null, false);
        setContentView(pageBinding.getRoot());

        mListMenuViewModel = new ListMenuViewModel(this);

        pageBinding.pageListMenuRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        pageBinding.pageListMenuRecyclerView.setLayoutManager(linearLayoutManager);

        pageBinding.setModel(mListMenuViewModel);

        bindPageTitleBar(R.id.page_list_menu_title_bar);
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();

        if (!mIsMenuLoadComplete) {
            mListMenuViewModel.queryMenu(new Observer<Result>() {
                @Override
                public void onCompleted() {
                    mIsMenuLoadComplete = true;
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Result result) {

                }
            }, mCategoryId);
        }
    }

    @Override
    protected void onPageDestroy() {
        super.onPageDestroy();

        mListMenuViewModel.clearSubscription();
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        View leftView = View.inflate(Util.getContext(), R.layout.layout_titlebar_left_back, null);
        BarMenuCustomItem leftMenu = new BarMenuCustomItem(0, leftView);
        leftMenu.setTag(TitleBar.Position.LEFT);
        menu.addItem(leftMenu);

        BarMenuTextItem centerMenu = new BarMenuTextItem(1, mCategoryName);
        centerMenu.setTag(TitleBar.Position.CENTER);
        centerMenu.setTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s5));
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

}
