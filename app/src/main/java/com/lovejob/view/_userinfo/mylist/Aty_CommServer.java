package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_CommServer extends BaseActivity {

    @Bind(R.id.img_comm_back)
    ImageView imgCommBack;
    @Bind(R.id.tv_comm_commit)
    TextView tvCommCommit;
    @Bind(R.id.img_comm_logo)
    CircleImageView imgCommLogo;
    @Bind(R.id.rating)
    RatingBar rating;
    @Bind(R.id.et_comm_edit)
    EditText etCommEdit;
    Activity context;
    String serverPid,useid,pictrue;
    String requestType;
    private Call call_comMent;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_comm);
        ButterKnife.bind(this);
        context = this;
        serverPid = getIntent().getStringExtra("serverPid");
        useid = getIntent().getStringExtra("userid");
        pictrue=getIntent().getStringExtra("pictrue");
        Glide.with(context).load(StaticParams.QiNiuYunUrl+pictrue).into(imgCommLogo);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_comMent != null && call_comMent.isCanceled())
            call_comMent.cancel();
    }


    @OnClick({R.id.img_comm_back, R.id.tv_comm_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_comm_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_comm_commit:
                if (!TextUtils.isEmpty(etCommEdit.getText())) {
                    if (etCommEdit.getText().toString().length() < 5) {
                        Utils.showToast(context, "内容不能少于五个字");
                        return;
                    }
                } else {
                    Utils.showToast(context, "内容不能为空");
                    return;
                }
                call_comMent = LoveJob.comMent("68", serverPid, useid, String.valueOf(rating.getRating()), etCommEdit.getText().toString().trim(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        Utils.showToast(context, "评论成功");
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, TextUtils.isEmpty(msg) ? "网络异常" : msg);
                    }
                });

                break;
        }
    }
}
