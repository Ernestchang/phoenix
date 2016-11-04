package com.yulin.act.page.category.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yulin.act.page.base.PageImpl;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageCategoryBinding;

/**
 * Created by liulei0905 on 2016/11/4.
 */

public class CategoryPage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageCategoryBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_category, null, false);
        setContentView(pageBinding.getRoot());
    }

}
