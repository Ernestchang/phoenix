package com.yulin.applib.network;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http代理类 处理数据收发的核心类 依赖于Context
 * 
 * @version 1.0
 *
 */
public class OKHttpManager {


    /**
     * 上传文件
     * 
     * @param url d
     * @param filePath s
     * @param tag d
     */
    public static void uploadFileRequst(String url, String filePath, String tag) {
        File file = new File(filePath);
        if (file.exists()) {
            String fileName = file.getName();

            RequestBody fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("file", fileName, fileBody);
            builder.addFormDataPart("some-field", "some-value");

            Request.Builder reqBuilder = new Request.Builder();
            reqBuilder.url(url);
            if (tag != null) {
                reqBuilder.tag(tag);
            }
            reqBuilder.post(builder.build());

            OkHttpUtil.enqueue(reqBuilder.build(), new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        } else {
        }

    }

    /**
     * 根据文件名获取文件的MIME类型
     * 
     * @param fileName d
     * @return d
     */
    public static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(fileName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    protected String getFailureMessage(String message, String content) {
        if (message == null)
            message = "null";

        if (content == null)
            content = "请求出错";

        return content;
    }


    public static void cancelRequest(String tag) {
        if (tag != null) {
            OkHttpUtil.cancelRequest(tag);
        }
    }



}
