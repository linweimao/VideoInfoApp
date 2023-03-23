package com.lwm.videoinfoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.entity.NewsInfo;

import java.util.List;

/**
 * @Author: linweimao
 * @Version: 1.0
 */
public class HotNewsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<NewsInfo> newsList;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;

    public HotNewsFragmentAdapter(Context context, List<NewsInfo> newsList) {
        this.context = context;
        this.newsList = newsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        NewsInfo newsInfo = newsList.get(position);
        if (null != newsInfo.getThumbnail_pic_s() && !newsInfo.getThumbnail_pic_s().isEmpty()) {
            if (null != newsInfo.getThumbnail_pic_s02() && !newsInfo.getThumbnail_pic_s02().isEmpty()) {
                if (newsInfo.getThumbnail_pic_s03() != null && !newsInfo.getThumbnail_pic_s03().isEmpty()) {
                    // 三张图片
                    return 3;
                } else {
                    // 两张图片
                    return 2;
                }
            } else {
                // 一张图片
                return 1;
            }
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.hot_news_item_zero, viewGroup, false);
            return new HotNewsViewHolderZero(view);
        } else if (viewType == 1) {
            View view = inflater.inflate(R.layout.hot_news_item_one, viewGroup, false);
            return new HotNewsViewHolderOne(view);
        } else if (viewType == 2) {
            View view = inflater.inflate(R.layout.hot_news_item_two, viewGroup, false);
            return new HotNewsViewHolderTwo(view);
        } else {
            View view = inflater.inflate(R.layout.hot_news_item_three, viewGroup, false);
            return new HotNewsViewHolderThree(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        NewsInfo newsInfo = newsList.get(position);
        if (type == 0) {
            HotNewsViewHolderZero vh = (HotNewsViewHolderZero) holder;
            vh.title.setText(newsInfo.getTitle());
            vh.category.setText(newsInfo.getCategory());
            vh.author.setText(newsInfo.getAuthor_name());
            vh.time.setText(newsInfo.getDate());
        } else if (type == 1) {
            HotNewsViewHolderOne vh = (HotNewsViewHolderOne) holder;
            vh.title.setText(newsInfo.getTitle());
            Glide.with(context).load(newsInfo.getThumbnail_pic_s()).into(vh.pic1);
            vh.category.setText(newsInfo.getCategory());
            vh.author.setText(newsInfo.getAuthor_name());
            vh.time.setText(newsInfo.getDate());
        } else if (type == 2) {
            HotNewsViewHolderTwo vh = (HotNewsViewHolderTwo) holder;
            vh.title.setText(newsInfo.getTitle());
            vh.category.setText(newsInfo.getCategory());
            vh.author.setText(newsInfo.getAuthor_name());
            vh.time.setText(newsInfo.getDate());
            Glide.with(context).load(newsInfo.getThumbnail_pic_s()).into(vh.pic1);
            Glide.with(context).load(newsInfo.getThumbnail_pic_s02()).into(vh.pic2);
        } else {
            HotNewsViewHolderThree vh = (HotNewsViewHolderThree) holder;
            vh.title.setText(newsInfo.getTitle());
            Glide.with(context).load(newsInfo.getThumbnail_pic_s()).into(vh.pic1);
            Glide.with(context).load(newsInfo.getThumbnail_pic_s02()).into(vh.pic2);
            Glide.with(context).load(newsInfo.getThumbnail_pic_s03()).into(vh.pic3);
            vh.category.setText(newsInfo.getCategory());
            vh.author.setText(newsInfo.getAuthor_name());
            vh.time.setText(newsInfo.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // 零张图片
    public class HotNewsViewHolderZero extends RecyclerView.ViewHolder {

        private TextView title; // 标题
        private TextView category; // 类别
        private TextView author; // 作者
        private TextView time; // 时间

        public HotNewsViewHolderZero(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            category = view.findViewById(R.id.category);
            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);

            // ItemView的点击事件
            OnItemClick(view);
        }
    }

    // 一张图片
    public class HotNewsViewHolderOne extends RecyclerView.ViewHolder {

        private TextView title; // 标题
        private ImageView pic1; // 缩略图
        private TextView category; // 类别
        private TextView author; // 作者
        private TextView time; // 时间

        public HotNewsViewHolderOne(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            pic1 = view.findViewById(R.id.thumb);
            category = view.findViewById(R.id.category);
            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);

            // ItemView的点击事件
            OnItemClick(view);
        }
    }

    // 二张图片
    public class HotNewsViewHolderTwo extends RecyclerView.ViewHolder {

        private TextView title; // 标题
        private TextView category; // 类别
        private TextView author; // 作者
        private TextView time; // 时间
        private ImageView pic1, pic2; // 缩略图

        public HotNewsViewHolderTwo(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            category = view.findViewById(R.id.category);
            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);

            // ItemView的点击事件
            OnItemClick(view);
        }
    }

    // 三张图片
    public class HotNewsViewHolderThree extends RecyclerView.ViewHolder {

        private TextView title; // 标题
        private ImageView pic1, pic2, pic3; // 缩略图
        private TextView category; // 类别
        private TextView author; // 作者
        private TextView time; // 时间

        public HotNewsViewHolderThree(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);
            category = view.findViewById(R.id.category);
            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);
            // ItemView的点击事件
            OnItemClick(view);
        }
    }

    // ItemView的点击事件
    private void OnItemClick(View view) {
        if (onItemClick != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onclick(view);
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onclick(View view);
    }
}