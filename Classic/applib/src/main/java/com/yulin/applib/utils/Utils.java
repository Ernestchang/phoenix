package com.yulin.applib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by liulei0905 on 2016/11/1.
 * 工具类
 * 与业务无关的工具方法
 */

public class Utils {

    //##############################################################################################   系统相关   #####################
    /**
     * 是不是4.4以上的版本
     */
    public static boolean isKitkatUpVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    //##############################################################################################   网络相关   #####################
    /**
     * 检查网络是否可用
     */
    public static boolean IsNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取网络类型
     * */
    public static int getNetworkType(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }

        return -1;
    }

}
