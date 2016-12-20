package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MySendWork_ToComm extends BaseActivity {
    @Bind(R.id.img_tobecomm_back)
    ImageView imgTobecommBack;
    @Bind(R.id.lv_tobecomm_list)
    PullToRefreshListView lvTobecommList;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter;
    private Activity context;
    int page = 1;
    private Call call_getWorkList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_tobecomm);
        ButterKnife.bind(this);
        context = this;
        initAdapater();
        addData();
        setRefreshListener();
    }

    private void setRefreshListener() {
        lvTobecommList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.removeAll();
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                lvTobecommList.onRefreshComplete();
            }
        });
    }

    private void addData() {
        call_getWorkList = LoveJob.getWorkList("47", String.valueOf(page), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvTobecommList.onRefreshComplete();
                if (thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
                lvTobecommList.onRefreshComplete();
            }
        });
    }

    private void initAdapater() {
        adapter = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_lv_tobencomm) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_tobencomm, position);
                TextView tvTobecommTime = (TextView) viewHolder.getView(R.id.tv_tobecomm_time);
                TextView tvTobecommTitle = (TextView) viewHolder.getView(R.id.tv_tobecomm_title);
                TextView tvTobecommName = (TextView) viewHolder.getView(R.id.tv_tobecomm_name);
                TextView tvTobecommPosetion = (TextView) viewHolder.getView(R.id.tv_tobecomm_posetion);
                TextView tvTobecommCommpl = (TextView) viewHolder.getView(R.id.tv_tobecomm_commpl);
                CircleImageView imgTobecommLogo = (CircleImageView) viewHolder.getView(R.id.img_tobecomm_logo);
                TextView tvTobecommPrice = (TextView) viewHolder.getView(R.id.tv_tobecomm_price);
                TextView tvTobecommCommit = (TextView) viewHolder.getView(R.id.tv_tobecomm_commit);

                tvTobecommTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(getItem(position).getReleaseDate())));
                tvTobecommTitle.setText(getItem(position).getTitle());
                tvTobecommName.setText(getItem(position).getReleaseInfo().getRealName());
                tvTobecommPosetion.setText(getItem(position).getReleaseInfo().getPosition() + "");
                tvTobecommCommpl.setText(getItem(position).getReleaseInfo().getCompany() + "");
                tvTobecommPrice.setText(getItem(position).getSalary() + "/" + getItem(position).getPaymentDec());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getReleaseInfo().getPortraitId()).into(imgTobecommLogo);
                tvTobecommCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, Aty_CommView.class);
                        intent.putExtra("workPid", getItem(position).getPid());
                        intent.putExtra("userid", getItem(position).getReleaseInfo().getUserId());
                        startActivity(intent);
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvTobecommList.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getWorkList != null && call_getWorkList.isCanceled())
            call_getWorkList.cancel();
    }


    @OnClick(R.id.img_tobecomm_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
