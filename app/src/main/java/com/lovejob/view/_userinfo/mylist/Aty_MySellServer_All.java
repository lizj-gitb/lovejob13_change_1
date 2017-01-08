package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
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
public class Aty_MySellServer_All extends BaseActivity {
    @Bind(R.id.img_tobecomm_back)
    ImageView imgTobecommBack;
    @Bind(R.id.lv_mysellser_all)
    PullToRefreshListView lvMysellserAll;
    Activity context;
    private FastAdapter<ThePerfectGirl.ServerDTO> adapter;
    private Call call_getServiceList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mysellserall);
        ButterKnife.bind(this);
        context =this;
        setRefreshListener();
        initAdapter();
        addData();
    }

    private void addData() {
        call_getServiceList = LoveJob.getServiceList("418", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvMysellserAll.onRefreshComplete();
                if (thePerfectGirl.getData().getServerDTOList()!=null){
                    for (int i = 0; i <thePerfectGirl.getData().getServerDTOList().size() ; i++) {
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
        adapter = new FastAdapter<ThePerfectGirl.ServerDTO>(context,R.layout.item_lv_mybuyser_all) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context,convertView,parent,R.layout.item_lv_mybuyser_all,position);
                ((TextView)viewHolder.getView(R.id.tv_mysersell_title)).setText(getItem(position).getTitle());
                ((TextView)viewHolder.getView(R.id.tv_mysersell_money)).setText(getItem(position).getMoney()+"元");
                ((TextView)viewHolder.getView(R.id.tv_mysersell_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView)viewHolder.getView(R.id.tv_mysersell_position)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView)viewHolder.getView(R.id.tv_mysersell_company)).setText(getItem(position).getUserInfo().getCompany());
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getUserInfo().getPortraitId().toString().trim()).into((CircleImageView) viewHolder.getView(R.id.img_mysersell_logo));
                return viewHolder.getConvertView();
            }
        };
        lvMysellserAll.setAdapter(adapter);
    }

    private void setRefreshListener() {
        lvMysellserAll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.removeAll();
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getServiceList!= null && call_getServiceList.isCanceled())
            call_getServiceList.cancel();
    }


    @OnClick(R.id.img_tobecomm_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
