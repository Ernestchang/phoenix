package com.yulin.act.utils;

import android.content.Context;
import android.content.res.Resources;

import com.yulin.classic.ClassicApplication;

/**
 * Created by liulei0905 on 2016/11/1.
 * 工具类
 */

public class Util {

    //##############################################################################################   应用信息   ##########
    public static Context getContext() {
        return ClassicApplication.getInstance().getApplicationContext();
    }

    //##############################################################################################   获取资源   ##########
    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int statusBarHeight = 0;

        int resourceId = getRes().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getRDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    /**
     * 获取资源管理器
     */
    public static Resources getRes() {
        return ClassicApplication.getInstance().getResources();
    }

    /**
     * 获取资源文件里的dimension
     */
    public static int getRDimensionPixelSize(int resId) {
        return getRes().getDimensionPixelSize(resId);
    }

    /**
     * 获取资源文件里的dimension
     *
     * @param resId
     * @return
     */
    public static float getRDimension(int resId) {
        return getRes().getDimension(resId);
    }

}
