package com.yulin.act.config;

import android.app.Activity;
import android.content.SharedPreferences;

import com.yulin.act.utils.Util;

/**
 * Created by liulei0905 on 2016/11/1.
 * 管理SharedPreference
 */

public class SpManager {

    private static final String SP_NAME = "shared_prefers";

    /**
     * 是否自动刷新
     * */
    private static final String SP_KEY_AUTO_REFRESH = "sp_key_auto_refresh";

    /**
     * wifi连接时循环请求间隔时间
     * */
    private static final String SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_WIFI = "sp_key_auto_refresh_interval_time_on_wifi";

    /**
     * 移动数据连接时循环请求间隔时间
     * */
    private static final String SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_DATA = "sp_key_auto_refresh_interval_time_on_data";

    private static SpManager mInstance;
    private SharedPreferences mSharedPreferences;

    private SpManager() {
        mSharedPreferences = Util.getContext().getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
    }

    public static SpManager getInstance() {
        if (mInstance == null) {
            synchronized (SpManager.class) {
                if (mInstance == null) {
                    mInstance = new SpManager();
                }
            }
        }

        return mInstance;
    }

    //##############################################################################################   存取方法区   ##########
    public void setIsAutoRefresh(boolean value) {
        setBoolean(SP_KEY_AUTO_REFRESH, value);
    }

    public boolean getIsAutoRefresh() {
        return getBoolean(SP_KEY_AUTO_REFRESH, false);
    }

    public void setAutoRefreshIntervalTimeOnWifi(int value) {
        setInt(SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_WIFI, value);
    }

    public int getAutoRefreshIntervalTimeOnWifi() {
        return getInt(SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_WIFI, 0);
    }

    public void setAutoRefreshIntervalTimeOnData(int value) {
        setInt(SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_DATA, value);
    }

    public int getAutoRefreshIntervalTimeOnData() {
        return getInt(SP_KEY_AUTO_REFRESH_INTERVAL_TIME_ON_DATA, 0);
    }

    //##############################################################################################   底层方法区   ##########
    private void setBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key, boolean defVal) {
        return mSharedPreferences.getBoolean(key, defVal);
    }

    private void setInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    private int getInt(String key, int defVal) {
        return mSharedPreferences.getInt(key, defVal);
    }

}
