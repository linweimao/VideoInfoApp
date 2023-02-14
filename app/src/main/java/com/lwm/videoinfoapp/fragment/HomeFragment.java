package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.adapter.HomeAdapter;
import com.lwm.videoinfoapp.adapter.MyPagerAdapter;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.CategoryEntity;
import com.lwm.videoinfoapp.entity.VideoCategoryResponse;
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.entity.VideoListResponse;
import com.lwm.videoinfoapp.util.StringUtils;
import com.lwm.videoinfoapp.view.FixedViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //    private final String[] mTitles = {
//            "关注", "推荐", "新闻", "游戏", "娱乐", "综艺"
//    };
    private String[] mTitles;
    private EditText mEtSearch;
    private SlidingTabLayout mSlidingtablayout;
    private ViewPager mViewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mEtSearch = (EditText) mRootView.findViewById(R.id.et_search);
        mSlidingtablayout = (SlidingTabLayout) mRootView.findViewById(R.id.slidingtablayout);
        mViewPager = (FixedViewPager) mRootView.findViewById(R.id.fixedviewpager);
    }

    @Override
    protected void initData() {
//        for (String title : mTitles) {
//            mFragments.add(VideoFragment.newInstance(title));
//        }
//        // 当 ViewPager下 Fragment 很多时切换会出现异常(下标越界、页面空白)
//        // 解决方案：
//        //    设置预加载(启动 HomeFragment 时预加载全部 Fragment)
//        mViewPager.setOffscreenPageLimit(mFragments.size());
//        mViewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
//        mSlidingtablayout.setViewPager(mViewPager); // SlidingTabLayout 绑定 ViewPager

        // 从接口中获取视频类型
        getVideoCategoryList();
    }

    private void getVideoCategoryList() {
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String, Object> params = new HashMap<>();
            Api.config(ApiConfig.VIDEO_CATEGORY_LIST, params).getRequest(getActivity(), new RequestCallback() {
                @Override
                public void onSuccess(String res) {
                    Log.d("onSuccess：", res);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoCategoryResponse response = new Gson().fromJson(res, VideoCategoryResponse.class);
                            if (response != null && response.getCode() == 0) {
                                List<CategoryEntity> list = response.getPage().getList();
                                // 判断接口返回的数据是否为空
                                if (list != null && list.size() > 0) {
                                    mTitles = new String[list.size()];
                                    for (int i = 0; i < list.size(); i++) {
                                        mTitles[i] = list.get(i).getCategoryName();
                                        mFragments.add(VideoFragment.newInstance(list.get(i).getCategoryId()));
                                    }

                                    // 当 ViewPager下 Fragment 很多时切换会出现异常(下标越界、页面空白)
                                    // 解决方案：
                                    //    设置预加载(启动 HomeFragment 时预加载全部 Fragment)
                                    mViewPager.setOffscreenPageLimit(mFragments.size());
                                    mViewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
                                    mSlidingtablayout.setViewPager(mViewPager); // SlidingTabLayout 绑定 ViewPager

                                }
                            }
                        }
                    });
//                    showToastSync(res);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } else {
            navigateTo(LoginActivity.class);
        }
    }

/*
    // 通过 BaseFragment 进行封装，使用抽象方法进行调用
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mSlidingtablayout = (SlidingTabLayout) view.findViewById(R.id.slidingtablayout);
        mViewPager = (FixedViewPager) view.findViewById(R.id.fixedviewpager);
        return view;
    }

    // 通过 BaseFragment 进行封装，使用抽象方法进行调用
    // 在 onCreateView 后执行
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (String title : mTitles) {
            mFragments.add(VideoFragment.newInstance(title));
        }
        // 当 ViewPager下 Fragment 很多时切换会出现异常(下标越界、页面空白)
        // 解决方案：
        //    设置预加载(启动 HomeFragment 时预加载全部 Fragment)
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
        mSlidingtablayout.setViewPager(mViewPager); // SlidingTabLayout 绑定 ViewPager
    }
*/

}