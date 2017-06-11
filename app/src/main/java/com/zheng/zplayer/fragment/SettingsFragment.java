package com.zheng.zplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.application.ZPlayerApplication;
import com.zheng.zplayer.utils.ScanVideoUtils;
import com.zheng.zplayer.bus.RxBus;
import com.zheng.zplayer.bus.event.ChangeBackgroundEvent;
import com.zheng.zplayer.skin.SkinFragment;
import com.zheng.zplayer.utils.PreferenceUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:28
 * Mail: specialcyci@gmail.com
 */
public class SettingsFragment extends SkinFragment implements View.OnClickListener{

    private CardView mScan_view;
    private CardView mCache_view;
    private FrameLayout mSf_fl;
    private CardView mBackground;
    private TextView mScan_tv;
    private TextView mCache_tv;
    private TextView mBackground_tv;
    private FilePickerDialog mDialog;
    private DialogProperties mProperties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = initView(inflater, container);
        mCache_view.setOnClickListener(this);
        mScan_view.setOnClickListener(this);
        mBackground.setOnClickListener(this);
        return view;
}



    private void initDialogProperties() {
        mProperties = new DialogProperties();
        mProperties.root = new File(DialogConfigs.DEFAULT_DIR);
        mProperties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        mProperties.offset = new File(DialogConfigs.DEFAULT_DIR);
        mProperties.extensions = null;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.settings, container, false);
        mScan_view = (CardView) view.findViewById(R.id.video_dir);
        mCache_view = (CardView) view.findViewById(R.id.cache_dir);
        mBackground = (CardView) view.findViewById(R.id.background_select);

        mSf_fl = (ScrollView) view.findViewById(R.id.sf_sv);
        mScan_tv = (TextView) mScan_view.findViewById(R.id.scan_settings_tv);
        mCache_tv = (TextView) mCache_view.findViewById(R.id.cache_settings_tv);

        mBackground_tv = (TextView) mBackground.findViewById(R.id.background_select_settings_tv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialogProperties();
        initSkinMode();
    }

    @Override
    public void onClick(View v) {

        if(v== mScan_view){
            showScanPathSelectDialog(mProperties);
        }else if (v== mCache_view){
            showCachePathSelectDialog(mProperties);
        }else if(v==mBackground){
            showSlectBackgroundDialog(mProperties);
        }

    }

    private void showSlectBackgroundDialog(DialogProperties properties) {
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        mDialog = new FilePickerDialog(getActivity(),properties);
        mDialog.setTitle("Select a Picture for Background");
        mDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                ArrayList<String> paths = new ArrayList<>();
                for (String file : files) {
                    paths.add(file);
                }
                PreferenceUtils.remove(ZPlayerConst.BACKGROUND_PATH);
                if(paths.size()>0){
                    PreferenceUtils.putString(ZPlayerConst.BACKGROUND_PATH, paths.get(0));
                    RxBus.getInstance().post(new ChangeBackgroundEvent());
                }

            }
        });
        mDialog.show();
    }

    private void showCachePathSelectDialog(DialogProperties properties) {
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        mDialog = new FilePickerDialog(getActivity(),properties);
        mDialog.setTitle("Select a directory for cache");
        mDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                ArrayList<String> paths = new ArrayList<>();
                for (String file : files) {
                    paths.add(file);
                }
                PreferenceUtils.remove(ZPlayerConst.CACHE_PATH);
                PreferenceUtils.putString(ZPlayerConst.CACHE_PATH, paths.get(0));
            }
        });
        mDialog.show();
    }

    private void showScanPathSelectDialog(DialogProperties properties) {
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        mDialog = new FilePickerDialog(getActivity(), properties);
        mDialog.setTitle("Select a directory for loading video");
        mDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                ArrayList<String> paths = new ArrayList<>();
                for (String file : files) {
                    paths.add(file);
                }
                PreferenceUtils.remove(ZPlayerConst.VIDEO_PATH);
                PreferenceUtils.putBean(ZPlayerConst.VIDEO_PATH, paths);
                new ScanVideoUtils().begin();
    }
        });
        mDialog.show();
    }


    @Override
    public void setNightMode() {
        mScan_view.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_night));
        mCache_view.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_night));
        mBackground.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_night));

        mScan_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));
        mCache_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));
        mBackground_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));


        mSf_fl.setBackgroundColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.background_night));
    }

    @Override
    public void setDayMode() {
        mScan_view.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_day));
        mCache_view.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_day));
        mBackground.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_day));


        mScan_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));
        mCache_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));
        mBackground_tv.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));

        mSf_fl.setBackgroundColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.background_day));
    }



}
