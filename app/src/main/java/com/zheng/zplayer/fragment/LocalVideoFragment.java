package com.zheng.zplayer.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zheng.zplayer.R;
import com.zheng.zplayer.ZPlayerConst;
import com.zheng.zplayer.been.ZMedia;
import com.zheng.zplayer.bus.RxBus;
import com.zheng.zplayer.bus.event.RefreshVideoEvent;
import com.zheng.zplayer.bus.event.ShowTypeEvent;
import com.zheng.zplayer.bus.event.UpdataEvent;
import com.zheng.zplayer.database.DbHelper;
import com.zheng.zplayer.database.FileBusiness;
import com.zheng.zplayer.local_video.adapter.VideoGridAdapter;
import com.zheng.zplayer.local_video.adapter.VideoListAdapter;
import com.zheng.zplayer.local_video.animation.Rotate3dAnimation;
import com.zheng.zplayer.local_video.dialog.FileOptionDialog;
import com.zheng.zplayer.local_video.listener.OnRecyclerViewItemClickListener;
import com.zheng.zplayer.local_video.listener.OnRecyclerViewItemLongClickListener;
import com.zheng.zplayer.local_video.listener.onFileOptionDialogOptionListener;
import com.zheng.zplayer.play.PlayActivity;
import com.zheng.zplayer.skin.SkinFragment;
import com.zheng.zplayer.utils.PreferenceUtils;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class LocalVideoFragment extends SkinFragment implements OnRecyclerViewItemLongClickListener, onFileOptionDialogOptionListener, View.OnClickListener, OnRecyclerViewItemClickListener {

    private VideoListAdapter mListAdapter;
    private ArrayList<ZMedia> mData;
    protected RecyclerView mListView;
    private RecyclerView mGridView;
    private VideoGridAdapter mGridAdapter;
    private int mShowType;
    private Activity mActivity;
    private FloatingActionButton mRefreshBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file, container, false);
        // ~~~~~~~~~ 绑定控件

        mListView = (RecyclerView) v.findViewById(android.R.id.list);
        mGridView = (RecyclerView) v.findViewById(R.id.grid);
        mRefreshBtn = (FloatingActionButton) v.findViewById(R.id.refresh_btn);

        mGridView.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,1);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取数据
        mData = FileBusiness.getAllFiles();
        mActivity = LocalVideoFragment.this.getActivity();
        //创建Adapter
        mListAdapter = new VideoListAdapter(getActivity(), mData);
        mGridAdapter = new VideoGridAdapter(getActivity(), mData);
        //绑定item的长按事件
        mGridAdapter.setItemLongClickListener(this);
        mListAdapter.setItemLongClickListener(this);
        mListAdapter.setItemClickListener(this);
        mRefreshBtn.setOnClickListener(this);
        //设置RecyclerView的Adapter
        mListView.setAdapter(mListAdapter);
        mGridView.setAdapter(mGridAdapter);
        //显示视图
        mShowType = PreferenceUtils.getInt(ZPlayerConst.SHOW_TYPE, ZPlayerConst.SHOWLIST);
        showVideoTable(mShowType);
        //切换视图动画
        mListView.setLayoutAnimation(getLayoutAnim());
        mGridView.setLayoutAnimation(getLayoutAnim());
        initSkinMode();
        //注册接收事件
        registerEvent();


    }

    /**
     * 注册接收事件
     */
    private void registerEvent() {
        //接收数据更新的事件
        RxBus.getInstance().toObserverable(UpdataEvent.class)
                .subscribe(new Consumer<UpdataEvent>() {
                    @Override
                    public void accept(UpdataEvent updataEvent) throws Exception {
                        ArrayList<ZMedia> data = updataEvent.getData();
                        mListAdapter.replaceData(data);
                        mGridAdapter.replaceData(data);
                    }
                });

        //接收视图切换按钮的事件
        RxBus.getInstance().toObserverable(ShowTypeEvent.class)
                .subscribe(new Consumer<ShowTypeEvent>() {
                    @Override
                    public void accept(ShowTypeEvent showTypeEvent) throws Exception {
                        mShowType = PreferenceUtils.getInt(ZPlayerConst.SHOW_TYPE, ZPlayerConst.SHOWLIST);
                        Resources resources = mActivity.getResources();
                        ((ImageView) showTypeEvent.getView())
                                .setImageBitmap(BitmapFactory.decodeResource(resources
                                        , mShowType == ZPlayerConst.SHOWLIST ? R.drawable.view_list : R.drawable.view_grid));
                        showVideoTable(mShowType);
                    }
                });
    }

    /**
     * 切换视图
     */
    private void showVideoTable(int showType) {
        if (showType == ZPlayerConst.SHOWLIST) {
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mListView.startLayoutAnimation();
        } else {
            mGridView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mGridView.startLayoutAnimation();
        }
    }


    /**
     * 删除文件
     */
    private void deleteFile(final ZMedia f,
                            final int position) {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.file_delete)
                .setMessage(getString(R.string.file_delete_confirm, f.title))
                .setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    File file = new File(f.path);
                                    if (file.canRead() && file.exists())
                                        file.delete();
                                    new DbHelper<ZMedia>().remove(f);
                                    mListAdapter.delete(position);
                                    mGridAdapter.delete(position);
                                } catch (Exception e) {
                                }
                            }
                        }).setPositiveButton("取消", null).
                create().show();
    }

    /**
     * 重命名文件
     */
    private void renameFile(final ZMedia f) {
        final EditText et = new EditText(getActivity());
        et.setText(f.title);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.file_rename)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setNegativeButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String name = et.getText().toString().trim();
                                if (name == null || name.trim().equals("")
                                        || name.trim().equals(f.title))
                                    return;

                                try {
                                    File fromFile = new File(f.path);
                                    File nf = new File(fromFile.getParent(),
                                            name.trim());
                                    if (nf.exists()) {
                                        Toast.makeText(getActivity(),
                                                R.string.file_rename_exists,
                                                Toast.LENGTH_LONG).show();
                                    } else if (fromFile.renameTo(nf)) {
                                        f.title = name;
                                        f.path = nf.getPath();
                                        FileBusiness.renameFile(f);

                                        new DbHelper<ZMedia>().update(f);
                                        mGridAdapter.notifyDataSetChanged();
                                        mListAdapter.notifyDataSetChanged();
                                    }
                                } catch (SecurityException se) {
                                    Toast.makeText(getActivity(),
                                            R.string.file_rename_failed,
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        }).setPositiveButton(android.R.string.no, null).show();
    }

    /**
     * 初始化动画控制器
     * @return
     */
    public  LayoutAnimationController getLayoutAnim(){
        LayoutAnimationController controller;
        Animation anim=new Rotate3dAnimation(90f,0f,0.5f,0.5f,0.5f,false);
        anim.setDuration(500);
        controller=new LayoutAnimationController(anim,0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }
    /**
     * 单击启动播放
     */
    @Override
    public void onItemClick(View view, int position) {
        final ZMedia f = mListAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.setData(Uri.parse(f.path));
        intent.putExtra("displayName", f.title);
        startActivity(intent);
    }

    /**
     * RecyclerView长按点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemLongClick(View view, int position) {
        FileOptionDialog fileOptionDialog = new FileOptionDialog(getActivity(), position);
        fileOptionDialog.setOptionListener(this);
        fileOptionDialog.show();
    }

    /**
     * FileOptionDialog Item点击事件监听
     *
     * @param v
     * @param position
     */
    @Override
    public void onOptionListener(View v, int position, int itemPosition) {
        switch (position) {
            case 0:
                renameFile(mListAdapter.getItem(itemPosition));
                break;
            case 1:
                deleteFile(mListAdapter.getItem(itemPosition), itemPosition);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        RxBus.getInstance().post(new RefreshVideoEvent());
    }


    @Override
    public void setNightMode() {
        mListView.setBackgroundColor(mActivity.getResources().getColor(R.color.background_night));
        mGridView.setBackgroundColor(mActivity.getResources().getColor(R.color.background_night));
        mListAdapter.setNightMode();
        mGridAdapter.setNightMode();
    }

    @Override
    public void setDayMode() {
        mListView.setBackgroundColor(mActivity.getResources().getColor(R.color.item_background_day));
        mGridView.setBackgroundColor(mActivity.getResources().getColor(R.color.item_background_day));
        mListAdapter.setDayMode();
        mGridAdapter.setDayMode();
    }
}
