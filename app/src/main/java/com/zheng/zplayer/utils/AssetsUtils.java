package com.zheng.zplayer.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;


public class AssetsUtils {

    private InputStream mInputStream;
    private Bitmap mBitmap;
    private static AssetsUtils mAssetsUtils;
    private AssetsUtils(){}
    public static AssetsUtils getInstance(){
        if (mAssetsUtils==null){
            synchronized (AssetsUtils.class){
                if (mAssetsUtils==null){
                mAssetsUtils=new AssetsUtils();
                }
            }
        }
        return mAssetsUtils;
    }

    public Bitmap getAssetsBitmap(AssetManager assetManager, String fileName){
        try {
            mInputStream = assetManager.open(fileName);
            mBitmap = BitmapFactory.decodeStream(mInputStream);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}
