package com.lovejob.view._userinfo.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.OkHttpClient;
import com.v.rapiddev.utils.V;
import com.zwy.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_SystemEdition extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_now_system)
    TextView tvNowSystem;
    @Bind(R.id.tv_new_system)
    TextView tvNewSystem;
    @Bind(R.id.system_update)
    RelativeLayout systemUpdate;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_systemedition);
        ButterKnife.bind(this);
        AddData();
    }

    private void AddData() {
        V.d("banbenhao:" + Utils.getAppVersionName(context));
//        Utils.getAppVersionName(context);
        //TODO 添加版本信息
        //如果用户是最新版本，则不显示更新按钮，只显示：您目前已是最新版本无需更新。
        LoveJob.getSystemVersion(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                String v = thePerfectGirl.getData().getAboutusDTO().getVersion();
                if (Utils.getAppVersionName(context).equals(v)) {
                    systemUpdate.setVisibility(View.GONE);
                } else {
                    systemUpdate.setVisibility(View.VISIBLE);
                }
                tvNewSystem.setText("V"+v);
            }

            @Override
            public void onError(String msg) {
                com.lovejob.model.Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onResume_() {
        tvNowSystem.setText("V" + Utils.getAppVersionName(context));
    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.back, R.id.system_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.system_update:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.lovejob#opened");
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
    }
}
