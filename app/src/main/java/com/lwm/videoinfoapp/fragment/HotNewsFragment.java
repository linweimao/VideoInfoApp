package com.lwm.videoinfoapp.fragment;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.HotNewsDetailActivity;
import com.lwm.videoinfoapp.adapter.HotNewsFragmentAdapter;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.News;
import com.lwm.videoinfoapp.entity.NewsInfo;
import com.lwm.videoinfoapp.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 显示新闻列表的fragment，实现view层的方法
 */
public class HotNewsFragment extends BaseFragment {

    private static final String TAG = HotNewsFragment.class.getSimpleName();
    private RecyclerView mFcRecyclerview;
    private List<NewsInfo> mNewsList;
    private HotNewsFragmentAdapter mHotNewsFragmentAdapter;

    public HotNewsFragment() {

    }

    public static HotNewsFragment newInstance() {
        HotNewsFragment fragment = new HotNewsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_hot_news;
    }

    @Override
    protected void initView() {
        mFcRecyclerview = (RecyclerView) mRootView.findViewById(R.id.fc_recyclerview);
        mNewsList = new ArrayList<>();
        mFcRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mHotNewsFragmentAdapter = new HotNewsFragmentAdapter(getContext(), mNewsList);
        mFcRecyclerview.setAdapter(mHotNewsFragmentAdapter);
        mHotNewsFragmentAdapter.setOnItemClick(new HotNewsFragmentAdapter.OnItemClick() {
            @Override
            public void onclick(View view) {
                int position = mFcRecyclerview.getChildAdapterPosition(view);
                Log.i(TAG, "onclick：position=" + position);
                String newsUrl = mNewsList.get(position).getUrl();
                Intent intent = new Intent(getActivity(), HotNewsDetailActivity.class);
                intent.putExtra(Constant.URL, newsUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        getHotNewsData();
    }

    /**
     * 获取新闻列表数据
     */
    private void getHotNewsData() {
        String type = getArguments().getString(Constant.TYPE);
        Api.configJuhe(Constant.JUHE_URL + "?type=" + type + "&key=" + Constant.JUHE_APP_KEY)
                .getJuheRequest(getActivity(), new RequestCallback() {
                    @Override
                    public void onSuccess(String res) {
                        Gson gson = new Gson();
                        News news = gson.fromJson(res, News.class);
                        NewsInfo[] data = news.getResult().getData();
                        final List<NewsInfo> newsInfos = Arrays.asList(data);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadNewsList(newsInfos);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Looper.prepare();
                        error("网络请求失败，原因：" + e.getMessage());
                    }
                });
    }

    public void loadNewsList(List<NewsInfo> newsList) {
        this.mNewsList.addAll(newsList);
        mHotNewsFragmentAdapter.notifyDataSetChanged();
    }

    private void error(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}