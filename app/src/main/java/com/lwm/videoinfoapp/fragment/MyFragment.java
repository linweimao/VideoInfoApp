package com.lwm.videoinfoapp.fragment;

import android.view.View;
import android.widget.ImageView;

import com.lwm.videoinfoapp.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {

    @BindView(R.id.img_header)
    ImageView imgHeader;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_header, R.id.rl_collect, R.id.rl_skin, R.id.rl_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header: // 头像
                break;
            case R.id.rl_collect: // 我的收藏
                break;
            case R.id.rl_skin: // 换肤
                break;
            case R.id.rl_logout: // 退出登录
                break;
        }
    }
}