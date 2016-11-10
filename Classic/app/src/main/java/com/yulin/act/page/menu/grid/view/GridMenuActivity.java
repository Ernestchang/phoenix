package com.yulin.act.page.menu.grid.view;

import android.databinding.DataBindingUtil;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.classic.R;

/**
 * 诗经每篇标题长度短，列表中不必每篇独占一行，所以设置为一行显示四个标题。
 */
public class GridMenuActivity extends Module {

    @Override
    protected void initModule() {
        super.initModule();

        DataBindingUtil.setContentView(this, R.layout.activity_frame);

        GridMenuPage.startPage(this);
    }

    public static void gotoModule(Page page) {
        page.startModule(GridMenuActivity.class);
    }

}
