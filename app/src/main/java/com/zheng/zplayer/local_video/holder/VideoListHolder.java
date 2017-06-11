package com.zheng.zplayer.local_video.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheng.zplayer.R;


public class VideoListHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;
    public TextView title;
    public TextView file_size;
    public CardView mCardView;

    public VideoListHolder(View itemView) {
        super(itemView);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        title = (TextView) itemView.findViewById(R.id.title);
        file_size = (TextView) itemView.findViewById(R.id.file_size);
        mCardView = (CardView) itemView.findViewById(R.id.local_video_cv);
    }


}
