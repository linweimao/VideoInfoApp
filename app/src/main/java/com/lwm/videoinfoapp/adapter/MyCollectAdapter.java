package com.lwm.videoinfoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lwm.videoinfoapp.R;
import com.lwm.videoinfoapp.entity.VideoEntity;
import com.lwm.videoinfoapp.listener.OnItemChildClickListener;
import com.lwm.videoinfoapp.listener.OnItemClickListener;
import com.lwm.videoinfoapp.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.doikki.videocontroller.component.PrepareView;

public class MyCollectAdapter extends RecyclerView.Adapter<MyCollectAdapter.ViewHolder> {

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
    public MyCollectAdapter(Context context) {
        this.mContext = context;
    }

    // 刷新(mRefreshLayout.setOnRefreshListener)加载(mRefreshLayout.setOnLoadMoreListener)时，
    // 如果通过 VideoAdapter 构造函数传入数据则每次都要重复创建一个VideoAdapter对象
    public MyCollectAdapter(Context context, List<VideoEntity> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mycollect_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoEntity videoEntity = mDatas.get(position);
        holder.tvTitle.setText(videoEntity.getVtitle());
        holder.tvAuthor.setText(videoEntity.getAuthor());
        // 异步加载图片
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform()) // 将图片转化为定义的圆形图片
                .into(holder.imgHeader);
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
        private ImageView imgHeader;
        public FrameLayout mPlayerContainer;
        public ImageView mThumb; // 缩略图
        public PrepareView mPrepareView;
        public int mPosition;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            imgHeader = view.findViewById(R.id.img_header);
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

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}