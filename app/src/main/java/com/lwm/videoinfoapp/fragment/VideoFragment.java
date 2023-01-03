package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.adapter.VideoAdapter;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.entity.VideoListResponse;
import com.lwm.videoinfoapp.util.StringUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoFragment extends BaseFragment {

    private String mTitle;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private int pageNum = 1; // 分页：第几页
    private VideoAdapter mVideoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String title) {
        VideoFragment fragment = new VideoFragment();
        fragment.mTitle = title;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerview);
        mRefreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoAdapter = new VideoAdapter(getActivity());
        mRecyclerView.setAdapter(mVideoAdapter);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000, false); // 传入false表示刷新失败
                pageNum = 1; // 刷新时将pageNum(第几页)重置为 1
                getVideoList(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000, false); // 传入false表示加载失败
                pageNum++; // 加载时将pageNum(第几页)++
                getVideoList(false);
            }
        });
        getVideoList(true);
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        return view;
    }

    // 通过 BaseFragment 进行封装，使用抽象方法进行调用
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoAdapter = new VideoAdapter(getActivity());
        mRecyclerView.setAdapter(mVideoAdapter);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000, false); // 传入false表示刷新失败
                pageNum = 1; // 刷新时将pageNum(第几页)重置为 1
                getVideoList(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000, false); // 传入false表示加载失败
                pageNum++; // 加载时将pageNum(第几页)++
                getVideoList(false);
            }
        });
        getVideoList(true);
    }
*/

    // isRefresh：区分刷新/加载
    //    true：刷新
    //    false：加载
    private void getVideoList(boolean isRefresh) {
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", token);
            // "page" 和 "limit"用于实现分页
            params.put("page", pageNum); // 分页：第几页
            params.put("limit", ApiConfig.PAGE_SIZE);  // 分页：每页5条数据
            Api.config(ApiConfig.VIDEO_LIST, params).getRequest(new RequestCallback() {
                @Override
                public void onSuccess(String res) {
                    Log.d("onSuccess：", res);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRefresh) {
                                mRefreshLayout.finishRefresh(true); // 将刷新动画关闭
                            } else {
                                mRefreshLayout.finishLoadMore(true); // 将加载动画关闭
                            }
                            VideoListResponse response = new Gson().fromJson(res, VideoListResponse.class);
                            if (response != null && response.getCode() == 0) {
                                /*
                                List<VideoEntity> datas = response.getPage().getList();
                                // 刷新(mRefreshLayout.setOnRefreshListener)加载(mRefreshLayout.setOnLoadMoreListener)时，
                                // 如果通过 VideoAdapter 构造函数传入数据则每次都要重复创建一个VideoAdapter对象
                                VideoAdapter videoAdapter = new VideoAdapter(getActivity(), datas);
                                mRecyclerView.setAdapter(videoAdapter);
                                */
                                List<VideoEntity> list = response.getPage().getList();
                                // 判断接口返回的数据是否为空
                                if (list != null && list.size() > 0) {
                                    if (isRefresh) {
                                        // 刷新时
                                        datas = list;
                                    } else {
                                        // 加载时，添加 list
                                        datas.addAll(list);
                                    }
                                    // 将数据设置进 VideoAdapter
                                    mVideoAdapter.setDatas(datas);
                                    mVideoAdapter.notifyDataSetChanged(); // 通知 RecyclerView 刷新页面(刷新数据)
                                } else {
                                    if (isRefresh) {
                                        showToast(getString(R.string.refresh_toast));
                                    } else {
                                        showToast(getString(R.string.loadmore_toast));
                                    }
                                }
                            }
                        }
                    });
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