package com.yulin.act.page.activity.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yulin.act.page.base.PageImpl;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageActivityBinding;

/**
 * Created by liulei0905 on 2016/11/4.
 */

public class ActivityPage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageActivityBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_activity, null, false);
        setContentView(pageBinding.getRoot());
    }

}
