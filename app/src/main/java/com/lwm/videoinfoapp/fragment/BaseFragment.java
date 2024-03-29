package com.lwm.videoinfoapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.doikki.videoplayer.player.VideoViewManager;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 当 mRootView为空时创建mRootView，不为空时复用 mRootView(避免重新加载initLayout()布局)
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayout(), container, false);
            initView();
        }
        mUnbinder = ButterKnife.bind(this, mRootView); // 将 mRootView 和 ButterKnife进行绑定
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind(); // 将 mRootView 和 ButterKnife进行解绑
    }

    protected abstract int initLayout(); // Layout布局

    protected abstract void initView(); // 初始化 View

    protected abstract void initData(); // 初始化数据

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

    // 跳转带参数
    public void navigateToWithBundle(Class cls,Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void navigateToWithFlag(Class cls, int flags) {
        Intent intent = new Intent(getActivity(), cls);
        intent.setFlags(flags);
        startActivity(intent);
    }

    // SharedPreferences 本地存储
    public void saveStringToSp(String key, String val) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_lwm", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    protected String getStringFromSp(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_lwm", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    protected void removeByKey(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_lwm", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    /**
     * 子类可通过此方法直接拿到VideoViewManager
     */
    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }
}