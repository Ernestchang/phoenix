package com.yulin.act.page.main.category.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;

import com.yulin.act.page.base.PageImpl;
import com.yulin.act.model.BaseItem;
import com.yulin.act.page.main.category.viewmodel.CategoryViewModel;
import com.yulin.act.util.Util;
import com.yulin.applib.bar.BarMenuContainer;
import com.yulin.applib.bar.BarMenuTextItem;
import com.yulin.applib.bar.TitleBar;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageCategoryBinding;

public class CategoryPage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageCategoryBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_category, null, false);
        setContentView(pageBinding.getRoot());

        final CategoryViewModel categoryViewModel = new CategoryViewModel(this);

        pageBinding.pageCategoryRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = categoryViewModel.getItem(position).getItemType();
                if (itemType == BaseItem.ITEM_TYPE_SECTION || itemType == BaseItem.ITEM_TYPE_BOTTOM) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        pageBinding.pageCategoryRecyclerView.setLayoutManager(gridLayoutManager);

        pageBinding.setModel(categoryViewModel);

        bindPageTitleBar(R.id.page_category_title_bar);
    }

    @Override
    protected boolean onCreatePageTitleBarMenu(BarMenuContainer menu) {
        BarMenuTextItem centerMenu = new BarMenuTextItem(1, "分类");
        centerMenu.setTag(TitleBar.Position.CENTER);
        centerMenu.setTextSize(Util.getRDimensionPixelSize(R.dimen.txt_s5));
        menu.addItem(centerMenu);

        return true;
    }

}
