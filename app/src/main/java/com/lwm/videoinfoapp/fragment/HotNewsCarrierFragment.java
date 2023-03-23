package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.adapter.HotNewsCarrierAdapter;

/**
 * HotNewsFragment的载体 Fragment
 */
public class HotNewsCarrierFragment extends BaseFragment {

    private TabLayout mHotNewsCarrierTabLayout;
    private ViewPager mHotNewsCarrierViewPager;
    private PagerAdapter mAdapter;

    public HotNewsCarrierFragment() {

    }

    public static HotNewsCarrierFragment newInstance() {
        HotNewsCarrierFragment fragment = new HotNewsCarrierFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_hot_news_carrier;
    }

    @Override
    protected void initView() {
        mHotNewsCarrierTabLayout = (TabLayout) mRootView.findViewById(R.id.hot_news_carrier_tab_layout);
        mHotNewsCarrierViewPager = (ViewPager) mRootView.findViewById(R.id.hot_news_carrier_view_pager);
        mHotNewsCarrierTabLayout.setupWithViewPager(mHotNewsCarrierViewPager);
        mAdapter = new HotNewsCarrierAdapter(getFragmentManager());
        mHotNewsCarrierViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }
}