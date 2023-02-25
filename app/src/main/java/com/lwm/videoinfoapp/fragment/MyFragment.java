package com.lwm.videoinfoapp.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.activity.MyCollectActivity;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

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
                navigateTo(MyCollectActivity.class);
                break;
            case R.id.rl_skin: // 换肤
                String skin = getStringFromSp("skin");
                if (skin.equals("night")) {
                    // 恢复应用默认皮肤
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    saveStringToSp("skin", "default");
                } else {
                    /**
                     * "night": 皮肤包名称 与 res-night文件的 night文件名保持一致
                     * SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN: 皮肤包加载策略(_night命名在后面)
                     */
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                    saveStringToSp("skin", "night");
                }

                break;
            case R.id.rl_logout: // 退出登录
                removeByKey("token");
                navigateToWithFlag(LoginActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
    }
}