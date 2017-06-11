package com.zheng.zplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.utils.PreferenceUtils;


public class SettingsItemView extends CardView {
    private final Context mContext;
    private String mText;
    private int mTextColor;
    private float mTextSize;
    private int mBackgroundColor;
    private TextView mTextView;

    public SettingsItemView(Context context) {
        this(context, null);
    }

    public SettingsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttr(context, attrs);
        initColor();
        initView(context);
        addView(mTextView);
        this.setFocusable(true);
        this.setClickable(true);
        this.setBackgroundColor(mBackgroundColor);
    }

    private void initColor() {
        int mode = PreferenceUtils.getInt(ZPlayerConst.SKIN_MODE, ZPlayerConst.DAY_MODE);
        if(mode == ZPlayerConst.DAY_MODE){
            mBackgroundColor = mContext.getResources().getColor(R.color.item_background_day);
            mTextColor = mContext.getResources().getColor(R.color.text_color_day);
        }else{
            mBackgroundColor = mContext.getResources().getColor(R.color.item_background_night);
            mTextColor = mContext.getResources().getColor(R.color.text_color_night);
        }
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemView);
        mTextColor = typedArray.getColor(R.styleable.SettingsItemView_textColor,
                context.getResources().getColor(R.color.text_color_day));
        mText = typedArray.getString(R.styleable.SettingsItemView_text);
        mTextSize = typedArray.getDimension(R.styleable.SettingsItemView_textSize, 10);
        mBackgroundColor = typedArray.getColor(R.styleable.SettingsItemView_backgroudColor,
                context.getResources().getColor(R.color.item_background_day));
    }

    private void initView(Context context) {
        this.setCardBackgroundColor(mBackgroundColor);
        mTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.setting_item, null);
        mTextView.setText(mText);
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(mTextSize);
    }
    public void setDayMode(){
        initColor();
        this.setCardBackgroundColor(mBackgroundColor);
        mTextView.setTextColor(mTextColor);
    }
    public void setNightMode(){
        initColor();
        this.setCardBackgroundColor(mBackgroundColor);
        mTextView.setTextColor(mTextColor);
    }
}
