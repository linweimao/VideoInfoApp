package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.adapter.HomeAdapter;
import com.lwm.videoinfoapp.adapter.MyPagerAdapter;
import com.lwm.videoinfoapp.view.FixedViewPager;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "关注", "推荐", "新闻", "游戏", "娱乐", "综艺"
    };
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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