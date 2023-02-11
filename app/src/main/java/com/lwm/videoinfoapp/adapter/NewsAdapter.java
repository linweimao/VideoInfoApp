package com.lwm.videoinfoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.entity.NewsEntity;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsEntity> mDatas;

    public void setDatas(List<NewsEntity> datas) {
        this.mDatas = datas;
    }

    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public NewsAdapter(Context context, List<NewsEntity> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    // 当要实现多种ItemView时，需要实现该方法
    // 该方法的返回值会传递给 onCreateViewHolder 的 viewType参数
    @Override
    public int getItemViewType(int position) {
        int type = mDatas.get(position).getType();
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_one, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_two, parent, false);
            return new ViewHolderTwo(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_three, parent, false);
            return new ViewHolderThree(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == 1) {
            ViewHolderOne vh = (ViewHolderOne) holder;
        } else if (type == 2) {
            ViewHolderTwo vh = (ViewHolderTwo) holder;
        } else {
            ViewHolderThree vh = (ViewHolderThree) holder;
        }
        NewsEntity newsEntity = mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        public ViewHolderOne(@NonNull View view) {
            super(view);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        public ViewHolderTwo(@NonNull View view) {
            super(view);
        }
    }

    public class ViewHolderThree extends RecyclerView.ViewHolder {

        public ViewHolderThree(@NonNull View view) {
            super(view);
        }
    }
}