package com.zheng.zplayer.local_video.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.local_video.holder.VideoGridHolder;
import com.zheng.zplayer.utils.PreferenceUtils;

import java.io.File;
import java.util.List;


public class VideoGridAdapter extends VideoBaseAdapter {
    private int mTitleTextColor;
    private int mBackgroundCoor;

    public VideoGridAdapter(Activity activity, List<ZMedia> data) {
        mActivity = activity;
        mData = data;
        initColor();
    }

    private void initColor() {
        int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
        if(mode==ZPlayerConst.DAY_MODE){
            mTitleTextColor = mActivity.getResources().getColor(R.color.text_color_day);
            mBackgroundCoor = mActivity.getResources().getColor(R.color.item_background_day);
        }else{
            mTitleTextColor = mActivity.getResources().getColor(R.color.text_color_night);
            mBackgroundCoor = mActivity.getResources().getColor(R.color.item_background_night);
        }
    }


    @Override
    public VideoGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_file_item_grid, parent,
                false);
        VideoGridHolder holder = new VideoGridHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ZMedia f = mData.get(position);
        VideoGridHolder gridHolder = (VideoGridHolder) holder;
        gridHolder.title.setText(f.title);
        gridHolder.title.setTextColor(mTitleTextColor);
        gridHolder.item.setCardBackgroundColor(mBackgroundCoor);
        if(f.thumb_path!=null){
            Picasso.with(mActivity).load(new File(f.thumb_path)).into(gridHolder.thumbnail);
        }else{
            Picasso.with(mActivity).load(R.drawable.video_file).into(gridHolder.thumbnail);
        }
        gridHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });
        gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }
        });
    }
   public void setDayMode(){
       initColor();
       notifyDataSetChanged();
   }
    public void setNightMode(){
        initColor();
        notifyDataSetChanged();
    }
}
