package com.lwm.videoinfoapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwm.videoinfoapp.R;

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
        TextView tv = view.findViewById(R.id.title);
        tv.setText(mTitle);
        return view;
    }
}