package com.lwm.videoinfoapp.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.adapter.NewsAdapter;
import com.lwm.videoinfoapp.entity.NewsEntity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private NewsAdapter mNewsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerview);
        mRefreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNewsAdapter = new NewsAdapter(getActivity());
        for (int i = 0; i < 15; i++) {
            int type = i % 3 + 1;
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setType(type);
            datas.add(newsEntity);
        }
        mNewsAdapter.setDatas(datas);
        mRecyclerView.setAdapter(mNewsAdapter);
    }
}