package com.yulin.applib.network;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient = null;
    private static Context appContext;


    public static void doInit(Context context) {
        appContext = context;
        getOkHttpClient();

    }

    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            if (appContext != null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                builder.writeTimeout(10, TimeUnit.SECONDS);
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.readTimeout(10, TimeUnit.SECONDS);
                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                Cache cache = new Cache(appContext.getExternalCacheDir(), cacheSize);
                builder.cache(cache);

                mOkHttpClient = builder.build();
            }
        }

        return mOkHttpClient;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient client = null;
        if (appContext != null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.writeTimeout(10, TimeUnit.SECONDS);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(appContext.getCacheDir(), cacheSize);
            builder.cache(cache);

            client = builder.build();
        }


        return client;
    }

    public static void cancelRequest(String tag) {
        if (getOkHttpClient() == null) {
            return;
        }
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
    }

    /**
     * 该方法不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        OkHttpClient okHttpClient = createOkHttpClient();
        if (okHttpClient != null) {
            return okHttpClient.newCall(request).execute();
        }
        return null;
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        if (getOkHttpClient() != null) {
            mOkHttpClient.newCall(request).enqueue(responseCallback);
        }
    }


    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        if (getOkHttpClient() != null) {
            mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onResponse(Call arg0, Response arg1) throws IOException {
                    // TODO Auto-generated method stub

                }

            });
        }

    }

    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static final String CHARSET_NAME = "UTF-8";


}
