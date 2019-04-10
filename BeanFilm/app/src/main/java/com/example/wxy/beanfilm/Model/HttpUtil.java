package com.example.wxy.beanfilm.Model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by WXY on 2019/2/2.
 */

public class HttpUtil {
    //仅仅带地址
    public static void sendRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //地址+ 参数
    public static void sendRequest(String address, RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    //回调方法见第一行代码p337
}
