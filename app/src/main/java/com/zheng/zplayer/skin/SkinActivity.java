package com.zheng.zplayer.skin;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.bus.RxBus;
import com.zheng.zplayer.bus.event.SkinChangeEvent;
import com.zheng.zplayer.utils.PreferenceUtils;

import io.reactivex.functions.Consumer;


public abstract class SkinActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().toObserverable(SkinChangeEvent.class).subscribe(new Consumer<SkinChangeEvent>() {
            @Override
            public void accept(SkinChangeEvent skinhangeEvent) throws Exception {
                int mode = skinhangeEvent.getMode();
                PreferenceUtils.putInt(ZPlayerConst.SKIN_MODE, mode);
                switch (mode) {
                    case ZPlayerConst.DAY_MODE:
                        setDayMode();
                        break;
                    case ZPlayerConst.NIGHT_MODE:
                        setNightMode();
                        break;
                }
            }
        });
    }

    public abstract void setDayMode();

    public abstract void setNightMode();
}
