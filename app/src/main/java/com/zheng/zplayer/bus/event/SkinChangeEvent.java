package com.zheng.zplayer.bus.event;


public class SkinChangeEvent {
    private int mode;
    public SkinChangeEvent(int mode){
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
