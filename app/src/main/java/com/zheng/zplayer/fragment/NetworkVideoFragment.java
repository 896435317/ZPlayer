package com.zheng.zplayer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zheng.zplayer.R;
import com.zheng.zplayer.application.ZPlayerApplication;
import com.zheng.zplayer.play.PlayActivity;
import com.zheng.zplayer.skin.SkinFragment;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:31
 * Mail: specialcyci@gmail.com
 */
public class NetworkVideoFragment extends SkinFragment implements View.OnClickListener {

    private EditText mVideo_url;
    private CardView mButton;
    private View mView;
    private TextView mNet_text;
    private TextView mNet_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.net_video, container, false);
        mNet_text = (TextView) mView.findViewById(R.id.net_text);
        mNet_ok = (TextView) mView.findViewById(R.id.net_ok);
        mVideo_url = (EditText) mView.findViewById(R.id.video_url);
        mButton = (CardView) mView.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        initSkinMode();
        return mView;
    }

    @Override
    public void onClick(View v) {
        Editable text = mVideo_url.getText();
        String url = text.toString();
        if(url!=null){
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void setNightMode() {
        mView.setBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_night));
        mButton.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_night));
        mNet_ok.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));
        mNet_text.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));
        mVideo_url.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_night));
    }

    @Override
    public void setDayMode() {
        mView.setBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_day));
        mButton.setCardBackgroundColor(ZPlayerApplication.getContext()
                .getResources().getColor(R.color.background_day));
        mNet_ok.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));
        mNet_text.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));
        mVideo_url.setTextColor(ZPlayerApplication.getContext().getResources()
                .getColor(R.color.text_color_day));
    }
}
