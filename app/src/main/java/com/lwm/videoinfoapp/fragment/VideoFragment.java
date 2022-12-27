package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.adapter.VideoAdapter;
import com.lwm.videoinfoapp.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    private String mTitle;

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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 测试数据
        List<VideoEntity> datas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            VideoEntity ve = new VideoEntity();
            ve.setTitle("韭菜盒子新做法，不发面不烫面");
            ve.setName("大胃王");
            ve.setDzCount(i * 2);
            ve.setCollectCount(i * 4);
            ve.setCommentCount(i * 6);
            datas.add(ve);
        }
        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), datas);
        recyclerView.setAdapter(videoAdapter);
        return view;
    }
}