package com.zheng.zplayer.bus.event;

import android.view.View;


public class ShowTypeEvent {
    private View mView;

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public ShowTypeEvent(View v){
        mView = v;
    }
}
