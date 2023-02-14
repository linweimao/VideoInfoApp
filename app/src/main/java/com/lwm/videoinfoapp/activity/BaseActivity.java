package com.lwm.videoinfoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Activity 的基类
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initLayout());
        initView();
        initData();
    }

    protected abstract int initLayout(); // Layout布局

    protected abstract void initView(); // 初始化 View

    protected abstract void initData(); // 初始化数据

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    // 主线程中提示 Toast
    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    public void navigateToWithFlag(Class cls, int flags) {
        Intent intent = new Intent(mContext, cls);
        intent.setFlags(flags);
        startActivity(intent);
    }

    // SharedPreferences 本地存储
    public void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_lwm", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }
}