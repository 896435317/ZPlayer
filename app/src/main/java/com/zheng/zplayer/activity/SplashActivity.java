package com.zheng.zplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zheng.zplayer.R;
import com.zheng.zplayer.utils.AssetsUtils;

public class SplashActivity extends Activity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initAnima();

    }

    private void initAnima() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,MenuActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        }, 2000);
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mImageView = (ImageView) findViewById(R.id.iv_splash);
        Bitmap bitmap = getBitmap();
        mImageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmap() {
        AssetManager assetManager = getAssets();
        String fileName = "1.jpg";
        Bitmap bitmap = AssetsUtils.getInstance().getAssetsBitmap(assetManager, fileName);
        return bitmap;
    }
}
