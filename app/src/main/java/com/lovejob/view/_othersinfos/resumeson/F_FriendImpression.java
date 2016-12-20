package com.lovejob.view._othersinfos.resumeson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view._userinfo.myinformation.Aty_AddWriteTalk;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyGirdView;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/11/17.
 */
public class F_FriendImpression extends BaseFragment {
    View view;
    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.rl_biaoti)
    RelativeLayout rlBiaoti;
    @Bind(R.id.img_add_biaoqian)
    ImageView imgAddBiaoqian;
    @Bind(R.id.gv_friend)
    MyGirdView gvFriend;
    @Bind(R.id.tv_add_talk)
    TextView tvAddTalk;
    @Bind(R.id.tv_zhankai)
    TextView tvZhanKai;
    @Bind(R.id.lv_friendtalk)
    MyListView lvFriendtalk;
    private Activity context;
    String userPid;
    private FastAdapter<ThePerfectGirl.UserImpressionInfo> adapter;
    private FastAdapter<ThePerfectGirl.UserImpressionInfo> gvadapter;
    int zhankai = 1;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aty_friendtalk, null);
        ButterKnife.bind(this, view);
        context = getActivity();
        rlBiaoti.setVisibility(View.GONE);
        userPid = getActivity().getIntent().getStringExtra("userId");


        tvZhanKai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhankai == 1) {
                    zhankai = 2;
                    tvZhanKai.setText("收起");
                } else {
                    zhankai = 1;
                    tvZhanKai.setText("展开");
                }
                gvadapter.removeAll();
                addData();
            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addData();
        addLvData();
        initAdapter();

    }

    private void addLvData() {
        LoveJob.getFriendTalk("131", "1", userPid, new OnAllParameListener() {
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

            }
        });
    }

    private void addData() {
        LoveJob.getFriendTalk("131", "0", userPid, new OnAllParameListener() {
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

            }
        });

    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserImpressionInfo>(context, R.layout.item_lv_friendtalk) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                final FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_friendtalk, position);
                ((TextView) viewHolder.getView(R.id.img_friendtalk_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.img_friendtalk_company)).setText(getItem(position).getUserInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.img_friendtalk_position)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView) viewHolder.getView(R.id.content)).setText(getItem(position).getContent());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_friendtalk_userlogo));
                if (getItem(position).getState() == 1) {
                    ((ImageView) viewHolder.getView(R.id.img_dianzan)).setImageResource(R.mipmap.icon_good_on);
                }
                ((LinearLayout) viewHolder.getView(R.id.img_friendtalk_rl)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Others.class);
                        intent.putExtra("userId", adapter.getItem(position).getUserInfo().getUserId());
                        startActivity(intent);
                    }
                });
                ((ImageView) viewHolder.getView(R.id.img_dianzan)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoveJob.toDynGoodOrBad(adapter.getItem(position).getPid(), 1, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                if (thePerfectGirl.getData().getPoints() == 2) {
                                    ((ImageView) viewHolder.getView(R.id.img_dianzan)).setImageResource(R.mipmap.icon_friend);
                                } else {
                                    ((ImageView) viewHolder.getView(R.id.img_dianzan)).setImageResource(R.mipmap.icon_good_on);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String msg) {
                                Utils.showToast(context, msg);
                            }
                        });
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvFriendtalk.setAdapter(adapter);

        gvadapter = new FastAdapter<ThePerfectGirl.UserImpressionInfo>(context, R.layout.item_friend_talk) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_friend_talk, position);
                ((TextView) viewHolder.getView(R.id.tv_biaoqian_1)).setText(getItem(position).getContent());
                ((TextView) viewHolder.getView(R.id.tv_biaoqian_count1)).setText(getItem(position).getCount() + "".trim());
                if (getItem(position).getState() == 1) {
                    ((RelativeLayout) viewHolder.getView(R.id.rl_biaoqian_1)).setBackgroundDrawable(getResources().getDrawable(R.mipmap.yxxuanzhong));
                }

                return viewHolder.getConvertView();
            }
        };
        gvFriend.setAdapter(gvadapter);
        gvFriend.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoveJob.toDynGoodOrBad(gvadapter.getItem(position).getPid(), 1, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        gvadapter.removeAll();
                        addData();
                        gvadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
            }
        });

    }


    @Override
    public void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.img_add_biaoqian, R.id.tv_add_talk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add_biaoqian:
                Intent intent = new Intent(context, Aty_AddWriteTalk.class);
                intent.putExtra("userpid", userPid);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.tv_add_talk:
                Intent intent1 = new Intent(context, Aty_AddWriteTalk.class);
                intent1.putExtra("userpid", userPid);
                intent1.putExtra("type", "1");
                startActivity(intent1);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
