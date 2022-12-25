package com.lwm.videoinfoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lwm.videoinfoapp.activity.BaseActivity;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.activity.RegisterActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnLogin;
    private Button mBtnRegister;

/*
    // 通过 BaseActivity 进行封装，使用抽象方法进行调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
*/

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    protected void initView() {
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 登录
            case R.id.btn_login:
//                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
                navigateTo(LoginActivity.class);
                break;
            // 注册
            case R.id.btn_register:
//                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(registerIntent);
                navigateTo(RegisterActivity.class);
                break;
        }
    }
}