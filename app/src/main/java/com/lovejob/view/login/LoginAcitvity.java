package com.lovejob.view.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.lovejob.AppConfig;
import com.lovejob.BaseActivity;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.ms.MainActivityMs;
import com.lovejob.view.MainActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.ClearEditText;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:登录页面
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view.loginmodle
 * Created on 2016-11-21 10:37
 */

public class LoginAcitvity extends BaseActivity {
    @Bind(R.id.tv_login_version)
    TextView tvLoginVersion;
    @Bind(R.id.et_login_phonenumber)
    EditText etLoginPhonenumber;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.tv_login_forgotpassword)
    TextView tvLoginForgotpassword;
    @Bind(R.id.tv_login_goregister)
    TextView tvLoginGoregister;
    @Bind(R.id.bt_login_commit)
    Button btLoginCommit;
    @Bind(R.id.img_login_loginwithwechat)
    ImageView imgLoginLoginwithwechat;
    @Bind(R.id.img_login_loginwithqq)
    ImageView imgLoginLoginwithqq;
    @Bind(R.id.iv_search_clear_num)
    ImageView iv_search_clear_num;
    @Bind(R.id.iv_search_clear_psw)
    ImageView iv_search_clear_psw;

    private Call call_exchangeKey, call_login, call_loginOther;
    private String phoneNumber, password;
    private UMShareAPI mShareAPI;
    private boolean isLoginOther = false;//是否为第三方登录
    private String toOtherActivity;
    private String otherId;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.loginlayout);
        ButterKnife.bind(this);
        V.d("into login activity...");
        toOtherActivity = getIntent().getStringExtra("toOtherActivity");
        otherId = getIntent().getStringExtra("otherId");
        etLoginPhonenumber.setText(new AppPreferences(context.getApplicationContext()).getString(StaticParams.FileKey.__UserNumber__, ""));
        addEditTestListener();
        initView();

    }

    private void addEditTestListener() {
        if (!TextUtils.isEmpty(etLoginPhonenumber.getText().toString())) {
            iv_search_clear_num.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            iv_search_clear_psw.setVisibility(View.VISIBLE);
        }

        etLoginPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    iv_search_clear_num.setVisibility(View.VISIBLE);
                } else {
                    iv_search_clear_num.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    iv_search_clear_psw.setVisibility(View.VISIBLE);
                } else {
                    iv_search_clear_psw.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
//        taskHelper = new TaskHelper<>();
        mShareAPI = UMShareAPI.get(context);
        setDefaultVersion();
    }

    private void setDefaultVersion() {
        tvLoginVersion.setText("Version:" + Utils_RapidDev.getAppVersionName(context));
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_exchangeKey != null && !call_exchangeKey.isCanceled())
            call_exchangeKey.cancel();
        if (call_login != null && !call_login.isCanceled()) call_login.cancel();
        V.d("login activity was destroyed...");
    }

    @OnClick({R.id.iv_search_clear_num, R.id.iv_search_clear_psw, R.id.tv_login_forgotpassword, R.id.tv_login_goregister, R.id.bt_login_commit, R.id.img_login_loginwithwechat, R.id.img_login_loginwithqq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forgotpassword:
                V.d("tv_login_forgotpassword");
                startActivityForResult(new Intent(context, ForgotPassWordAty.class), StaticParams.RequestCode.RequestCode_Aty_Login_FORGOTPASSWORD);
                break;
            case R.id.tv_login_goregister:
                V.d("tv_login_goregister");
                startActivityForResult(new Intent(context, RegisterActivity.class), StaticParams.RequestCode.REQUESTCODE_LOGINACTIVITY_TO_REGISTERACTIVITY);
                break;
            case R.id.bt_login_commit:
                V.d("bt_login_commit");
                checkPermission();
                break;
            case R.id.img_login_loginwithwechat:
                V.d("img_login_loginwithwechat");
                mShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, ongetUserInfos);
                break;
            case R.id.img_login_loginwithqq:
                V.d("img_login_loginwithqq");
//                dialog = Utils.showProgressDliago(context, "正在调起QQ");
                mShareAPI.getPlatformInfo(context, SHARE_MEDIA.QQ, ongetUserInfos);
                break;
            case R.id.iv_search_clear_num:
                etLoginPhonenumber.setText("");
                iv_search_clear_num.setVisibility(View.GONE);
                break;
            case R.id.iv_search_clear_psw:
                etLoginPassword.setText("");
                iv_search_clear_psw.setVisibility(View.GONE);
                break;
        }
    }

    private void checkPermission() {
        CheckAnnotatePermission
                .from(this, this)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }


    @PermissionGranted
    public void onSucc() {

        if (call_exchangeKey != null && !call_exchangeKey.isCanceled())
            call_exchangeKey.cancel();
        if (call_login != null && !call_login.isCanceled()) call_login.cancel();
        /**
         * 检测用户输入是否为空
         */
        UserInputModel inputModel = Utils.checkUserInputParams(etLoginPhonenumber, etLoginPassword);
        V.d("__:" + inputModel.toString());
        if (!inputModel.isNotEmpty()) {
            Utils.showToast(context, R.string.inputNull);
            return;
        }
        if (!Utils.checkMobileNumberValid(inputModel.getParams()[0])) {
            Utils.showToast(context, R.string.inputErrorNumber);
            return;
        }
        V.d("___:" + Utils.checkPassword(inputModel.getParams()[1]));
        V.d("will start login...");
        V.d("start exchange key...");
        phoneNumber = inputModel.getParams()[0];
        password = inputModel.getParams()[1];
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                dialog = Utils.showProgressDliago(context, "正在登录，请稍后……");
            }
        });

        HandlerUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                //交换密钥
                call_exchangeKey = LoveJob.exChangeKey(phoneNumber, 0, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        //登录
                        call_login = LoveJob.startLogin(phoneNumber, password, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                                        V.d("login success,start connect rong service...");
//                                        if (!AppConfig.isConnectRongService) {
//                                            RongIM.connect(MyApplication.getAppPreferences().getString(StaticParams.FileKey.__RONGTOKEN__, ""), new MyConnectCallback());
//                                        } else {
                                Intent intent =new Intent(LoginAcitvity.this, MainActivityMs.class);
                                intent.putExtra("otherId", otherId);
                                intent.putExtra("toOtherActivity", toOtherActivity);
                                startActivity(intent);
                                new AppPreferences(context.getApplicationContext()).put(StaticParams.FileKey.__UserNumber__, etLoginPhonenumber.getText().toString());
                                AppManager.getAppManager().finishActivity(context);
//                                        }
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg.equals("-666") ? "用户不存在" : msg);
                    }
                });
                callList.add(call_login);
                callList.add(call_exchangeKey);
            }
        }, 700);
    }

    @PermissionDenied
    public void onError() {
        Utils.showToast(context, "为了更好的体验app请至权限管理处授予相关权限！");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == StaticParams.RequestCode.REQUESTCODE_LOGINACTIVITY_TO_REGISTERACTIVITY) {
            if (data == null) return;
            etLoginPhonenumber.setText(data.getStringExtra("phoneNumber"));
            etLoginPassword.setText(data.getStringExtra("passWord"));
            Utils.showToast(context, R.string.registerSuccess);
        } else if (resultCode == StaticParams.RequestCode.RequestCode_Aty_Login_BoundQQOrWeChatAty) {
            Intent intent =new Intent(LoginAcitvity.this, MainActivityMs.class);
            intent.putExtra("otherId", otherId);
            intent.putExtra("toOtherActivity", toOtherActivity);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(LoginAcitvity.class);
            new AppPreferences(context.getApplicationContext()).put(StaticParams.FileKey.__UserNumber__, etLoginPhonenumber.getText().toString());
//            connectRongYun(MyApplication.getAppPreferences().getString(StaticParams.FileKey.__RONGTOKEN__, ""));
        } else if (resultCode == StaticParams.RequestCode.RequestCode_Aty_Login_FORGOTPASSWORD) {
            etLoginPassword.setText("");
        }
    }

    //获取用户资料回调
    UMAuthListener ongetUserInfos = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            V.d(share_media.toString() + "user data get success,\n:" + map);
            StaticParams.openId = share_media.toString() + map.get("openid").toString();
//            Utils.dissmissprogreceBar(context);
            if (TextUtils.isEmpty(StaticParams.openId)) {
                Utils.showToast(context, R.string.autherror);
                return;
            }
            if (share_media == SHARE_MEDIA.QQ) {
                StaticParams.openId = "QQ" + StaticParams.openId;
            } else if (share_media == SHARE_MEDIA.WEIXIN) {
                StaticParams.openId = "WX" + StaticParams.openId;
            }
//            Utils.showProgreceBar(context, "正在登录……");
            HandlerUtils.post(new Runnable() {
                @Override
                public void run() {
                    dialog = Utils.showProgressDliago(context, "正在登录……");
                }
            });
            isLoginOther = true;
            call_exchangeKey = LoveJob.exChangeKey(StaticParams.openId, 1, new OnAllParameListener() {
                @Override
                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                    V.d("交换密钥成功，开始第三方登录");
                    call_loginOther = LoveJob.startLogin(StaticParams.openId, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            dialog.dismiss();
                            Intent intent =new Intent(LoginAcitvity.this, MainActivityMs.class);
                            intent.putExtra("otherId", otherId);
                            intent.putExtra("toOtherActivity", toOtherActivity);
                            startActivity(intent);
                            AppManager.getAppManager().finishActivity(LoginAcitvity.class);
//                            V.d("login success,start connect rong service...");
//                            AppPreferences appPreferences = new AppPreferences(context);
//                            connectRongYun(appPreferences.getString(StaticParams.FileKey.__RONGTOKEN__, ""));
                        }

                        @Override
                        public void onError(String msg) {
                            dialog.dismiss();
                            V.d(msg);
                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    dialog.dismiss();
                    if (msg.equals("-666")) {
                        Utils.showToast(context, "跳转绑定页面");
                        startActivityForResult(new Intent(context, QQOrWeChatBoundAty.class), StaticParams.RequestCode.RequestCode_Aty_Login_BoundQQOrWeChatAty);
                    } else {
                        Utils.showToast(context, R.string.SystemErrpr);
                    }
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            V.e("get user data error ");
//            Utils.dissmissprogreceBar(context);
            Utils.showToast(context, R.string.autherror);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            V.e("get user data error , user cancle..");
//            Utils.dissmissprogreceBar(context);
            Utils.showToast(context, R.string.userCancle);
        }
    };

}
