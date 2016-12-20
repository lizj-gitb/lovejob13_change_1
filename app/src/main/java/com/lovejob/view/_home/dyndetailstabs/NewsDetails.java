package com.lovejob.view._home.dyndetailstabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.v.rapiddev.base.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/20.
 */

public class NewsDetails extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.tv_newsdetails_subtitle)
    TextView tvNewsdetailsSubtitle;
    @Bind(R.id.tv_newsdetails_date)
    TextView tvNewsdetailsDate;
    @Bind(R.id.tv_newsdetails_time)
    TextView tvNewsdetailsTime;
    @Bind(R.id.tv_newsdetails_number)
    TextView tvNewsdetailsNumber;
    @Bind(R.id.rv_newsdetails)
    RecyclerView rvNewsdetails;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.newsdetails);
        ButterKnife.bind(this);
        setActionbar();
        AddData();
    }

    private void AddData() {
//        LoveJob
    }

    private void setActionbar() {
            actionbarSave.setVisibility(View.GONE);
        actionbarTitle.setText("新闻详情");
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }


    @OnClick(R.id.actionbar_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
