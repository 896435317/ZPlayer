package com.zheng.zplayer.bus.event;

import com.zheng.zplayer.been.ZMedia;

import java.util.ArrayList;


public class UpdataEvent {
    private  ArrayList<ZMedia> mData;

    public UpdataEvent(ArrayList<ZMedia> data) {
        mData = data;
    }

    public ArrayList<ZMedia> getData() {
        return mData;
    }

    public void setData(ArrayList<ZMedia> data) {
        mData = data;
    }
}
