package com.yulin.act.page.main.view;

import android.databinding.DataBindingUtil;

import com.yulin.applib.module.Module;
import com.yulin.classic.R;
import com.yulin.classic.databinding.ActivityMainBinding;

public class MainActivity extends Module {

    private ActivityMainBinding mActivityBinding;

    @Override
    protected void initModule() {
        super.initModule();
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

}
