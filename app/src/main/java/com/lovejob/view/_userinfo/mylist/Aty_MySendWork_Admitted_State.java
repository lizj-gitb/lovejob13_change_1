package com.lovejob.view._userinfo.mylist;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.MyApplication;
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
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MySendWork_Admitted_State extends BaseActivity {
    @Bind(R.id.img_mysendallworkbusiness_back)
    ImageView imgMysendallworkbusinessBack;
    @Bind(R.id.lv_aty_workbusiness_list)
    MyListView lvAtyWorkbusinessList;
    @Bind(R.id.lv_aty_workbusiness_sv)
    PullToRefreshScrollView svAtyWorkbusinessSv;
    private String workId;
    private FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
    private Activity context;
    private Call call_toAdmitted;
    int page = 1;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mysendallwork_state);
        ButterKnife.bind(this);
        context = this;
          /*
        获取上个页面传过来的workid
         */
        workId = getIntent().getStringExtra("workId");
        svAtyWorkbusinessSv.setMode(PullToRefreshBase.Mode.BOTH);
        addScroViewListener();
        addData();
        initAdapter();

    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserInfoDTO>(context, R.layout.aty_published_work_admit) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.aty_published_work_admit, position);
                CircleImageView imgTobeconfirmIcon = (CircleImageView) viewHolder.getView(R.id.img_tobeconfirm_icon);
                TextView tvTobeconfirmName = (TextView) viewHolder.getView(R.id.tv_tobeconfirm_name);
                TextView tvTobeconfirmCraft = (TextView) viewHolder.getView(R.id.tv_tobeconfirm_craft);
                TextView tvCommpl = (TextView) viewHolder.getView(R.id.tv_commpl);
                TextView tvAtyPublishWorkstate = (TextView) viewHolder.getView(R.id.tv_aty_publish_workstate);
                ImageView imgAtyPublishedGetthisperson = (ImageView) viewHolder.getView(R.id.img_aty_published_getthisperson);
                ImageView imgAtyPublishedChat = (ImageView) viewHolder.getView(R.id.img_aty_published_chat);
                ImageView imgAtyPublishedCall = (ImageView) viewHolder.getView(R.id.img_aty_published_call);
                setOnclickListener(position, imgAtyPublishedGetthisperson);
                setOnclickListener(position, imgAtyPublishedChat);
                setOnclickListener(position, imgAtyPublishedCall);
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getPortraitId() + "".trim()).into(imgTobeconfirmIcon);
                tvTobeconfirmName.setText(getItem(position).getRealName() + "".trim());
                tvTobeconfirmCraft.setText(getItem(position).getPosition() + "".trim());
                tvCommpl.setText(getItem(position).getCompany() + "".trim());
                switch (getItem(position).getApplaySate()) {
                    case 0:
                        //待录取
                        tvAtyPublishWorkstate.setText("待录取");
                        break;
                    case 1:
                        //已录用
                        tvAtyPublishWorkstate.setText("已录用");
                        break;
                    case 2:

                        break;
                }
                return viewHolder.getConvertView();
            }

            private void setOnclickListener(final int position, View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.img_aty_published_getthisperson:
//                                Utils.showToast(Aty_MySendWork_Admitted_State.this, "申请", false);
                                /**
                                 * 录用
                                 */
                                dialog = Utils.showProgressDliago(context, "请稍后");
                                getThisPerson(position);
                                break;
                            case R.id.img_aty_published_chat:
                                if (RongIM.getInstance() != null && getItem(position).getUserId() != null && !TextUtils.isEmpty(getItem(position).getUserId())) {
                                    RongIM.getInstance().startPrivateChat(context, getItem(position).getUserId(), getItem(position).getRealName());
                                } else {
                                    Utils.showToast(context, "请重新登录后再试");
                                    V.e("用户Id未获取成功");
                                }
                                break;
                            case R.id.img_aty_published_call:

                                if (TextUtils.isEmpty(getItem(position).getPhoneNumber())) {
                                    Utils.showToast(context, "请稍后再试");
                                    return;
                                }
                                Intent intent_phone = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + getItem(position).getPhoneNumber());
                                intent_phone.setData(data);
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    V.e("用户权限缺失");
                                    return;
                                }
                                startActivity(intent_phone);
                                break;

                        }
                    }

                    private void getThisPerson(final int position) {
                        call_toAdmitted = LoveJob.toAdmitted(getItem(position).getUserId(), workId, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                dialog.dismiss();
                                adapter.remove(position);
                                adapter.notifyDataSetChanged();
                                Utils.showToast(context, "录用成功");
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        });
                    }
                };
                view.setOnClickListener(onClickListener);
            }
        };
        lvAtyWorkbusinessList.setAdapter(adapter);
    }

    private void addData() {
        LoveJob.getWorkList2("48", workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() == null || thePerfectGirl.getData().getUserInfoDTOs().size() == 0) {
                    Utils.showToast(context, "没有更多数据");
                    return;
                }
                V.d("data:" + (thePerfectGirl == null) + "");
                for (int i = 0; i < thePerfectGirl.getData().getUserInfoDTOs().size(); i++) {
                    adapter.addItem(thePerfectGirl.getData().getUserInfoDTOs().get(i));
                    V.d("+1");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {

            }
        });

    }

    private void addScroViewListener() {
        svAtyWorkbusinessSv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                adapter.removeAll();
                addData();
                Utils.showToast(context, "刷新成功");
                svAtyWorkbusinessSv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                Utils.showToast(context, "加载成功");
                svAtyWorkbusinessSv.onRefreshComplete();

            }
        });
    }

    @Override
    public void onResume_() {


    }

    @Override
    public void onDestroy_() {
        if (call_toAdmitted != null && call_toAdmitted.isCanceled())
            call_toAdmitted.cancel();
    }


    @OnClick(R.id.img_mysendallworkbusiness_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
