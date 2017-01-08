package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
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
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_MyBuyServer_All extends BaseActivity {
    @Bind(R.id.img_tobecomm_back)
    ImageView imgTobecommBack;
    @Bind(R.id.lv_mybuyser_all)
    PullToRefreshListView lvMybuyserAll;
    private FastAdapter<ThePerfectGirl.ServerDTO> adapter;
    private Call call_getServiceList;
    Activity context;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mybuyserall);
        ButterKnife.bind(this);
        initAdapter();
        context = this;
        addData();
        setRefreshListener();


    }

    private void setRefreshListener() {
        lvMybuyserAll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.removeAll();
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                lvMybuyserAll.onRefreshComplete();
            }
        });
    }

    private void addData() {
        call_getServiceList = LoveJob.getServiceList("418", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvMybuyserAll.onRefreshComplete();
                if (thePerfectGirl.getData().getServerDTOList()!=null){
                    for (int i = 0; i < thePerfectGirl.getData().getServerDTOList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getServerDTOList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context,msg);
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.ServerDTO>(context,R.layout.item_lv_mysellser_all) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context,convertView,parent,R.layout.item_lv_mysellser_all,position);
                String s1 = String.format("%tF%n", getItem(position).getCreateDate());
                ((TextView)viewHolder.getView(R.id.tv_myserbuy_date)).setText(s1.substring(5, s1.length()));

                ((TextView)viewHolder.getView(R.id.tv_myserbuy_title)).setText(getItem(position).getTitle());
                ((TextView)viewHolder.getView(R.id.tv_myserbuy_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView)viewHolder.getView(R.id.tv_myserbuy_position)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView)viewHolder.getView(R.id.tv_myserbuy_company)).setText(getItem(position).getUserInfo().getCompany());
                ((TextView)viewHolder.getView(R.id.tv_myserbuy_money)).setText(getItem(position).getMoney()+"");
                TextView tvState = viewHolder.getView(R.id.tv_myserbuy_state);
                switch (getItem(position).getState()){
                    case "1":
                        tvState.setText("待确认");
                        break;
                    case "2":
                        tvState.setText("待评价");
                        break;
                    case "3":
                        tvState.setText("退款中");
                        break;
                    case "4":
                        tvState.setText("待评价");
                        break;
                    case "5":
                        tvState.setText("已完成");
                        break;
                }
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_myserbuy_logo));
                return viewHolder.getConvertView();
            }
        };
        lvMybuyserAll.setAdapter(adapter);
    }

    @Override
    public void onResume_() {
//      if (call_getServiceList!= null && call_getServiceList.isCanceled());
//        call_getServiceList.cancel();
    }

    @Override
    public void onDestroy_() {
        if (call_getServiceList != null && call_getServiceList.isCanceled())
            call_getServiceList.cancel();
    }
    @OnClick({R.id.img_tobecomm_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_tobecomm_back:
                finish();
                break;

        }
    }

}
