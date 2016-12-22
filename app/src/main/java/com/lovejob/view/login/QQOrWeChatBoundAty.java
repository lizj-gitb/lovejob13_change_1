package com.lovejob.view.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.TimerUtil;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view.loginmodle
 * Created on 2016-11-21 21:32
 */

public class QQOrWeChatBoundAty extends BaseActivity {
    @Bind(R.id.et_number_bind)
    EditText etNumberBind;
    @Bind(R.id.et_code_bind)
    EditText etCodeBind;
    @Bind(R.id.bt_send_bind)
    TextView btSendBind;
    @Bind(R.id.et_psw1_bind)
    EditText etPsw1Bind;

    @Bind(R.id.bt_commit_bind)
    Button btCommitBind;
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    private String openId;
    private String phoneNumber, msgCode, psw1, psw2;
    //    private TaskHelper<Object> taskHelper;
//    private SendMsgCodeAsyncTask sendMsgCodeAsyncTask;
//    private ExChangeKeyAsyncTask exChangeKeyAsyncTask;
//    private BoundQQOrWeiXinTask boundQQOrWeiXinTask;
    private Call call_sendMsgCode, call_exchangeKey, call_bound;
    private TimerUtil timer;

    //    private
    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_qqbound);
        ButterKnife.bind(this);
        addEtNumberBindListener();
        setActionbar();
//        taskHelper = new TaskHelper<>();
    }

    @Override
    public void onResume_() {

    }

    private void setActionbar() {
        actionbarSave.setVisibility(View.GONE);
        actionbarTitle.setText("账号绑定");
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarTitle.setTextSize(15);
    }

    @Override
    public void onDestroy_() {
        if (call_sendMsgCode != null && !call_sendMsgCode.isCanceled()) call_sendMsgCode.cancel();
        if (timer!=null)timer.cancle();
    }

    private void addEtNumberBindListener() {
        etNumberBind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    UserInputModel inputModel = Utils.checkUserInputParams(etNumberBind);
                    if (Utils.checkMobileNumberValid(inputModel.getParams()[0])) {
                        V.d("chaeck phone number true");
                        phoneNumber = inputModel.getParams()[0];
                        btSendBind.setEnabled(true);
                    } else {
                        Utils.showToast(context, R.string.inputErrorNumber);
                        btSendBind.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.actionbar_back, R.id.bt_send_bind, R.id.bt_commit_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(context);
                break;
            case R.id.bt_send_bind:
                //发送验证码
//                Utils.showProgreceBar(context, "正在发送验证码，请稍后");
                dialog = Utils.showProgressDliago(context, "正在发送验证码，请稍后");
                call_sendMsgCode = LoveJob.sendMsgCode(phoneNumber, "0", new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context, "发送成功");
                            timer=    new TimerUtil(btSendBind, "发送验证码");
                        timer.RunTimer();
                        etNumberBind.setEnabled(false);
                        phoneNumber = etNumberBind.getText().toString().trim();
                        etCodeBind.setEnabled(true);
                        etPsw1Bind.setEnabled(true);

                        btCommitBind.setEnabled(true);
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                });
                callList.add(call_sendMsgCode);
                break;
            case R.id.bt_commit_bind:
                UserInputModel inputModel = Utils.checkUserInputParams(etCodeBind, etPsw1Bind);
                if (!inputModel.isNotEmpty()) {
                    Utils.showToast(context, R.string.inputNull);
                    return;
                }
//                if (!inputModel.getParams()[1].equals(inputModel.getParams()[2])) {
//                    Utils.showToast(context, R.string.passwordisfalse);
//                    return;
//                }
                String isP = Utils.checkPassword(inputModel.getParams()[1]);
                if ((isP.equals("中") || isP.equals("强")) && inputModel.getParams()[1].length() > 6
                        &&inputModel.getParams()[1].length()<16) {
                    V.d("will start bound...");
                    V.d("start exchange key...");
                    msgCode = inputModel.getParams()[0];
                    psw1 = inputModel.getParams()[1];
//                    Utils.showProgreceBar(context, "正在注册");
                    dialog = Utils.showProgressDliago(context, "正在绑定");
                    HandlerUtils.postTaskDelay(new Runnable() {
                        @Override
                        public void run() {
                            call_exchangeKey = LoveJob.exChangeKey(phoneNumber, 2, new OnAllParameListener() {
                                @Override
                                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                    //调用绑定接口
                                    call_bound = LoveJob.bound(phoneNumber, msgCode, psw1, new OnAllParameListener() {
                                        @Override
                                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                            V.d("登录成功");
                                            setResult(StaticParams.RequestCode.RequestCode_Aty_Login_BoundQQOrWeChatAty);
                                            AppManager.getAppManager().finishActivity(QQOrWeChatBoundAty.this);
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            dialog.dismiss();
                                            Utils.showToast(QQOrWeChatBoundAty.this, msg);
                                        }
                                    });
                                }

                                @Override
                                public void onError(String msg) {
                                    dialog.dismiss();
                                    Utils.showToast(QQOrWeChatBoundAty.this, "系统不安全");
                                }
                            });
                            callList.add(call_exchangeKey);
                        }
                    }, 700);
                } else {
                    Utils.showToast(context, "密码不符合规范");
                    return;
                }
                break;
        }
    }
}
