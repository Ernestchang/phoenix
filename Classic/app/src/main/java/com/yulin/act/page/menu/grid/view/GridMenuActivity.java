package com.yulin.act.page.menu.grid.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.classic.R;

/**
 * 诗经每篇标题长度短，列表中不必每篇独占一行，所以设置为一行显示四个标题。
 */
public class GridMenuActivity extends Module {

    private static final short DEFAULT_SPAN_COUNT = 4;

    private static final String EXTRA_CATEGORY_ID = "extra_category_id";
    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private static final String EXTRA_SPAN_COUNT = "extra_span_count";

    private int mCategoryId;
    private int mSpanCount;
    private String mCategoryName;

    public static void gotoModule(Page page, int categoryId, String categoryName, int spanCount) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_CATEGORY_ID, categoryId);
        extras.putString(EXTRA_CATEGORY_NAME, categoryName);
        extras.putInt(EXTRA_SPAN_COUNT, spanCount);

        page.startModule(extras, GridMenuActivity.class);
    }

    @Override
    protected void receiveData(Intent intent) {
        super.receiveData(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_CATEGORY_ID)) {
                mCategoryId = extras.getInt(EXTRA_CATEGORY_ID);
            }
            if (extras.containsKey(EXTRA_CATEGORY_NAME)) {
                mCategoryName = extras.getString(EXTRA_CATEGORY_NAME);
            }
            if (extras.containsKey(EXTRA_SPAN_COUNT)) {
                mSpanCount = extras.getInt(EXTRA_SPAN_COUNT, DEFAULT_SPAN_COUNT);
            }
        }
    }

    @Override
    protected void initModule() {
        super.initModule();

        DataBindingUtil.setContentView(this, R.layout.activity_frame);

        GridMenuPage.startPage(this, mCategoryId, mCategoryName, mSpanCount);
    }

}
