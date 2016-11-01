package com.yulin.applib.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.yulin.applib.log.Tracer;


/**
 * 监控网络状态的模块
 * 从@Module 中派生而来
 * 默认注册了网络广播，一旦网络状态发生变化，回调onNetworkStatusChanged方法
 * emoney
 *
 * @version 1.0
 */
public abstract class NettingModule extends Module {

    private ConnectionChangeReceiver mConnectionChangeReceiver = null;

    private boolean mIsNetworkAvailable = false;

    protected abstract void onNetworkStatusChanged(boolean isConnected, int networkType);

    protected void beforeCreate(Bundle savedInstanceState) {
        if (mConnectionChangeReceiver == null) {
            mConnectionChangeReceiver = new ConnectionChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            try {
                registerReceiver(mConnectionChangeReceiver, filter);
            } catch (Exception e) {
            }
        }
    }

    protected void onDestory() {
        super.onDestroy();
    }

    public void finish() {
        try {
            if (mConnectionChangeReceiver != null) {
                unregisterReceiver(mConnectionChangeReceiver);
            }
        } catch (Exception e) {

        }
        super.finish();
    }

    class ConnectionChangeReceiver extends BroadcastReceiver {
        static final String TAG = "ConnectionChangeReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Tracer.D(TAG, "网络状态改变");
            // 获得网络连接服务
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        Tracer.D(TAG, "正在使用WIFI网络...");
                        mIsNetworkAvailable = true;
                        onNetworkStatusChanged(true, ConnectivityManager.TYPE_WIFI);
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        mIsNetworkAvailable = true;
                        onNetworkStatusChanged(true, ConnectivityManager.TYPE_MOBILE);
                    }
                } else {
                    // not connected to the internet
                    mIsNetworkAvailable = false;
                    onNetworkStatusChanged(false, ConnectivityManager.TYPE_MOBILE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        return mIsNetworkAvailable;
    }


}
