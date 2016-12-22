package com.lovejob.view.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
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
 * Created on 2016-11-21 20:33
 */

public class ForgotPassWordAty extends BaseActivity {
    @Bind(R.id.et_number_forgot)
    EditText etNumberForgot;
    @Bind(R.id.et_psw1_forgot)
    EditText etPsw1Forgot;
    @Bind(R.id.et_psw2_forgot)
    EditText etPsw2Forgot;
    @Bind(R.id.et_code_forgot)
    EditText etCodeForgot;
    @Bind(R.id.bt_send_forgot)
    TextView btSendForgot;
    @Bind(R.id.bt_next_forgot)
    Button btNextForgot;
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    private TimerUtil timer;
    private String phoneNumber, passWord, msgCode;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_forgotpassword);
        ButterKnife.bind(this);
        setActionbar();
        /**
         * 输入状态监听
         */
        addEditListener();
//        taskHelper = new TaskHelper<>();
    }

    private void setActionbar() {
        actionbarSave.setVisibility(View.GONE);
        actionbarTitle.setText("重置密码");
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarTitle.setTextSize(15);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (timer != null) timer.cancle();
    }

    @OnClick({R.id.actionbar_back, R.id.bt_send_forgot, R.id.bt_next_forgot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.bt_send_forgot:
                UserInputModel inputModel = Utils.checkUserInputParams(etNumberForgot, etPsw1Forgot, etPsw2Forgot);
                if (!inputModel.isNotEmpty()) {
                    Utils.showToast(context, R.string.inputNull);
                    return;
                }
                if (!Utils.checkMobileNumberValid(inputModel.getParams()[0])) {
                    Utils.showToast(context, R.string.inputErrorNumber);
                    return;
                }
                if (!inputModel.getParams()[1].equals(inputModel.getParams()[2])) {
                    Utils.showToast(context, R.string.passwordisfalse);
                    return;
                }
                String isP = Utils.checkPassword(inputModel.getParams()[1]);
                if ((isP.equals("中") || isP.equals("强")) && inputModel.getParams()[1].length() > 6
                        &&inputModel.getParams()[1].length()<16) {
                    phoneNumber = inputModel.getParams()[0];
                    passWord = inputModel.getParams()[1];
//                    msgCode = inputModel.getParams()[3];
                    dialog = Utils.showProgressDliago(context, "正在发送验证码，请稍后");
                    callList.add(LoveJob.sendMsgCode(phoneNumber, "1", new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            dialog.dismiss();
                            Utils.showToast(context, R.string.sendMsgCodeSuccess);
                            etCodeForgot.setEnabled(true);
                            btNextForgot.setEnabled(true);
                            V.d("send msg code success");
                        /*
                        倒计时
                         */
                            timer = new TimerUtil(btSendForgot, "获取验证码");
                            timer.RunTimer();
                        }

                        @Override
                        public void onError(String msg) {
                            dialog.dismiss();
                            Utils.showToast(context, msg);
                        }
                    }));
                } else {
                    Utils.showToast(context, "密码不符合规范");
                    return;
                }
                break;
            case R.id.bt_next_forgot:
                UserInputModel _inputModel1 = Utils.checkUserInputParams(etCodeForgot);
                if (_inputModel1.isNotEmpty()) {
                    msgCode = _inputModel1.getParams()[0];
                    //交换密钥
                    dialog = Utils.showProgressDliago(context, "正在重置密码，请稍后");
                    callList.add(LoveJob.exChangeKey(phoneNumber, 0, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            V.d("exchange key success");
                            //重置密码
                            callList.add(LoveJob.forgotpsw(phoneNumber, passWord, msgCode, new OnAllParameListener() {
                                @Override
                                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                    dialog.dismiss();
                                    Utils.showToast(ForgotPassWordAty.this, R.string.forgotPasswordSuccess);
                                    V.d("forgot password success");
                                    setResult(StaticParams.RequestCode.RequestCode_Aty_Login_FORGOTPASSWORD);
                                    AppManager.getAppManager().finishActivity(ForgotPassWordAty.this);
                                }

                                @Override
                                public void onError(String msg) {
                                    dialog.dismiss();
                                    Utils.showToast(context, msg);
                                }
                            }));
                        }

                        @Override
                        public void onError(String msg) {
                            dialog.dismiss();
                            Utils.showToast(context, msg);
                        }
                    }));
                } else {
                    Utils.showToast(context, R.string.inputNull);
                }
                break;
        }
    }

    private void addEditListener() {
        etNumberForgot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (Utils.checkMobileNumberValid(s.toString())) {
                        setV(true);
                    } else {
                        etNumberForgot.setTextColor(Color.RED);
                         /*
                         抖动提示用户
                             */
                        etNumberForgot.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                        Utils.showToast(context, R.string.inputErrorNumber);
                        setV(false);
                    }
                } else {
                    setV(false);
                }

                if (s.toString().trim().length() < 11) {
                    etNumberForgot.setTextColor(getResources().getColor(R.color.defaultTextColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setV(boolean isEnabled) {
        btSendForgot.setEnabled(isEnabled);
        etPsw1Forgot.setEnabled(isEnabled);
        etPsw2Forgot.setEnabled(isEnabled);
    }
}
