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

    private int categoryId;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private int pageNum = 1; // ??????????????????
    private VideoAdapter mVideoAdapter;
    private List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    // ????????????
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    /**
     * ?????????????????????
     */
    protected int mCurPos = -1;
    /**
     * ???????????????????????????????????????????????????????????????
     */
    protected int mLastPos = mCurPos;


    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(int categoryId) {
        VideoFragment fragment = new VideoFragment();
        fragment.categoryId = categoryId;
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

    // ????????????????????????
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new xyz.doikki.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //??????VideoViewManager?????????????????????
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

            // ????????? ItemView(????????????)????????????????????????(?????????????????????)????????????ItemView??????????????????????????????????????????????????????????????????
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
//                refreshlayout.finishRefresh(2000, false); // ??????false??????????????????
                pageNum = 1; // ????????????pageNum(?????????)????????? 1
                getVideoList(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000, false); // ??????false??????????????????
                pageNum++; // ????????????pageNum(?????????)++
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
     * ??????onPause????????????super????????????????????????
     * ????????????????????????????????????onPause?????????
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
     * ??????onResume????????????super????????????????????????
     * ????????????????????????????????????onResume?????????
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //???????????????????????????
        startPlay(mLastPos);
    }

    /**
     * PrepareView?????????
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * ????????????
     *
     * @param position ????????????
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //????????????
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //?????????????????????PrepareView??????????????????????????????isDissociate???????????????true, ???????????????isDissociate?????????
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //???????????????VideoView?????????VideoViewManager????????????????????????????????????
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    // ?????????????????????
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

    // isRefresh???????????????/??????
    //    true?????????
    //    false?????????
    private void getVideoList(boolean isRefresh) {
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", token);
            // "page" ??? "limit"??????????????????
            params.put("page", pageNum); // ??????????????????
            params.put("limit", ApiConfig.PAGE_SIZE);  // ???????????????5?????????
            Api.config(ApiConfig.VIDEO_LIST, params).getRequest(new RequestCallback() {
                @Override
                public void onSuccess(String res) {
                    Log.d("onSuccess???", res);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRefresh) {
                                mRefreshLayout.finishRefresh(true); // ?????????????????????
                            } else {
                                mRefreshLayout.finishLoadMore(true); // ?????????????????????
                            }
                            VideoListResponse response = new Gson().fromJson(res, VideoListResponse.class);
                            if (response != null && response.getCode() == 0) {
                                /*
                                List<VideoEntity> datas = response.getPage().getList();
                                // ??????(mRefreshLayout.setOnRefreshListener)??????(mRefreshLayout.setOnLoadMoreListener)??????
                                // ???????????? VideoAdapter ?????????????????????????????????????????????????????????VideoAdapter??????
                                VideoAdapter videoAdapter = new VideoAdapter(getActivity(), datas);
                                mRecyclerView.setAdapter(videoAdapter);
                                */
                                List<VideoEntity> list = response.getPage().getList();
                                // ???????????????????????????????????????
                                if (list != null && list.size() > 0) {
                                    if (isRefresh) {
                                        // ?????????
                                        datas = list;
                                    } else {
                                        // ?????????????????? list
                                        datas.addAll(list);
                                    }
                                    // ?????????????????? VideoAdapter
                                    mVideoAdapter.setDatas(datas);
                                    mVideoAdapter.notifyDataSetChanged(); // ?????? RecyclerView ????????????(????????????)
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
                    // ????????????????????????????????????
                    if (isRefresh) {
                        mRefreshLayout.finishRefresh(true); // ?????????????????????
                    } else {
                        mRefreshLayout.finishLoadMore(true); // ?????????????????????
                    }
                }
            });
        } else {
            navigateTo(LoginActivity.class);
        }
    }

    /*
    // ?????? BaseFragment ?????????????????????????????????????????????
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        return view;
    }

    // ?????? BaseFragment ?????????????????????????????????????????????
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
//                refreshlayout.finishRefresh(2000, false); // ??????false??????????????????
                pageNum = 1; // ????????????pageNum(?????????)????????? 1
                getVideoList(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000, false); // ??????false??????????????????
                pageNum++; // ????????????pageNum(?????????)++
                getVideoList(false);
            }
        });
        getVideoList(true);
    }
*/
}