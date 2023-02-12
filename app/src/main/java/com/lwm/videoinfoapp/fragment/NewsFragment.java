package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.activity.WebActivity;
import com.lwm.videoinfoapp.adapter.NewsAdapter;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.NewsEntity;
import com.lwm.videoinfoapp.entity.NewsListResponse;
import com.lwm.videoinfoapp.util.StringUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private NewsAdapter mNewsAdapter;
    private List<NewsEntity> datas = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private int pageNum = 1; // 分页：第几页

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 将数据设置进 NewsAdapter
                    mNewsAdapter.setDatas(datas);
                    mNewsAdapter.notifyDataSetChanged(); // 通知 RecyclerView 刷新页面(刷新数据)
                    break;
            }
        }
    };

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
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.setOnItemClickListener(this);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000, false); // 传入false表示刷新失败
                pageNum = 1; // 刷新时将pageNum(第几页)重置为 1
                getNewsList(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000, false); // 传入false表示加载失败
                pageNum++; // 加载时将pageNum(第几页)++
                getNewsList(false);
            }
        });
        getNewsList(true);
    }

    // ItemView点击事件的回调
    @Override
    public void onItemClick(Serializable obj) {
        NewsEntity newsEntity = (NewsEntity) obj;
        // 拼接访问h5页面的Url
        String url = "http://192.168.16.104:8089/newsDetail?title=" + newsEntity.getAuthorName();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        navigateToWithBundle(WebActivity.class, bundle);
    }

    /**
     * isRefresh：区分刷新/加载
     * true：刷新
     * false：加载
     */
    private void getNewsList(boolean isRefresh) {
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", token);
            // "page" 和 "limit"用于实现分页
            params.put("page", pageNum); // 分页：第几页
            params.put("limit", ApiConfig.PAGE_SIZE);  // 分页：每页5条数据
            Api.config(ApiConfig.NEWS_LIST, params).getRequest(new RequestCallback() {
                @Override
                public void onSuccess(String res) {
                    Log.d("onSuccess：", res);
                    if (isRefresh) {
                        mRefreshLayout.finishRefresh(true); // 将刷新动画关闭
                    } else {
                        mRefreshLayout.finishLoadMore(true); // 将加载动画关闭
                    }
                    NewsListResponse response = new Gson().fromJson(res, NewsListResponse.class);
                    if (response != null && response.getCode() == 0) {
                        List<NewsEntity> list = response.getPage().getList();
                        // 判断接口返回的数据是否为空
                        if (list != null && list.size() > 0) {
                            if (isRefresh) {
                                // 刷新时
                                datas = list;
                            } else {
                                // 加载时，添加 list
                                datas.addAll(list);
                            }
                            mHandler.sendEmptyMessage(0);
                        } else {
                            if (isRefresh) {
                                showToastSync(getString(R.string.refresh_toast));
                            } else {
                                showToastSync(getString(R.string.loadmore_toast));
                            }
                        }
                    }
//                    showToastSync(res);
                }

                @Override
                public void onFailure(Exception e) {
                    // 接口请求失败也要关闭动画
                    if (isRefresh) {
                        mRefreshLayout.finishRefresh(true); // 将刷新动画关闭
                    } else {
                        mRefreshLayout.finishLoadMore(true); // 将加载动画关闭
                    }
                }
            });
        } else {
            navigateTo(LoginActivity.class);
        }
    }
}