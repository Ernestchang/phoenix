package com.yulin.act.page.main.category.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import com.yulin.act.config.DataModule;
import com.yulin.act.model.Result;
import com.yulin.act.page.base.PageImpl;
import com.yulin.act.model.BaseItem;
import com.yulin.act.page.main.category.viewmodel.CategoryViewModel;
import com.yulin.act.page.menu.grid.view.GridMenuPage;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.applib.module.Module;
import com.yulin.applib.page.PageIntent;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageCategoryBinding;

import rx.Observer;

public class CategoryPage extends PageImpl {

    private CategoryViewModel mCategoryViewModel;
    private boolean mIsMenuLoadComplete;

    public static void startPage(Module module) {
        PageIntent pageIntent = new PageIntent(null, CategoryPage.class);
        pageIntent.setSupportAnimation(false);
        module.startPage(DataModule.G_CURRENT_FRAME, pageIntent);
    }

    @Override
    protected void initPage() {
        super.initPage();

        PageCategoryBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_category, null, false);
        setContentView(pageBinding.getRoot());

        mCategoryViewModel = new CategoryViewModel(this);

        pageBinding.pageCategoryRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = mCategoryViewModel.getItem(position).getItemType();
                if (itemType == BaseItem.ITEM_TYPE_SECTION || itemType == BaseItem.ITEM_TYPE_BOTTOM) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        pageBinding.pageCategoryRecyclerView.setLayoutManager(gridLayoutManager);

        pageBinding.setModel(mCategoryViewModel);

        bindPageTitleBar(R.id.page_category_title_bar);
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();

        if (!mIsMenuLoadComplete) {
            mCategoryViewModel.queryFirstTwoCategoryLevel(new Observer<Result>() {
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

        mCategoryViewModel.clearSubscription();
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        BarMenuTextItem centerMenu = new BarMenuTextItem(1, "分类");
        centerMenu.setTag(TitleBar.Position.CENTER);
        centerMenu.setTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s5));
        menu.addItem(centerMenu);

        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return true;
    }

}
