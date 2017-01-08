package com.lovejob.view._userinfo.mylist;

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
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_Myserver_ToComm extends BaseActivity {
    @Bind(R.id.img_tobecomm_back)
    ImageView imgTobecommBack;
    @Bind(R.id.lv_myserver_tocomm)
    PullToRefreshListView lvMyserverTocomm;
    private FastAdapter<ThePerfectGirl.ServerDTO> adapter;
    private Call call_getServiceList;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_myservertocomm);
        ButterKnife.bind(this);
        initAdapter();
        addData();
        setRefreshListener();
    }

    private void setRefreshListener() {
        lvMyserverTocomm.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.removeAll();
                addData();
                Utils.showToast(context, "刷新成功");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    private void addData() {
        call_getServiceList = LoveJob.getServiceList("417", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvMyserverTocomm.onRefreshComplete();
                if (thePerfectGirl.getData().getServerDTOList() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getServerDTOList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getServerDTOList().get(i));
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

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.ServerDTO>(context, R.layout.item_lv_myserver_tocomm) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_myserver_tocomm, position);
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_posetion)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_commpl)).setText(getItem(position).getUserInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_price)).setText(getItem(position).getMoney() + "元");
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getUserInfo().getPortraitId().toString().trim()).into((CircleImageView) viewHolder.getView(R.id.img_ser_tobecomm_logo));
                ((TextView) viewHolder.getView(R.id.tv_ser_tobecomm_commit)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Aty_CommServer.class);
                        intent.putExtra("serverPid", getItem(position).getServerRelationPid());
                        intent.putExtra("userid", getItem(position).getUserInfo().getUserId());
                        startActivity(intent);
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvMyserverTocomm.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getServiceList != null && call_getServiceList.isCanceled())
            call_getServiceList.cancel();
    }


    @OnClick(R.id.img_tobecomm_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
