package com.lwm.videoinfoapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    // 主线程中提示 Toast
    public void showToastSync(String msg) {
        Looper.prepare(); // 为当前线程提供一个Looper的实例
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show(); // 展示消息
        Looper.loop(); // 在线程中执行消息队列，让 Looper 开始工作，从消息队列里取消息，处理消息
    }

    public void navigateTo(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    // SharedPreferences 本地存储
    public void saveStringToSp(String key, String val) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_lwm", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key", val);
        editor.commit();
    }

    protected String getStringFromSp(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_lwm", MODE_PRIVATE);
        return sp.getString(key, "");
    }
}