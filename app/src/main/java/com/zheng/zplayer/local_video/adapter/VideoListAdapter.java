package com.zheng.zplayer.local_video.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.application.ZPlayerApplication;
import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.local_video.holder.VideoListHolder;
import com.zheng.zplayer.utils.FileUtils;
import com.zheng.zplayer.utils.PreferenceUtils;

import java.io.File;
import java.util.List;


public class VideoListAdapter extends VideoBaseAdapter {
    private int mBackgroundColor;
    private int mTitleTextColor;
    private int mSizeTextColor;


    public VideoListAdapter(Activity activity, List<ZMedia> data) {
        mActivity = activity;
        mData = data;
        initColor();
    }

    private void initColor() {
        int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
        switch (mode) {
            case ZPlayerConst.DAY_MODE:
                mBackgroundColor = mActivity.getResources().getColor(R.color.item_background_day);
                mSizeTextColor = mActivity.getResources().getColor(R.color.text_color_day);
                mTitleTextColor = mActivity.getResources().getColor(R.color.text_color_day);
                break;
            case ZPlayerConst.NIGHT_MODE:
                mBackgroundColor = mActivity.getResources().getColor(R.color.item_background_night);
                mSizeTextColor = mActivity.getResources().getColor(R.color.text_color_night);
                mTitleTextColor = mActivity.getResources().getColor(R.color.text_color_night);
                break;
        }
    }

    @Override
    public VideoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_file_item, parent,
                false);
        VideoListHolder holder = new VideoListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoListHolder listHolder = (VideoListHolder) holder;
        ZMedia f = mData.get(position);
        listHolder.file_size.setText(FileUtils.showFileSize(f.file_size));
        listHolder.file_size.setTextColor(mSizeTextColor);
        listHolder.title.setText(f.title);
        listHolder.title.setTextColor(mTitleTextColor);
        if (f.thumb_path != null) {
            Picasso.with(ZPlayerApplication.getContext()).load((new File(f.thumb_path))).into(listHolder.thumbnail);
        }else{
            Picasso.with(ZPlayerApplication.getContext()).load(R.drawable.video_file).into(listHolder.thumbnail);
        }
        listHolder.mCardView.setCardBackgroundColor(mBackgroundColor);
        listHolder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });
        listHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }
        });

    }

    public void setNightMode() {
        initColor();
        notifyDataSetChanged();
    }

    public void setDayMode() {
        initColor();
        notifyDataSetChanged();
    }

}
