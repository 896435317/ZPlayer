package com.zheng.zplayer.local_video.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.application.ZPlayerApplication;
import com.zheng.zplayer.local_video.listener.onFileOptionDialogOptionListener;
import com.zheng.zplayer.utils.PreferenceUtils;


public class FileOptionDialog extends Dialog implements View.OnClickListener {
    private int mItemPosition;
    protected Context mContext;
    onFileOptionDialogOptionListener mListener;
    private TextView mRename;
    private TextView mDelete;
    private View mBackground;

    public FileOptionDialog(Context context, int position) {
        super(context);
        mItemPosition = position;
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initColor();
        //设置dialog大小
        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity) mContext).getWindowManager();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);//设置对话框位置
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(params);
    }


    protected void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_file_option, null);
        setContentView(view);
        //绑定控件
        mRename = (TextView) view.findViewById(R.id.rename);
        mDelete = (TextView) view.findViewById(R.id.delete);
        mBackground = view.findViewById(R.id.background_dialog);
        //绑定点击事件
        mRename.setOnClickListener(this);
        mDelete.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.rename:
                    mListener.onOptionListener(v, 0, mItemPosition);
                    break;
                case R.id.delete:
                    mListener.onOptionListener(v, 1, mItemPosition);
                    break;
            }
        }
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOptionListener(onFileOptionDialogOptionListener listener) {
        mListener = listener;
    }
    private void initColor() {
        int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
        switch (mode) {
            case ZPlayerConst.DAY_MODE:
                mBackground.setBackgroundColor(ZPlayerApplication.getContext().getResources().getColor(R.color.background_day));
                mDelete.setTextColor(ZPlayerApplication.getContext().getResources().getColor(R.color.text_color_day));
                mRename.setTextColor(ZPlayerApplication.getContext().getResources().getColor(R.color.text_color_day));
                break;
            case ZPlayerConst.NIGHT_MODE:
                mBackground.setBackgroundColor(ZPlayerApplication.getContext().getResources().getColor(R.color.background_night));
                mDelete.setTextColor(ZPlayerApplication.getContext().getResources().getColor(R.color.text_color_night));
                mRename.setTextColor(ZPlayerApplication.getContext().getResources().getColor(R.color.text_color_night));
                break;
        }
    }
}
