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

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    private String[] mTitles = {"首页", "收藏", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect,
            R.mipmap.my_unselect}; // 未选中时的图标
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_select,
            R.mipmap.my_selected}; // 选中时的图标
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ViewPager mViewpager;
    private CommonTabLayout mCommontablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
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
    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mCommontablayout = (CommonTabLayout) findViewById(R.id.commontablayout);
    }
}