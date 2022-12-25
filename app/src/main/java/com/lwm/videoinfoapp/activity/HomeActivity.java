package com.lwm.videoinfoapp.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.adapter.MyPagerAdapter;
import com.lwm.videoinfoapp.entity.TabEntity;
import com.lwm.videoinfoapp.fragment.CollectFragment;
import com.lwm.videoinfoapp.fragment.HomeFragment;
import com.lwm.videoinfoapp.fragment.MyFragment;
import com.lwm.videoinfoapp.view.FixedViewPager;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    private String[] mTitles = {"首页", "收藏", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect,
            R.mipmap.my_unselect}; // 未选中时的图标
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_selected,
            R.mipmap.my_selected}; // 选中时的图标
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ViewPager mViewpager;
    private CommonTabLayout mCommontablayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    protected void initView() {
        mViewpager = (FixedViewPager) findViewById(R.id.viewpager);
        mCommontablayout = (CommonTabLayout) findViewById(R.id.commontablayout);
    }

    @Override
    protected void initData() {
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(CollectFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mCommontablayout.setTabData(mTabEntities);
        // 设置底部导航栏点击监听
        mCommontablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
        // ViewPager 滑动监听(页面和底部导航栏按钮同时变化)
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCommontablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewpager.setCurrentItem(0); // 设置选中第一个
    }
/*
    // 通过 BaseActivity 进行封装，使用抽象方法进行调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
*/
}