package com.zheng.zplayer.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.util.ArrayMap;
import android.util.Log;

import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.utils.PreferenceUtils;
import com.zheng.zplayer.utils.VideoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public final class FileBusiness {

    private static final String TAG = "FileBusiness";

    /**
     * 批量提取视频的缩略图已经视频的宽高
     */
    public static ArrayList<ZMedia> batchBuildThumbnail(final Context ctx, final ArrayList<File> files) {
        ArrayList<ZMedia> result = new ArrayList<>();

        for (File f : files) {
            ZMedia pf = new ZMedia(f);
            try {
                if (f.exists() && f.canRead()) {
                    //取出视频的一帧图像
                    Bitmap bitmap = VideoUtils.getVideoThumbnail(f.getPath());
                    if (bitmap != null) {
                        pf.width = bitmap.getWidth();
                        pf.height = bitmap.getHeight();
                        //缩略图
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, pf.width / pf.height * 256, 256, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                        if (bitmap != null) {
                            File thum;
                            if (Environment.getExternalStorageDirectory() != null) {

                                String paths = PreferenceUtils.getString(ZPlayerConst.CACHE_PATH,
                                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZPlayer");
                                File dir = new File(paths);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                thum = new File(dir, f.getName() + ".jpg");
                            } else {
                                thum = new File(f.getParent(), f.getName() + ".jpg");
                            }
                            String absolutePath = thum.getAbsolutePath();
                            pf.thumb_path = absolutePath;
                            FileOutputStream iStream = new FileOutputStream(thum);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, iStream);
                            iStream.close();
                        }
                    }
                    if (bitmap != null)
                        bitmap.recycle();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                continue;
            } finally {
                result.add(pf);
            }
        }

        return result;
    }

    /**
     * 批量插入数据
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void batchInsertFiles(ArrayList<ZMedia> files) {
        DbHelper<ZMedia> dbHelper = new DbHelper<>();
        dbHelper.clear(ZMedia.class);
        for (ZMedia file : files) {
            ArrayMap<String, Object> where = new ArrayMap<>();
            where.put(ZPlayerConst.VIDEO_PATH, file.path);
            if (!dbHelper.exists(file, where)) {
                dbHelper.create(file);
            }
        }
    }

    /**
     * 获取所有数据
     *
     * @return 所有视频信息集合
     */
    public static ArrayList<ZMedia> getAllFiles() {
        DbHelper<ZMedia> dbHelper = new DbHelper<>();
        return (ArrayList<ZMedia>) (dbHelper.queryForAll(ZMedia.class));
    }

    public static void renameFile(ZMedia p) {
        DbHelper<ZMedia> dbHelper = new DbHelper<>();

        dbHelper.update(p);


    }

}
