package com.yulin.act.views.base;

import android.net.ConnectivityManager;
import android.os.Handler;

import com.yulin.act.config.SpManager;
import com.yulin.act.utils.Util;
import com.yulin.applib.utils.Utils;

public abstract class PageImpl extends PageTitleBar {

    /**
     * 是否循环请求数据
     */
    private boolean mIsRepeatRequest;

    /**
     * 用于发起循环请求的handler
     */
    private Handler mAutoRequestHandler;

    /**
     * 循环请求任务
     */
    private Runnable mAutoRequestTask;

    @Override
    protected void initPage() {
        mAutoRequestHandler = new Handler();
    }

    @Override
    protected void onPagePause() {
        super.onPagePause();

        cancelAutoRequest();
    }

    @Override
    protected void onPageDestroy() {
        // 清空MessageQueue中的Message
        cancelAutoRequest();
        mAutoRequestHandler = null;

        super.onPageDestroy();
    }

    /**
     * 启动循环请求
     * 不指定间隔时间，根据网络设置使用相应时间
     */
    protected void startAutoRequest() {
        startAutoRequest(-1);
    }

    /**
     * 启动循环请求
     *
     * @param intervalTime 循环请求间隔时间，单位s
     */
    protected void startAutoRequest(final int intervalTime) {
        // 避免重复启动循环请求
        cancelAutoRequest();

        if (Utils.IsNetworkAvailable(Util.getContext())) {
            // 在启动循环之前请求一次数据。保证如果启动循环请求失败，可以请求一次数据。
            requestData();
        }

        if (!SpManager.getInstance().getIsAutoRefresh()) {
            // 如果不允许自动刷新，中止启动
            return;
        }

        // 定义循环请求任务
        mAutoRequestTask = new Runnable() {
            @Override
            public void run() {
                // 请求数据
                if (Utils.IsNetworkAvailable(Util.getContext())) {
                    requestData();
                }

                // 发起延时，请求下一次数据
                final int tInterval = getInterval(intervalTime);
                if (tInterval > 0 && SpManager.getInstance().getIsAutoRefresh() && mAutoRequestHandler != null) {
                    mAutoRequestHandler.postDelayed(this, tInterval);
                } else {
                    cancelAutoRequest();
                }
            }
        };

        // 确定循环请求第一次请求间隔时间，然后发起延时请求
        final int tInterval = getInterval(intervalTime);
        if (tInterval > 0 && SpManager.getInstance().getIsAutoRefresh() && mAutoRequestHandler != null) {
            mAutoRequestHandler.postDelayed(mAutoRequestTask, tInterval);
        }

        mIsRepeatRequest = true;
    }

    /**
     * 停止循环请求
     */
    protected void cancelAutoRequest() {
        mIsRepeatRequest = false;
        if (mAutoRequestHandler != null)
            mAutoRequestHandler.removeCallbacks(mAutoRequestTask);
    }

    /**
     * 请求数据
     */
    protected void requestData() {

    }

    /**
     * 获取循环请求间隔时间
     */
    private int getInterval(int intervalTime) {
        final int interval;

        if (intervalTime == -1) {
            interval = getIntervalByNetworkType() * 1000;
        } else if (intervalTime > 0) {
            interval = intervalTime * 1000;
        } else {
            interval = 0;
        }

        return interval;
    }

    /**
     * 根据网络类型，获取对应的循环请求时间时隔
     */
    private int getIntervalByNetworkType() {
        int interval = SpManager.getInstance().getAutoRefreshIntervalTimeOnWifi();

        final int networkType = Utils.getNetworkType(Util.getContext());
        if (networkType == ConnectivityManager.TYPE_MOBILE) {
            interval = SpManager.getInstance().getAutoRefreshIntervalTimeOnData();
        } else if (networkType == ConnectivityManager.TYPE_WIFI) {
            interval = SpManager.getInstance().getAutoRefreshIntervalTimeOnWifi();
        }

        return interval;
    }

    /**
     * 是否循环自动请求
     */
    protected boolean getIsAutoRefresh() {
        return mIsRepeatRequest;
    }

}
