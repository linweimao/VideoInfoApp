package com.lwm.videoinfoapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.api.Api;
import com.lwm.videoinfoapp.api.ApiConfig;
import com.lwm.videoinfoapp.api.RequestCallback;
import com.lwm.videoinfoapp.entity.BaseResponse;
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.listener.OnItemChildClickListener;
import com.lwm.videoinfoapp.listener.OnItemClickListener;
import com.lwm.videoinfoapp.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import xyz.doikki.videocontroller.component.PrepareView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<VideoEntity> mDatas;

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;

    // 刷新(mRefreshLayout.setOnRefreshListener)加载(mRefreshLayout.setOnLoadMoreListener)时，
    // 如果通过 VideoAdapter 构造函数传入数据则每次都要重复创建一个VideoAdapter对象
    public void setDatas(List<VideoEntity> datas) {
        this.mDatas = datas;
    }

    // 刷新(mRefreshLayout.setOnRefreshListener)加载(mRefreshLayout.setOnLoadMoreListener)时，
    // 如果通过 VideoAdapter 构造函数传入数据则每次都要重复创建一个VideoAdapter对象
    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    // 刷新(mRefreshLayout.setOnRefreshListener)加载(mRefreshLayout.setOnLoadMoreListener)时，
    // 如果通过 VideoAdapter 构造函数传入数据则每次都要重复创建一个VideoAdapter对象
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
//        holder.tvDz.setText(String.valueOf(videoEntity.getLikeNum()));
//        holder.tvComment.setText(String.valueOf(videoEntity.getCommentNum()));
//        holder.tvCollect.setText(String.valueOf(videoEntity.getCollectNum()));
        if (videoEntity.getVideoSocialEntity() != null) {
            int likeNum = videoEntity.getVideoSocialEntity().getLikenum();
            int commentNum = videoEntity.getVideoSocialEntity().getCommentnum();
            int collectNum = videoEntity.getVideoSocialEntity().getCollectnum();
            boolean flagLike = videoEntity.getVideoSocialEntity().isFlagLike();
            boolean flagCollect = videoEntity.getVideoSocialEntity().isFlagCollect();
            if (flagLike) { // 已点赞
                holder.tvDz.setTextColor(mContext.getResources().getColor(R.color.color_e21918));
                holder.imgDzan.setImageResource(R.mipmap.dianzan_select);
            }
            if (flagCollect) { // 已收藏
                holder.tvCollect.setTextColor(mContext.getResources().getColor(R.color.color_e21918));
                holder.imgCollect.setImageResource(R.mipmap.collect_select);
            }
            holder.tvDz.setText(String.valueOf(likeNum));
            holder.tvComment.setText(String.valueOf(commentNum));
            holder.tvCollect.setText(String.valueOf(collectNum));
            holder.flagCollect = flagCollect;
            holder.flagLike = flagLike;
        }
        // 异步加载图片
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                .into(holder.imgHeader);
//        Picasso.with(mContext)
//                .load(videoEntity.getCoverurl())
//                .into(holder.imgCover);
        Picasso.with(mContext)
                .load(videoEntity.getCoverurl())
                .into(holder.mThumb);
        holder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView imgCollect, imgDzan;
        private ImageView imgHeader;
        //        private ImageView imgCover;
        public FrameLayout mPlayerContainer;
        public ImageView mThumb; // 缩略图
        public PrepareView mPrepareView;
        public int mPosition;
        private boolean flagCollect; // 收藏标志(用于判断用户是否收藏)
        private boolean flagLike; // 点赞标志(用于判断用户是否点赞)

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            imgCollect = view.findViewById(R.id.img_collect);
            imgDzan = view.findViewById(R.id.img_like);
            imgHeader = view.findViewById(R.id.img_header);
//            imgCover = view.findViewById(R.id.img_cover);
            mPlayerContainer = view.findViewById(R.id.player_container);
            mPrepareView = view.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(xyz.doikki.videocontroller.R.id.thumb);
            // 视频播放器的点击事件
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            // 整个子ItemView的点击事件
            if (mOnItemClickListener != null) {
                view.setOnClickListener(this);
            }
            // 收藏点击事件
            imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int collectNum = Integer.parseInt(tvCollect.getText().toString());
                    if (flagCollect) { // 已收藏
                        // 大于 0才减，否则为负数
                        if (collectNum > 0) {
                            tvCollect.setText(String.valueOf(--collectNum));
                            tvCollect.setTextColor(mContext.getResources().getColor(R.color.color_161616));
                            imgCollect.setImageResource(R.mipmap.collect);
//                            updateCount(mDatas.get(mPosition).getVid(), 1, !flagCollect);
                        }
                    } else { // 未收藏
                        tvCollect.setText(String.valueOf(++collectNum));
                        tvCollect.setTextColor(mContext.getResources().getColor(R.color.color_e21918));
                        imgCollect.setImageResource(R.mipmap.collect_select);
//                        updateCount(mDatas.get(mPosition).getVid(), 1, !flagCollect);
                    }
                    flagCollect = !flagCollect; // 给 UI做显示用
                    updateCount(mDatas.get(mPosition).getVid(), 1, flagCollect);
                }
            });
            // 点赞点击事件
            imgDzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(tvDz.getText().toString());
                    if (flagLike) { // 已点赞
                        // 大于 0才减，否则为负数
                        if (likeNum > 0) {
                            tvDz.setText(String.valueOf(--likeNum));
                            tvDz.setTextColor(mContext.getResources().getColor(R.color.color_161616));
                            imgDzan.setImageResource(R.mipmap.dianzan);
//                            updateCount(mDatas.get(mPosition).getVid(), 2, !flagLike);
                        }
                    } else { // 未点赞
                        tvDz.setText(String.valueOf(++likeNum));
                        tvDz.setTextColor(mContext.getResources().getColor(R.color.color_e21918));
                        imgDzan.setImageResource(R.mipmap.dianzan_select);
//                        updateCount(mDatas.get(mPosition).getVid(), 2, !flagLike);
                    }
                    flagLike = !flagLike; // 给 UI做显示用
                    updateCount(mDatas.get(mPosition).getVid(), 2, flagLike);
                }
            });
            //通过tag将 ViewHolder和 itemView绑定
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }
        }
    }

    /**
     * 点赞、收藏接口
     *    请求地址: /app/videolist/updateCount
     *    请求方法: post
     *    请求头: Content-Type: application/json;charset=UTF-8
     *    请求体实例：
     *       "vid": 视频id
     *       "type": (0 评论, 1 收藏, 2 点赞)
     *       "flag": (true 已收藏, false 未收藏 / true 已点赞, false 未点赞)
     */
    private void updateCount(int vid, int type, boolean flag) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("vid", vid);
        params.put("type", type);
        params.put("flag", flag);
        Api.config(ApiConfig.VIDEO_UPDATE_COUNT, params).postRequest(mContext, new RequestCallback() {
            @Override
            public void onSuccess(String res) {
                Log.d("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                if (baseResponse.getCode() == 0) {

                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("onFailure", e.getMessage());
            }
        });
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}