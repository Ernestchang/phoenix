package com.yulin.act.page.menu.grid.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.yulin.act.config.DataModule;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.model.BaseItem;
import com.yulin.act.page.menu.grid.vm.ShortMenuViewModel;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuCustomItem;
import com.yulin.applib.bar.BarMenuItem;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.PageIntent;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageShortMenuBinding;

import rx.Observer;

public class GridMenuPage extends PageImpl {

    private ShortMenuViewModel mShortMenuViewModel;
    private boolean mIsMenuLoadComplete;

    @Override
    protected void initPage() {
        super.initPage();

        PageShortMenuBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_short_menu, null, false);
        setContentView(pageBinding.getRoot());

        mShortMenuViewModel = new ShortMenuViewModel(this);

        pageBinding.pageShortMenuRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = mShortMenuViewModel.getItem(position).getItemType();
                if (itemType == BaseItem.ITEM_TYPE_SECTION || itemType == BaseItem.ITEM_TYPE_BOTTOM) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        pageBinding.pageShortMenuRecyclerView.setLayoutManager(gridLayoutManager);

        pageBinding.setModel(mShortMenuViewModel);

        bindPageTitleBar(R.id.page_short_menu_title_bar);
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();

        if (!mIsMenuLoadComplete) {
            mShortMenuViewModel.queryMenu(new Observer<Result>() {
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
            });
        }
    }

    @Override
    protected void onPageDestroy() {
        super.onPageDestroy();

        mShortMenuViewModel.clearSubscription();
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        View leftView = View.inflate(Util.getContext(), R.layout.layout_titlebar_left_back, null);
        BarMenuCustomItem leftMenu = new BarMenuCustomItem(0, leftView);
        leftMenu.setTag(TitleBar.Position.LEFT);
        menu.addItem(leftMenu);

        BarMenuTextItem centerMenu = new BarMenuTextItem(1, "诗经");
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

    public static void startPage(Module module) {
        PageIntent pageIntent = new PageIntent(null, GridMenuPage.class);
        pageIntent.setSupportAnimation(false);
        module.startPage(DataModule.G_CURRENT_FRAME, pageIntent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return true;
    }

}
