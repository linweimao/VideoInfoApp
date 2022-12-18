package com.lwm.videoinfoapp.api;

public interface RequestCallback {
    // 请求成功
    void onSuccess(String res);

    // 请求失败
    void onFailure(Exception e);
}
