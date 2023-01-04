package com.lwm.videoinfoapp.fragment;

import android.content.pm.ActivityInfo;
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
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.MainActivity;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.activity.LoginActivity;
import com.lwm.videoinfoapp.adapter.VideoAdapter;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.entity.VideoListResponse;
import com.lwm.videoinfoapp.listener.OnItemChildClickListener;
import com.lwm.videoinfoapp.util.StringUtils;
import com.lwm.videoinfoapp.util.Tag;
import com.lwm.videoinfoapp.util.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class VideoFragment extends BaseFragment implements OnItemChildClickListener {

    private String mTitle;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private int pageNum = 1; // 分页：第几页
    private VideoAdapter mVideoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    // 视频播放
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;


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
        initVideoView();
        mRecyclerView = mRootView.findViewById(R.id.recyclerview);
        mRefreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    // 初始化视频播放器
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new xyz.doikki.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == xyz.doikki.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    protected void initData() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mVideoAdapter = new VideoAdapter(getActivity());
        mVideoAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mVideoAdapter);
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            // 当一个 ItemView(一个子项)从手机界面脱离时(向上滑动时脱离)，此时该ItemView看不到了，就会回调此方法，此时释放此视频播放
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * PrepareView被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    // 释放视频播放器
    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

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
}