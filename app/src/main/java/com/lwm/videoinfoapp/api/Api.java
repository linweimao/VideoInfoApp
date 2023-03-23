package com.lwm.videoinfoapp.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    // 配置聚合接口API
    public static Api configJuhe(String url) {
        mClient = new OkHttpClient.Builder()
                .build();
        requestUrl = url;
        return getInstance();
    }

    // post请求
    public void postRequest(Context context, RequestCallback callback) {
        SharedPreferences sp = context.getSharedPreferences("sp_lwm", MODE_PRIVATE);
        String token = sp.getString("token", "");
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        // 第二步创建 Request 对象
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=utf-8")
                .addHeader("token", token)
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

    // get请求
    public void getRequest(Context context, RequestCallback callback) {
        SharedPreferences sp = context.getSharedPreferences("sp_lwm", MODE_PRIVATE);
        String token = sp.getString("token", "");
        String url = getAppendUrl(requestUrl, mParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token)
                .get()
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    // 当 token失效时返回登录页进行重新登录
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    // token为空(没有登录) 或 token失效时 code均为401)
                    // expire为失效时间
                    if ("401".equals(code)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }
        });
    }

    // 拼接 url
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            // 迭代器
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }

    // 聚合数据 get请求
    public void getJuheRequest(Context context, RequestCallback callback) {
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String errorCode = jsonObject.getString("error_code");
                    // token为空(没有登录) 或 token失效时 code均为401)
                    // expire为失效时间
                    if (!"0".equals(errorCode)) {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }
        });
    }
}
