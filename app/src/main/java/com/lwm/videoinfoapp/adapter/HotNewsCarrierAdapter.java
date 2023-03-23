package com.lwm.videoinfoapp.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lwm.videoinfoapp.fragment.HotNewsFragment;
import com.lwm.videoinfoapp.util.Constant;

public class HotNewsCarrierAdapter extends FragmentPagerAdapter {

    public HotNewsCarrierAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = HotNewsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TYPE, Constant.NEWS_TYPE[i]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return Constant.NEWS_TYPE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.NEWS_TITLES[position];
    }
}