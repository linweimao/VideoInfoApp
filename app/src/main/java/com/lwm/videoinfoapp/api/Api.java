package com.lwm.videoinfoapp.api;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static String requestUrl;
    private static HashMap<String, Object> mParams;
    private static OkHttpClient mClient;
    private static volatile Api mApi;

    private Api() {
    }

    public static Api getInstance() {
        if (mApi == null) {
            synchronized (Api.class) {
                if (mApi == null) {
                    mApi = new Api();
                }
            }
        }
        return mApi;
    }

    public static Api config(String url, HashMap<String, Object> params) {
        mClient = new OkHttpClient.Builder()
                .build();
        requestUrl = ApiConfig.BASE_URl + url;
        mParams = params;
        return getInstance();
    }

    // post请求
    public void postRequest(RequestCallback callback) {
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        // 第二步创建 Request 对象
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        // 第三步创建 Call 回调对象
        final Call call = mClient.newCall(request);
        // 第四步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }
}
