package com.hc.poem.config;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hc.poem.db.DbHelper;
import com.umeng.analytics.MobclickAgent;

public class PoemApplication extends Application {

    private static PoemApplication poemApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        poemApplication = this;

        // Init database.
        DbHelper.getSQLiteDb(this);

        // 配置友盟统计
        // 集成/普通测试
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);    // 普通统计场景类型

        // 初始化图片加载模式Fresco
        Fresco.initialize(this);
    }

    public static PoemApplication getInstance() {
        return poemApplication;
    }

}
