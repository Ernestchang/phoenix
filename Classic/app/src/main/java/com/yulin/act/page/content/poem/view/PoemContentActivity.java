package com.yulin.act.page.content.poem.view;

import android.databinding.DataBindingUtil;

import com.yulin.applib.module.Module;
import com.yulin.applib.page.Page;
import com.yulin.classic.R;

/**
 * Created by liulei0905 on 2016/11/9.
 * <p>
 * 显示诗、词、曲内容
 */

public class PoemContentActivity extends Module {

    @Override
    protected void initModule() {
        super.initModule();

        DataBindingUtil.setContentView(this, R.layout.activity_frame);

        PoemContentPage.startPage(this);
    }

    public static void gotoModule(Page page) {
        page.startModule(PoemContentActivity.class);
    }

}
