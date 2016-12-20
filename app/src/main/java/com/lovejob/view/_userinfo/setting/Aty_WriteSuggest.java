package com.lovejob.view._userinfo.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/10.
 */
public class Aty_WriteSuggest extends BaseActivity {
    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.tvsettitle)
    TextView tvsettitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.et_aty_write)
    EditText etAtyWrite;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_writetalk);
        ButterKnife.bind(this);
        tvSave.setText("提交");
        tvsettitle.setText("反馈意见");
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.img_history_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_history_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_save:
                if ((TextUtils.isEmpty(etAtyWrite.getText().toString()))) {
                    Utils.showToast(context, "信息不能为空");
                    return;
                }
                dialog = Utils.showProgressDliago(context, "正在提交");
                callList.add(LoveJob.KeepSuggest(etAtyWrite.getText().toString(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context, "提交已完成");
                        AppManager.getAppManager().finishActivity();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                }));
                break;
        }
    }
}
