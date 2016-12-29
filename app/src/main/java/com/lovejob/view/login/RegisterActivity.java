package com.lovejob.view.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.umeng.analytics.MobclickAgent;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:注册页面
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view.loginmodle
 * Created on 2016-11-21 18:55
 */

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.tv_register_usertype_1)
    TextView tvRegisterUsertype1;
    @Bind(R.id.tv_register_usertype_2)
    TextView tvRegisterUsertype2;
    @Bind(R.id.et_register_phonenumber)
    EditText etRegisterPhonenumber;
    @Bind(R.id.tv_register_getmsgcode)
    TextView tvRegisterGetmsgcode;
    @Bind(R.id.et_register_msgcode)
    EditText etRegisterMsgcode;
    @Bind(R.id.et_register_psw1)
    EditText etRegisterPsw1;
    @Bind(R.id.et_register_psw2)
    EditText etRegisterPsw2;
    @Bind(R.id.bt_register_commit)
    Button btRegisterCommit;
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.yiyouzhanghao)
    TextView yiyouzhanghao;
    private int userType = 0x00;//默认用户类型为个人
    private Call call_sendMsgCode, call_exchangekey, call_register;
    private String phoneNumber;
    private String password;
    private String msgCode;
    private TimerUtil timer;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_register);
        ButterKnife.bind(this);
        addEditTextListener();
        setActionbar();
//        taskHelper = new TaskHelper<>();
    }

    private void setActionbar() {
        actionbarSave.setVisibility(View.GONE);
        actionbarTitle.setText("新用户注册");
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarTitle.setTextSize(15);
    }

    @Override
    public void onResume_() {
        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    @Override
    public void onDestroy_() {
        try {
            if (timer != null)
                timer.cancle();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void addEditTextListener() {
        etRegisterPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    UserInputModel inputModel = Utils.checkUserInputParams(etRegisterPhonenumber);
                    if (Utils.checkMobileNumberValid(inputModel.getParams()[0])) {
                        V.d("chaeck phone number true");
                        tvRegisterGetmsgcode.setEnabled(true);
                    } else {
                        Utils.showToast(context, R.string.inputErrorNumber);
                        tvRegisterGetmsgcode.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.yiyouzhanghao, R.id.actionbar_back, R.id.tv_register_usertype_1, R.id.tv_register_usertype_2, R.id.tv_register_getmsgcode, R.id.bt_register_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(context);
                break;

            case R.id.yiyouzhanghao:
                AppManager.getAppManager().toNextPage(this, LoginAcitvity.class, true);
                break;
            case R.id.tv_register_usertype_1:
                tvRegisterUsertype1.setTextColor(getResources().getColor(R.color.white));
                tvRegisterUsertype1.setBackground(getResources().getDrawable(R.mipmap.button_user_left_off));

                tvRegisterUsertype2.setTextColor(getResources().getColor(R.color.actionbar));
                tvRegisterUsertype2.setBackground(getResources().getDrawable(R.mipmap.button_user_right_on));
                userType = 0x00;
                break;
            case R.id.tv_register_usertype_2:
                tvRegisterUsertype2.setTextColor(getResources().getColor(R.color.white));
                tvRegisterUsertype2.setBackground(getResources().getDrawable(R.mipmap.button_user_right_off));
                tvRegisterUsertype1.setTextColor(getResources().getColor(R.color.actionbar));
                tvRegisterUsertype1.setBackground(getResources().getDrawable(R.mipmap.button_user_left_on));
                userType = 0x01;
                break;
            case R.id.tv_register_getmsgcode:
                V.d("send msg code...");
                if (etRegisterPhonenumber.getText() == null) return;
                if (!Utils.checkMobileNumberValid(etRegisterPhonenumber.getText().toString())) {
                    Utils.showToast(this, R.string.inputErrorNumber);
                    return;
                }
                dialog = Utils.showProgressDliago(context, "正在发送验证码");
                phoneNumber = etRegisterPhonenumber.getText().toString();
                  /*
                发送验证码
                 */
                call_sendMsgCode = LoveJob.sendMsgCode(phoneNumber, "0", new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context, "发送成功");
                        V.d("send msg code success");
                        etRegisterPhonenumber.setEnabled(false);
                        tvRegisterGetmsgcode.setEnabled(false);
                        etRegisterMsgcode.setEnabled(true);
                        etRegisterPsw1.setEnabled(true);
                        etRegisterPsw2.setEnabled(true);
                        btRegisterCommit.setEnabled(true);
                        /*
                        倒计时
                         */
                        timer = new TimerUtil(tvRegisterGetmsgcode, "获取验证码");
                        timer.RunTimer();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                });
                callList.add(call_sendMsgCode);
                break;
            case R.id.bt_register_commit:
                UserInputModel inputModel = Utils.checkUserInputParams(etRegisterMsgcode, etRegisterPsw1, etRegisterPsw2);
                if (!inputModel.isNotEmpty()) {
                    Utils.showToast(context, R.string.inputNull);
                    return;
                }

                if (!inputModel.getParams()[1].equals(inputModel.getParams()[2])) {
                    Utils.showToast(context, R.string.passwordisfalse);
                    return;
                }

                String isP = Utils.checkPassword(inputModel.getParams()[1]);
                if ((isP.equals("中") || isP.equals("强")) && inputModel.getParams()[1].length() > 6) {
                    V.d("will start register...");
                    V.d("start exchange key...");
                    msgCode = inputModel.getParams()[0];
                    password = inputModel.getParams()[1];
                    dialog = Utils.showProgressDliago(context, "正在注册");
                    HandlerUtils.postTaskDelay(new Runnable() {
                        @Override
                        public void run() {
                            call_exchangekey = LoveJob.exChangeKey(phoneNumber, 2, new OnAllParameListener() {
                                @Override
                                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                    call_register = LoveJob.register(phoneNumber, password, String.valueOf(userType), msgCode, context, new OnAllParameListener() {
                                        @Override
                                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                            V.d("reister success");
                                            Intent intent = new Intent();
                                            intent.putExtra("phoneNumber", phoneNumber);
                                            intent.putExtra("passWord", password);
                                            setResult(StaticParams.RequestCode.REQUESTCODE_LOGINACTIVITY_TO_REGISTERACTIVITY, intent);
                                            AppManager.getAppManager().finishActivity(context);
                                        }

                                        @Override
                                        public void onError(String msg) {

                                            try {
                                                dialog.dismiss();
                                                Utils.showToast(context, msg);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onError(String msg) {
                                    dialog.dismiss();
                                    Utils.showToast(context, msg);
                                }
                            });
                            callList.add(call_exchangekey);
                        }
                    }, 700);

                } else {
                    Utils.showToast(context, R.string.inputErrorPassword);
                    return;
                }

                break;
        }
    }

}
