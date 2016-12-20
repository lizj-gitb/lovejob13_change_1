package com.lovejob.view.payinfoviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.Utils;
import com.lovejob.view._job.JobDetails;
import com.v.rapiddev.base.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view
 * Created on 2016-11-28 22:36
 */

public class SendJobSuccess extends BaseActivity {
    @Bind(R.id.tv_buyprompt_buy)
    TextView tvBuypromptBuy;
    @Bind(R.id.tv_buyprompt_back)
    TextView tvBuypromptBack;
    private String workPid;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.sendjobsuccess);
        ButterKnife.bind(this);
        workPid = getIntent().getStringExtra("workPid");
        if (workPid == null) {
            Utils.showToast(context, "系统异常");
            return;
        }

    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.tv_buyprompt_buy, R.id.tv_buyprompt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_buyprompt_buy:
                Intent in =new Intent(context, PayView.class);
                in.putExtra("workPid",workPid);
                startActivity(in);
                break;
            case R.id.tv_buyprompt_back:
                Intent intent =new Intent(context, JobDetails.class);
                intent.putExtra("workId",workPid);
                startActivity(intent);
                break;
        }
        AppManager.getAppManager().finishActivity(this);
    }
}
