package com.lwm.videoinfoapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.util.StringUtils;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity {

    private EditText mEtAccount;
    private EditText mEtPwd;
    private Button mBtnRegister;

/*
    // 通过 BaseActivity 进行封装，使用抽象方法进行调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
*/

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    protected void initView() {
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mEtAccount.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();
                if (StringUtils.isEmpty(account)) {
                    showToast(getString(R.string.account_hint));
                    return;
                }
                if (StringUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.pwd_hint));
                    return;
                }
                register(account, pwd);
            }
        });
    }

    // 采用封装 okhttp 的方式
    private void register(String account, String pwd) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", account);
        params.put("password", pwd);
        Api.config(ApiConfig.REGISTER, params).postRequest(this, new RequestCallback() {
            @Override
            public void onSuccess(String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(res);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.getMessage());
            }
        });
    }
}