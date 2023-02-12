package com.lwm.videoinfoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.entity.NewsEntity;
import com.lwm.videoinfoapp.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsEntity> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

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
        NewsEntity newsEntity = mDatas.get(position);
        if (type == 1) {
            ViewHolderOne vh = (ViewHolderOne) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + mContext.getResources().getString(R.string.news_item_comment));
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;

            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                    .into(vh.header);
            // 当 type == 1时只有一张图片，所以 get(0).getThumbUrl()
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        } else if (type == 2) {
            ViewHolderTwo vh = (ViewHolderTwo) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + mContext.getResources().getString(R.string.news_item_comment));
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;

            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                    .into(vh.header);
            // 当 type == 2时有三张图片
            // get(0).getThumbUrl()为第一张
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.pic1);
            // get(1).getThumbUrl()为第二张
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(1).getThumbUrl())
                    .into(vh.pic2);
            // get(2).getThumbUrl()为第三张
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(2).getThumbUrl())
                    .into(vh.pic3);
        } else {
            ViewHolderThree vh = (ViewHolderThree) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + mContext.getResources().getString(R.string.news_item_comment));
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;

            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                    .into(vh.header);
            // 当 type == 3时只有一张图片，所以 get(0).getThumbUrl()
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        }
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

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header; // 头像
        private ImageView thumb; // 缩略图
        private NewsEntity newsEntity;

        public ViewHolderOne(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            // ItemView的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header; // 头像
        private ImageView pic1, pic2, pic3; // 缩略图
        private NewsEntity newsEntity;

        public ViewHolderTwo(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);
            // ItemView的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public class ViewHolderThree extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header; // 头像
        private ImageView thumb; // 缩略图
        private NewsEntity newsEntity;

        public ViewHolderThree(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            // ItemView的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
}