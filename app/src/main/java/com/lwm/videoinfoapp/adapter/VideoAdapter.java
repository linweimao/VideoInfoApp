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
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<VideoEntity> mDatas;

    public VideoAdapter(Context context, List<VideoEntity> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoEntity videoEntity = mDatas.get(position);
        holder.tvTitle.setText(videoEntity.getVtitle());
        holder.tvAuthor.setText(videoEntity.getAuthor());
        holder.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
        holder.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
        holder.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));
        // 异步加载图片
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                .into(holder.imgHeader);
        Picasso.with(mContext)
                .load(videoEntity.getCoverurl())
                .into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView imgHeader;
        private ImageView imgCover;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            imgHeader = view.findViewById(R.id.img_header);
            imgCover = view.findViewById(R.id.img_cover);
        }
    }
}