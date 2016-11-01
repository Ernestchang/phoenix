package com.yulin.classic;

import android.app.Application;

public class ClassicApplication extends Application {

    private static ClassicApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static ClassicApplication getInstance() {
        return mInstance;
    }

}
