package com.lovejob.view._userinfo.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */
public class Aty_TongZhi extends BaseActivity {
    @Bind(R.id.img_news_back)
    ImageView imgNewsBack;
    @Bind(R.id.lv_tongzhi)
    PullToRefreshListView lvTongzhi;
    private FastAdapter<ThePerfectGirl.workPushDTO> adapter;
    Activity context;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_tongzhi);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
        addData();
    }

    private void addData() {
        LoveJob.getTongzhiList(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkPushDTO() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkPushDTO().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkPushDTO().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.workPushDTO>(context, R.layout.item_lv_tongzhi) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_tongzhi, position);
                if (getItem(position).getTypeDec().equals("待录取")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText(getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("刚刚报名了您发布的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText("工作岗位。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("待服务")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText(getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("购买了您的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText("服务。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("已验收")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText(getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("验收您的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText((getItem(position).getWorkType()).equals("1") ? "服务。" : "工作。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("已评价")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText(getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("对您的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText((getItem(position).getWorkType()).equals("1") ? "服务做出了评价。" : "工作做出了评价。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("退款通知")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText(getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("对您的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText((getItem(position).getWorkType()).equals("1") ? "服务申请了退款。" : "工作申请了退款。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("已录用")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText("您");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("申请的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText("已被录用。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                if (getItem(position).getTypeDec().equals("退款成功")) {
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_type)).setText(getItem(position).getTypeDec());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_name)).setText("您对" + getItem(position).getUserName());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_first)).setText("的");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_title)).setText("[" + getItem(position).getTitle() + "]");
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_foot)).setText((getItem(position).getWorkType()).equals("1") ? "服务的退款申请已通过，退款金额已发放到您的余额。" : "工作的退款申请已通过，退款金额已发放到您的余额。");
                    String s = String.format("%tR%n", getItem(position).getCreateDate());
                    ((TextView) viewHolder.getView(R.id.tv_tongzhi_time)).setText(s);
                }
                return viewHolder.getConvertView();
            }
        };
        lvTongzhi.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }




    @OnClick(R.id.img_news_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
