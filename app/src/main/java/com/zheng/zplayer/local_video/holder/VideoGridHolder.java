package com.zheng.zplayer.local_video.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zheng.zplayer.R;


public class VideoGridHolder extends RecyclerView.ViewHolder {
    public RoundedImageView thumbnail;
    public TextView title;
    public CardView item;

    public VideoGridHolder(View itemView) {
        super(itemView);
        item = (CardView) itemView.findViewById(R.id.grid_item_cv);
        thumbnail = (RoundedImageView) itemView.findViewById(R.id.thumbnail);
        title = (TextView) itemView.findViewById(R.id.title);
    }


}
