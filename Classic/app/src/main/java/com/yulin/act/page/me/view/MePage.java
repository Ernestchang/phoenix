package com.yulin.act.page.me.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.yulin.act.page.base.PageImpl;
import com.yulin.classic.R;
import com.yulin.classic.databinding.PageMeBinding;

/**
 * Created by liulei0905 on 2016/11/4.
 */

public class MePage extends PageImpl {

    @Override
    protected void initPage() {
        super.initPage();

        PageMeBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.page_me, null, false);
        setContentView(pageBinding.getRoot());
    }

}
