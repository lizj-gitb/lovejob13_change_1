package com.lovejob.view._userinfo.myinformation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_FriendTalk extends BaseActivity {

    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.img_add_biaoqian)
    ImageView imgAddBiaoqian;
    @Bind(R.id.gv_friend)
    GridView gvFriend;
    @Bind(R.id.lv_friendtalk)
    MyListView lvFriendtalk;
    @Bind(R.id.rl_biaoti)
    RelativeLayout rlBiaoti;
    @Bind(R.id.tv_add_talk)
    TextView tvAddTalk;
    @Bind(R.id.tv_zhankai)
    TextView tvZhankai;
    private FastAdapter<ThePerfectGirl.UserImpressionInfo> adapter;
    private FastAdapter<ThePerfectGirl.UserImpressionInfo> gvadapter;
    private Call call_getFriendTalk;
    int zhankai = 1;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_friendtalk);
        ButterKnife.bind(this);
        tvZhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhankai == 1) {
                    zhankai = 2;
                    tvZhankai.setText("收起");
                } else {
                    zhankai = 1;
                    tvZhankai.setText("展开");
                }
                gvadapter.removeAll();
                addData();
            }

        });

    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserImpressionInfo>(context, R.layout.item_lv_friendtalk) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_friendtalk, position);
                ((TextView) viewHolder.getView(R.id.img_friendtalk_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.img_friendtalk_company)).setText(getItem(position).getUserInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.img_friendtalk_position)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView) viewHolder.getView(R.id.content)).setText(getItem(position).getContent());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_friendtalk_userlogo));
                setItemOnClickListener(viewHolder.getConvertView(), position);
                return viewHolder.getConvertView();
            }

            private void setItemOnClickListener(View convertView, int position) {
                ImageView goodimg = (ImageView) convertView.findViewById(R.id.img_dianzan);
                goodimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.showToast(context, "点赞");
                    }
                });
            }


        };
        lvFriendtalk.setAdapter(adapter);
        gvadapter = new FastAdapter<ThePerfectGirl.UserImpressionInfo>(context, R.layout.item_friend_talk) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_friend_talk, position);
                ((TextView) viewHolder.getView(R.id.tv_biaoqian_1)).setText(getItem(position).getContent());
                ((TextView) viewHolder.getView(R.id.tv_biaoqian_count1)).setText(getItem(position).getCount() + "".trim());
                return viewHolder.getConvertView();
            }
        };
        gvFriend.setAdapter(gvadapter);
        //取消GridView中Item选中时默认的背景色
        gvFriend.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void addData() {
        LoveJob.getFriendTalk("131", "0", null, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getUserImpressionInfoList() != null) {
                    if (zhankai == 1) {
                        if (thePerfectGirl.getData().getUserImpressionInfoList().size() > 4) {
                            for (int i = 0; i < 4; i++) {
                                gvadapter.addItem(thePerfectGirl.getData().getUserImpressionInfoList().get(i));
                            }
                            gvadapter.notifyDataSetChanged();
                        } else {
                            for (int i = 0; i < thePerfectGirl.getData().getUserImpressionInfoList().size(); i++) {
                                gvadapter.addItem(thePerfectGirl.getData().getUserImpressionInfoList().get(i));
                            }
                            gvadapter.notifyDataSetChanged();
                        }
                    } else {
                        for (int i = 0; i < thePerfectGirl.getData().getUserImpressionInfoList().size(); i++) {
                            gvadapter.addItem(thePerfectGirl.getData().getUserImpressionInfoList().get(i));
                        }
                        gvadapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });


    }

    @Override
    public void onResume_() {
        addData();
        addLvData();
        initAdapter();
    }

    private void addLvData() {
        call_getFriendTalk = LoveJob.getFriendTalk("131", "1", null, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getUserImpressionInfoList() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getUserImpressionInfoList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getUserImpressionInfoList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onDestroy_() {
        if (call_getFriendTalk != null && !call_getFriendTalk.isCanceled())
            call_getFriendTalk.cancel();
    }


    @OnClick({R.id.img_history_back, R.id.img_add_biaoqian, R.id.tv_add_talk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_history_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.img_add_biaoqian:
                Utils.showToast(context, "您自己不能添加");
//                startActivity(new Intent(context,Aty_AddWriteTalk.class));
                break;
            case R.id.tv_add_talk:
                Utils.showToast(context, "您自己不能添加");
                break;
        }
    }


}
