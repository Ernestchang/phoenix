package com.hc.poem.page.base;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends TitleBarActivity {

    @Override
    protected void onResume() {
        super.onResume();

        // 必须调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据。
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

}
