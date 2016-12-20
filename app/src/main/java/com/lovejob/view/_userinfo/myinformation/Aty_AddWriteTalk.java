package com.lovejob.view._userinfo.myinformation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
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
import com.v.rapiddev.http.okhttp3.Call;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/5.
 */
public class Aty_AddWriteTalk extends BaseActivity {

    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.et_aty_write)
    EditText etAtyWrite;
    private Activity context;
    private String user, type;
    private Call call_AddFriendTalk;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_writetalk);
        ButterKnife.bind(this);
        context = this;
        user = getIntent().getStringExtra("userpid");
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            etAtyWrite.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        } else {
            etAtyWrite.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        }
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_AddFriendTalk != null && !call_AddFriendTalk.isCanceled())
            call_AddFriendTalk.cancel();
    }


    @OnClick({R.id.img_history_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_history_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_save:
                dialog = Utils.showProgressDliago(context, "正在保存,请稍后");
                call_AddFriendTalk = LoveJob.AddFriendTalk(etAtyWrite.getText().toString(), user, type, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context, "保存成功");
                        AppManager.getAppManager().finishActivity();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                });

                break;
        }
    }
}
