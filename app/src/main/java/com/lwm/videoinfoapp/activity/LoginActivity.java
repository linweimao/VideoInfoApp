package com.lwm.videoinfoapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.LoginResponse;
import com.lwm.videoinfoapp.util.AppConfig;
import com.lwm.videoinfoapp.util.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private EditText mEtAccount;
    private EditText mEtPwd;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mEtAccount.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();
                if (StringUtils.isEmpty(account)) {
//                    Toast.makeText(LoginActivity.this, R.string.account_hint, Toast.LENGTH_SHORT).show();
                    showToast(getString(R.string.account_hint));
                    return;
                }
                if (StringUtils.isEmpty(pwd)) {
//                    Toast.makeText(LoginActivity.this, R.string.pwd_hint, Toast.LENGTH_SHORT).show();
                    showToast(getString(R.string.pwd_hint));
                    return;
                }
                login(account, pwd);
            }
        });
    }

//    private void login(String account, String pwd) {
//        // 第一步创建 OkHttpClient 对象
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        Map m = new HashMap();
//        m.put("mobile", account);
//        m.put("password", pwd);
//        JSONObject jsonObject = new JSONObject(m);
//        String jsonStr = jsonObject.toString();
//        RequestBody requestBodyJson =
//                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
//                        , jsonStr);
//        // 第二步创建 Request 对象
//        Request request = new Request.Builder()
//                .url(AppConfig.BASE_URl + "/app/login")
//                .addHeader("contentType", "application/json;charset=utf-8")
//                .post(requestBodyJson)
//                .build();
//        // 第三步创建 Call 回调对象
//        final Call call = client.newCall(request);
//        // 第四步发起请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(result);
//                    }
//                });
//            }
//        });
//    }

    // 采用封装 okhttp 的方式
    private void login(String account, String pwd) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params).postRequest(new RequestCallback() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(res);
//                    }
//                });
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 0) {
                    String token = loginResponse.getToken();
//                    SharedPreferences sp = getSharedPreferences("sp_lwm", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("token", token);
//                    editor.commit();
                    saveStringToSp("token", token);
                    navigateTo(HomeActivity.class);
                    showToastSync(getString(R.string.login_success));
                } else {
                    showToastSync(getString(R.string.login_fail));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.getMessage());
            }
        });
    }
}