package com.example.coolweather.android.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendRequestWithOkHttp(String addr, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(addr)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
