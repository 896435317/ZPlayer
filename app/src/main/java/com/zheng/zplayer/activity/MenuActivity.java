package com.zheng.zplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.bus.RxBus;
import com.zheng.zplayer.bus.event.ChangeBackgroundEvent;
import com.zheng.zplayer.bus.event.RefreshVideoEvent;
import com.zheng.zplayer.bus.event.ShowTypeEvent;
import com.zheng.zplayer.bus.event.SkinChangeEvent;
import com.zheng.zplayer.fragment.LocalVideoFragment;
import com.zheng.zplayer.fragment.NetworkVideoFragment;
import com.zheng.zplayer.fragment.SettingsFragment;
import com.zheng.zplayer.skin.SkinActivity;
import com.zheng.zplayer.utils.PreferenceUtils;
import com.zheng.zplayer.utils.ScanVideoUtils;
import com.zheng.zplayer.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;


public class MenuActivity extends SkinActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private ResideMenuItem itemVideo;
    private ResideMenuItem itemNetwork;
    private ResideMenuItem itemSkinMode;
    private ResideMenuItem itemSettings;
    private ImageView mLeftBt;
    private int mShowType;
    private FrameLayout mMain_bar;
    private TextView mMain_title;
    private ImageView mMain_iv;
    private boolean isExit;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findView();
        setUpMenu();
        checkPermissions();
        registerEvent();


    }

    private void registerEvent() {
        RxBus.getInstance().toObserverable(RefreshVideoEvent.class).subscribe(new Consumer<RefreshVideoEvent>() {
            @Override
            public void accept(RefreshVideoEvent refreshVideoEvent) throws Exception {
                new ScanVideoUtils().begin();
            }
        });
        RxBus.getInstance().toObserverable(ChangeBackgroundEvent.class).subscribe(new Consumer<ChangeBackgroundEvent>() {
            @Override
            public void accept(ChangeBackgroundEvent changeBackgroundEvent) throws Exception {
                setBackground();
            }


        });
    }

    private void setBackground() {
        String path = PreferenceUtils.getString(ZPlayerConst.BACKGROUND_PATH, null);
        if (!StringUtils.isEmpty(path)) {
            resideMenu.setBackgroundByPath(path);
        } else {
           resideMenu.setBackground(R.drawable.background);
        }
    }

    /**
     * 动态权限申请
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        setBackground();
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.77f);

        // create menu items;
        itemVideo = new ResideMenuItem(this, R.drawable.video, "Video");
        itemNetwork = new ResideMenuItem(this, R.drawable.network, "Network");
        itemSettings = new ResideMenuItem(this, R.drawable.setting, "Settings");
        itemSkinMode = new ResideMenuItem(this, R.drawable.night_mode, "Night");


        itemVideo.setOnClickListener(this);
        itemNetwork.setOnClickListener(this);
        itemSkinMode.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemVideo, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNetwork, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSkinMode, ResideMenu.DIRECTION_LEFT);


        mLeftBt = (ImageView) findViewById(R.id.title_bar_right_menu);
        mShowType = PreferenceUtils.getInt(ZPlayerConst.SHOW_TYPE, 0);
        mLeftBt.setImageBitmap(BitmapFactory.decodeResource(
                MenuActivity.this.getResources()
                , mShowType == ZPlayerConst.SHOWLIST ? R.drawable.view_list : R.drawable.view_grid));
        mLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowType = PreferenceUtils.getInt(ZPlayerConst.SHOW_TYPE, 0);
                if (mShowType == 0) {
                    mShowType = 1;
                } else {
                    mShowType = 0;
                }
                PreferenceUtils.putInt(ZPlayerConst.SHOW_TYPE, mShowType);
                RxBus.getInstance().post(new ShowTypeEvent(view));

            }
        });
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        iniSkinMode();
    }

    private void findView() {
        mMain_bar = (FrameLayout) findViewById(R.id.main_bar);
        mMain_title = (TextView) findViewById(R.id.main_title);
        mMain_iv = (ImageView) findViewById(R.id.main_iv);
    }


    private void iniSkinMode() {
        int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
        if (mode == ZPlayerConst.DAY_MODE) {
            setDayMode();
        } else {
            setNightMode();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemVideo) {
            changeFragment(new LocalVideoFragment());
        } else if (view == itemNetwork) {
            changeFragment(new NetworkVideoFragment());
        } else if (view == itemSkinMode) {
            int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
            RxBus.getInstance().post(new SkinChangeEvent((mode == ZPlayerConst.DAY_MODE) ?
                    ZPlayerConst.NIGHT_MODE : ZPlayerConst.DAY_MODE));
        } else if (view == itemSettings) {
            changeFragment(new SettingsFragment());

        }

        resideMenu.closeMenu();
    }


    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        changeFragment(new LocalVideoFragment());
    }


    public void setDayMode() {
        mMain_bar.setBackgroundColor(getResources().getColor(R.color.background_day));
        mMain_title.setTextColor(getResources().getColor(R.color.main_title_day));
        mMain_iv.setBackgroundColor(getResources().getColor(R.color.main_cut_off_rule_day));
        resideMenu.setSkinMode(ZPlayerConst.DAY_MODE);
        itemSkinMode.setIcon(R.drawable.night_mode);
        itemSkinMode.setTitle("Night");

    }

    public void setNightMode() {
        mMain_bar.setBackgroundColor(getResources().getColor(R.color.background_night));
        mMain_title.setTextColor(getResources().getColor(R.color.main_title_night));
        mMain_iv.setBackgroundColor(getResources().getColor(R.color.main_cut_off_rule_night));
        resideMenu.setSkinMode(ZPlayerConst.NIGHT_MODE);
        itemSkinMode.setIcon(R.drawable.day_mode);
        itemSkinMode.setTitle("Day");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            exitByDoubleClick();
        }
        return false;
    }

    private void exitByDoubleClick() {
        Timer tExit=null;
        if(!isExit){
            isExit=true;
            Toast.makeText(MenuActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            finish();
            System.exit(0);
        }
    }
}

