package com.lovejob.view._userinfo.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.qiniuyun.storage.UploadManager;
import com.lovejob.view.MainActivity;
import com.lovejob.view.login.LoginAcitvity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_Setting extends BaseActivity {
    @Bind(R.id.back_setting)
    ImageView backSetting;
    @Bind(R.id.help)
    RelativeLayout help;
    @Bind(R.id.about)
    RelativeLayout about;
    @Bind(R.id.systemversion)
    RelativeLayout systemversion;
    @Bind(R.id.bind_qq)
    RelativeLayout bindQq;
    @Bind(R.id.bind_wechat)
    RelativeLayout bindWechat;
    @Bind(R.id.systemCancellation)
    RelativeLayout Cancellation;
    @Bind(R.id.qqbounded)
    TextView qqbounded;
    @Bind(R.id.wechatbounded)
    TextView wechatbounded;
    @Bind(R.id.isupdata)
    TextView isupdata;
    String userPid, token;
    private Activity context;
    private UMShareAPI mShareAPI;

    public enum OtherType {
        WeChat, QQ
    }

    private OtherType otherType;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_setting);
        ButterKnife.bind(this);
        bindWechat.setEnabled(false);
        bindQq.setEnabled(false);
        context = this;
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        token = appPreferences.getString(StaticParams.FileKey.__LOCALTOKEN__, "");
        mShareAPI = UMShareAPI.get(context);
        getData();
    }

    @Override
    public void onResume_() {
    }

    @Override
    public void onDestroy_() {

    }

    public void getData() {
        callList.add(LoveJob.getSettingInfos(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//
//                V.d("系统最新版本号：" + thePerfectGirl.getData().getSystemVersionUpdate());
//                V.d("是否绑定微信：" + thePerfectGirl.getData().isBoundWeChat());
//                V.d("是否绑定QQ：" + thePerfectGirl.getData().isBoundQQ());
//                V.d("当前系统版本号：" + Utils_RapidDev.getAppVersionName(context));

                isupdata.setVisibility(thePerfectGirl.getData().getSystemVersionUpdate().equals(Utils_RapidDev.getAppVersionName(context.getApplicationContext()))
                        ? View.GONE : View.VISIBLE);

                if (thePerfectGirl.getData().isBoundWeChat()) {
                    bindWechat.setEnabled(false);
                    wechatbounded.setVisibility(View.VISIBLE);
                } else {
                    bindWechat.setEnabled(true);
                    wechatbounded.setVisibility(View.INVISIBLE);
                }
                if (thePerfectGirl.getData().isBoundQQ()) {
                    bindQq.setEnabled(false);
                    qqbounded.setVisibility(View.VISIBLE);
                } else {
                    bindQq.setEnabled(true);
                    qqbounded.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(String msg) {

            }
        }));
    }
    private boolean isoncl=true;
    @OnClick({R.id.back_setting, R.id.help, R.id.about, R.id.systemversion, R.id.bind_qq, R.id.bind_wechat, R.id.systemCancellation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_setting:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.help:
                startActivity(new Intent(context, Aty_Help.class));
                break;
            case R.id.about:
                startActivity(new Intent(context, Aty_AboutMe.class));
                break;
            case R.id.systemversion:
                startActivity(new Intent(context, Aty_SystemEdition.class));

                break;
            case R.id.bind_qq:
                dialog = Utils.showProgressDliago(context, "正在绑定");
                otherType = OtherType.QQ;
                if (mShareAPI.isInstall(context,SHARE_MEDIA.QQ)){
                    mShareAPI.getPlatformInfo(context, SHARE_MEDIA.QQ, ongetUserInfos);
                }else {
                    Utils.showToast(context, "请下载最新版QQ");
                    dialog.dismiss();
                }
                break;
            case R.id.bind_wechat:
                dialog = Utils.showProgressDliago(context, "正在绑定");
                otherType = OtherType.WeChat;
                if (mShareAPI.isInstall(context,SHARE_MEDIA.WEIXIN)){
                    mShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, ongetUserInfos);
                }else {
                    Utils.showToast(context, "请下载最新版微信");
                    dialog.dismiss();
                }

                break;
            case R.id.systemCancellation:
                if (isoncl){
                    Utils.showToast(context, "注销");
                    LoveJob.Cancellation(userPid, token, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            AppManager.getAppManager().finishAllActivity();
                            startActivity(new Intent(context, LoginAcitvity.class));
                            new AppPreferences(context.getApplicationContext()).put(StaticParams.FileKey.__LOCALTOKEN__, "");
                        }

                        @Override
                        public void onError(String msg) {
                            Utils.showToast(context, msg);
                        }
                    });
                    isoncl = false;
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    UMAuthListener ongetUserInfos = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            StaticParams.openId = share_media.toString() + map.get("openid").toString();
            if (TextUtils.isEmpty(StaticParams.openId)) {
                Utils.showToast(context, R.string.autherror);
                return;
            }

            if (share_media == SHARE_MEDIA.QQ) {
                StaticParams.openId = "QQ" + StaticParams.openId;
            } else if (share_media == SHARE_MEDIA.WEIXIN) {
                StaticParams.openId = "WX" + StaticParams.openId;
            }

            callList.add(LoveJob.boundQQOrWeChat(StaticParams.openId, new OnAllParameListener() {
                @Override
                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                    dialog.dismiss();
                    V.d("绑定成功");
                    StaticParams.openId = null;
                    Utils.showToast(context, "绑定成功");
                    switch (otherType) {
                        case QQ:
                            bindQq.setEnabled(false);
                            break;
                        case WeChat:
                            bindWechat.setEnabled(false);
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    dialog.dismiss();
                    Utils.showToast(context, msg);
                }
            }));

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Utils.showToast(context, R.string.autherror);
            dialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Utils.showToast(context, R.string.userCancle);
            dialog.dismiss();
        }
    };
}
