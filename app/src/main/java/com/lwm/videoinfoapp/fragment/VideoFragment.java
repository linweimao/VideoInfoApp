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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoFragment extends BaseFragment {

    private String mTitle;
    private RecyclerView mRecyclerView;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String title) {
        VideoFragment fragment = new VideoFragment();
        fragment.mTitle = title;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getVideoList();
    }

    private void getVideoList() {
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", token);
            Api.config(ApiConfig.VIDEO_LIST_ALL, params).getRequest(new RequestCallback() {
                @Override
                public void onSuccess(String res) {
                    Log.d("onSuccess：", res);
                    VideoListResponse response = new Gson().fromJson(res, VideoListResponse.class);
                    if (response != null && response.getCode() == 0) {
                        List<VideoEntity> datas = response.getPage().getList();
                        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), datas);
                        mRecyclerView.setAdapter(videoAdapter);
                    }
//                    showToastSync(res);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } else {
            navigateTo(LoginActivity.class);
        }
    }
}