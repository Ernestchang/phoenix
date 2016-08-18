package com.hc.poem.util;

import android.content.res.Resources;
import android.os.Build;

import com.hc.poem.config.PoemApplication;

/**
 * Created by liulei0905 on 2016/8/18.
 *
 */
public class Util {

    private static Resources getRes() {
        return PoemApplication.getInstance().getResources();
    }

    /**
     * 获取资源文件里的dimension
     *
     * @param resId
     * @return
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

    /**
     * 获取字符串资源
     * */
    public static String getRString(int resId) {
        return getRes().getString(resId);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getRes().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getRDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static boolean isKITKATUpVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
