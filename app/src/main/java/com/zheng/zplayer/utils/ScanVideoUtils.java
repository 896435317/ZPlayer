package com.zheng.zplayer.utils;

import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.application.ZPlayerApplication;
import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.bus.RxBus;
import com.zheng.zplayer.bus.event.UpdataEvent;
import com.zheng.zplayer.database.FileBusiness;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ScanVideoUtils {
    private ArrayList<File> files = new ArrayList<>();
    public ScanVideoUtils(){

    }
    public void eachAllMedias(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : f.listFiles()) {
                    if (file.isDirectory()) {
                        eachAllMedias(file);
                    } else if (file.exists() && file.canRead() && FileUtils.isVideo(file)) {
                        this.files.add(file);
                    }
                }
            }
        }
    }
    public  void begin(){
        Observable<ArrayList<ZMedia>> mScan  = Observable.create(new ObservableOnSubscribe<ArrayList<ZMedia>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ZMedia>> e) throws Exception {
                // ~~~ 遍历文件夹
                ArrayList<String> path = (ArrayList<String>) PreferenceUtils.getBean(ZPlayerConst.VIDEO_PATH);
                if (path!=null && path.size() > 0) {
                    for (String p : path) {
                        eachAllMedias(new File(p));
                    }
                }
                // ~~~ 提取缩略图、视频尺寸等。
                ArrayList<ZMedia> ZMedias = FileBusiness.batchBuildThumbnail(ZPlayerApplication.getContext(), files);
                // ~~~ 入库
                FileBusiness.batchInsertFiles(ZMedias);
                // ~~~ 查询数据
                e.onNext(FileBusiness.getAllFiles());
            }
        });
        mScan.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<ZMedia>>() {
                    @Override
                    public void accept(ArrayList<ZMedia> zMedias) throws Exception {
                        RxBus.getInstance().post(new UpdataEvent(zMedias));
                    }
                });
    }
}
