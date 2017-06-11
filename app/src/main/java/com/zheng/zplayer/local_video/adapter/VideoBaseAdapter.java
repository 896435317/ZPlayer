package com.zheng.zplayer.local_video.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.local_video.listener.OnRecyclerViewItemClickListener;
import com.zheng.zplayer.local_video.listener.OnRecyclerViewItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


public class VideoBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener{
    public Activity mActivity;
    public List<ZMedia> mData;
    protected  OnRecyclerViewItemLongClickListener mLongClickListener;
    protected OnRecyclerViewItemClickListener mClickListener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(ArrayList<ZMedia> data) {
        if(mData!=null){}
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public void delete(int position) {
        synchronized (VideoGridAdapter.class) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public ZMedia getItem(int position) {
        if (mData!=null&&mData.size() >= position) {
            return mData.get(position);
        }
        return null;
    }

    public void replaceData(ArrayList<ZMedia> data) {
        clear();
        addAll(data);
    }
    @Override
    public boolean onLongClick(View v) {
        if(mLongClickListener!=null){
        }
        return true;
    }
    public void setItemLongClickListener(OnRecyclerViewItemLongClickListener click) {
        mLongClickListener = click;
    }
    public void setItemClickListener(OnRecyclerViewItemClickListener click) {
        mClickListener = click;
    }
}
