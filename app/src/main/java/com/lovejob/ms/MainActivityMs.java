package com.lovejob.ms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lovejob.BaseActivity;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.DisplayUtils;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view.login.AQQQ;
import com.lovejob.view.login.LoginAcitvity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.UMShareAPI;
import com.v.rapiddev.guide.GuideHelper;
import com.v.rapiddev.notifactioninfo.Effects;
import com.v.rapiddev.notifactioninfo.NiftyNotificationView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.swipebacklayout.SwipeBackLayout;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.zwy.activitymanage.AppManager;
import com.zwy.logger.Logger;

import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.iwf.photopicker.PhotoPicker;

/**
 * ClassType: 展示5个fragment
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view
 * Created on 2016-11-21 18:20
 */

public class MainActivityMs extends BaseActivity {
    @Bind(android.R.id.tabhost)
    MyFragmentTabHost mTabHost;
    private TabWidget tabWidget;
    int maxIntRquest = 0;
    int maxIntResponse = 0;

    private void getParamFromHtml() {
        String toOtherActivity = getIntent().getStringExtra("toOtherActivity");
        String otherId = getIntent().getStringExtra("otherId");
        Logger.e("从H5页面跳转进入首页,otherId=" + otherId + "，toOtherActivity=" + toOtherActivity);
        switch (toOtherActivity) {
            case "0":
                //跳转新闻页面，带入参数
                Logger.e("1111111111");
                Intent intent = new Intent(MainActivityMs.this, NewsDetails.class);
                Logger.e("22222222222");
                intent.putExtra("newsId", otherId);
                Logger.e("3333333333");
//                AppManager.getAppManager().toNextPage(intent,false);
                startActivity(intent);
                Logger.e("跳转新闻详情页面,newsId:"+otherId+",intent==null"+(intent==null));
                break;
            case "1":
                //跳转长期工作详情页面 带入参数
                intent = new Intent(context, NewsDetails.class);
                intent.putExtra("workId", otherId);
                Logger.e("跳转工作详情页面");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mainactivity2);
        ButterKnife.bind(this);
        /**
         * 设置当前activity不可滑动关闭页面
         */
        setThisActivityCanNotSwipeBackClosed();
        initView();
//        showDuide();
        V.d("测试当前登录用户的ID：" + new AppPreferences(context).getString(StaticParams.FileKey.__USERPID__, ""));
        connectRongIM();
        //设置融云推送监听
        setRongIMPushListener();
        System.gc();
        Glide.get(MainActivityMs.this).clearMemory();
        try {
            getParamFromHtml();
            V.e("页面从H5页面跳入，已将Id传给下个页面");
        } catch (Exception e) {
            V.e("页面不是从H5页面跳入，执行默认操作");
            V.e("异常信息："+e.toString());
        }

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                V.d("aaaa");
                AQQQ aqqq = new Gson().fromJson(uMessage.custom, AQQQ.class);

                if (aqqq != null && aqqq.getMessageType() != null
                        && !TextUtils.isEmpty(aqqq.getMessageType())) {
                    if (aqqq.getMessageType().equals("0")) {
                        Utils.showToast(getApplicationContext(), "您的账号在异地登录，请重新登录");
                        com.zwy.preferences.AppPreferences appPreferences = new com.zwy.preferences.AppPreferences(getApplicationContext());
                        appPreferences.put(StaticParams.FileKey.__LOCALTOKEN__, "");
//                        AppManager.getAppManager().AppExit(MainActivityMs.super.context);
                        finish();
                        startActivity(new Intent(context, LoginAcitvity.class));
                    }
                }

            }

//            @Override
//            public Notification getNotification(Context context, UMessage uMessage) {
//
//                switch (uMessage.builder_id) {
//                    case 1:
//
//                        return null;
//                    default:
//                        return super.getNotification(context, uMessage);
//
//                }
//
//            }


        };
        MyApplication.mPushAgent.setMessageHandler(messageHandler);
    }

    private void connectRongIM() {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                RongIM.connect(MyApplication.getAppPreferences().getString(StaticParams.FileKey.__RONGTOKEN__, ""), new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        Utils.showToast(context, "连接到聊天服务器失败");
                        V.e("连接到聊天服务器失败");
                    }

                    @Override
                    public void onSuccess(String s) {
                        //
                        V.d("连接到融云聊天服务器成功，s=" + s);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        //
                        Utils.showToast(context, "连接到聊天服务器失败");
                        V.e("连接到聊天服务器失败,errorCode=" + errorCode);
                    }
                });
            }
        });
    }

    private void setRongIMPushListener() {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                    @Override
                    public boolean onReceived(final io.rong.imlib.model.Message message, int i) {
                        V.d("接收到消息：" + message.getExtra() + "\n" + message.getObjectName() + "\n" + message.getSenderUserId()
                                + "\n" + message.getUId() + "\n" + "\n" +
                                message);
                        //根据UerId获取用户的昵称和头像
                        callList.add(LoveJob.getUserNameAndUserLogo(message.getSenderUserId(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                if (thePerfectGirl.getData().getUserInfoDTOs() == null || thePerfectGirl.getData().getUserInfoDTOs().size() == 0) {
                                    Utils.showToast(context, "获取用户资料失败");
                                    V.e("获取用户资料失败");
                                    return;
                                }
                                if (thePerfectGirl.getData().getUserInfoDTOs().get(0) == null) {
                                    push(message.getObjectName(), "", "");
                                    return;
                                }
                                push(message.getObjectName(), thePerfectGirl.getData().getUserInfoDTOs().get(0).getRealName(), StaticParams.QiNiuYunUrl + thePerfectGirl.getData().getUserInfoDTOs().get(0).getPortraitId());
                            }

                            @Override
                            public void onError(String msg) {
                                V.d("1");
                            }
                        }));
                        return false;
                    }
                });
            }
        });
    }

    private void push(String type, final String userName, final String userLogo) {

        String sendType = "";
        switch (type) {
            case "RC:TxtMsg":
                //文字消息
                sendType = "文字";
//                NiftyNotificationView.build(context, userName + "给你发送了一条语音消息", Effects.thumbSlider, R.id.mLyout)
//                        .setIcon(R.drawable.appicon)         //You must call this method if you use ThumbSlider effect
//                        .show();
                break;

            case "VcMsg":
                //语音消息
                sendType = "语音";
//                NiftyNotificationView.build(context, userName + "给你发送了一条语音消息", Effects.thumbSlider, R.id.mLyout)
//                        .setIcon(R.drawable.appicon)         //You must call this method if you use ThumbSlider effect
//                        .show();
                break;

            case "ImgMsg":
                //图片消息
                sendType = "图文";
//                NiftyNotificationView.build(context, userName + "给你发送了一张美图", Effects.thumbSlider, R.id.mLyout)
//                        .setIcon(R.drawable.appicon)         //You must call this method if you use ThumbSlider effect
//                        .show();
                break;

        }
        final String finalSendType = sendType;


        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(context).load(userLogo).asBitmap().into(120, 90).get();
                    final Drawable drawable;
                    if (bitmap != null) {
                        drawable = new BitmapDrawable(getResources(), bitmap);
                    } else {
                        drawable = getDrawable(R.drawable.ic_launcher);
                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            NiftyNotificationView.build(context, userName + "给你发送了一条" + finalSendType + "消息", Effects.thumbSlider, R.id.mLyout)
//                                    .setIcon(drawable)         //You must call this method if you use ThumbSlider effect
//                                    .show();
//                        }
//                    });
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            NiftyNotificationView.build(context, userName + "给你发送了一条" + finalSendType + "消息", Effects.thumbSlider, R.id.mLyout)
                                    .setIcon(drawable)         //You must call this method if you use ThumbSlider effect
                                    .show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    V.e("e:" + e);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    V.e("e1:" + e);
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            NiftyNotificationView.build(context, userName + "给你发送了一条" + finalSendType + "消息", Effects.thumbSlider, R.id.mLyout)
                                    .setIcon(R.drawable.ic_launcher)         //You must call this method if you use ThumbSlider effect
                                    .show();
                        }
                    });
                }
            }
        });
    }

    private void showDuide() {

        View ltTab1 = tabWidget.getChildTabViewAt(0);
        AppPreferences appPreferences = MyApplication.getAppPreferences();
//        if (!TextUtils.isEmpty(appPreferences.getString(StaticParams.FileKey.__ISSHOWGUIDEVIEW__, ""))) {
//            return;
//        }
        //显示引导
        GuideHelper guideHelper = new GuideHelper(context);
        //第一页 欢迎来到……
        View first = guideHelper.inflate(R.layout.giude_first);
        guideHelper.addPage(new GuideHelper.TipData(first, Gravity.CENTER));
        //第2页
        GuideHelper.TipData tipData2 = new GuideHelper.TipData(R.mipmap.spalsh_02, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData2.setLocation(60, -DisplayUtils.px2dip(context, 400));
        guideHelper.addPage(tipData2);

//        //第3页
//        GuideHelper.TipData tipData3 = new GuideHelper.TipData(R.mipmap.spalsh_07, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
//        tipData3.setLocation(30, -DisplayUtils.dipToPix(context, 340));
//        guideHelper.addPage(tipData3);
//
//
////        //第4页
////        GuideHelper.TipData tipData4 = new GuideHelper.TipData(R.mipmap.spalsh_08, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
////        tipData4.setLocation(30, -DisplayUtils.dipToPix(context, 240));
////        guideHelper.addPage(tipData4);
//
//        //第5页
//        GuideHelper.TipData tipData5 = new GuideHelper.TipData(R.mipmap.spalsh_03, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
//        tipData5.setLocation(238, -DisplayUtils.dipToPix(context, 100));
//        guideHelper.addPage(tipData5);
//
//        //第6页
//        GuideHelper.TipData tipData6 = new GuideHelper.TipData(R.mipmap.spalsh_04, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
//        tipData6.setLocation(250, -DisplayUtils.dipToPix(context, 100));
//        guideHelper.addPage(tipData6);
//
//        //第7页
//        GuideHelper.TipData tipData7 = new GuideHelper.TipData(R.mipmap.spalsh_06, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
//        tipData7.setLocation(275, -DisplayUtils.dipToPix(context, 100));
//        guideHelper.addPage(tipData7);
////
////        //第8页
////        GuideHelper.TipData tipData8 = new GuideHelper.TipData(R.mipmap.spalsh_05, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
////        tipData8.setLocation(400, -DisplayUtils.dipToPix(context, 90));
////        guideHelper.addPage(tipData8);

        guideHelper.show(false);
//        StaticParams.FileKey
        appPreferences.put(StaticParams.FileKey.__ISSHOWGUIDEVIEW__, "1");
    }

    private void setThisActivityCanNotSwipeBackClosed() {
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume_() {
        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onDestroy_() {

    }

    private void initView() {
        initTabs();
    }

    private void initTabs() {
        mTabHost.setup(this, this.getSupportFragmentManager(),
                R.id.realtabcontent);
        MainTab[] tabs = MainTab.values();
        for (int i = 0; i < tabs.length; i++) {
            MainTab tab = tabs[i];

            TabHost.TabSpec newTabSpec = mTabHost.newTabSpec(tab.getIdx() + "");
            View view = View.inflate(this, R.layout.tab, null);
            TextView tv = (TextView) view.findViewById(R.id.tab_title);
            tv.setText(getResources().getString(tab.getResName()));
            Drawable drawable = getResources().getDrawable(
                    tab.getResIcon());
            // setCompoundDrawablesWithIntrinsicBounds图标的宽高将会设置为固有宽高，
            tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
                    null, null);
            newTabSpec.setIndicator(view);

            mTabHost.addTab(newTabSpec, tab.getClz(), null);
        }

        tabWidget = mTabHost.getTabWidget();
        tabWidget.setDividerDrawable(null);
    }

    private long exitTime = 0;


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Utils.showToast(context, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        UMShareAPI.get (context).onActivityResult (requestCode, resultCode, data);
//        V.d("传值到f");
////        ((F_Job) fragments.get(1)).onActivityResult(requestCode, resultCode, data);
//    }

    int requestCode;
    int resultCode;
    Intent data;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        Intent intent = new Intent();
        intent.setAction("com.lovejob.onactivityresult");
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("resultCode", resultCode);
        if (data != null) {
            intent.putExtra("photos", data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS));
            intent.putExtra("isRefresh", data.getBooleanExtra("isRefresh", false));
        }
        context.sendBroadcast(intent);
        V.d("MainActivity接收到用户进入onActivityResult的回调，已发出广播");
    }

    public Object[] getRequestCodeAndData() {
        Object[] strings = new Object[]{this.requestCode, this.resultCode, this.data};
        return strings;
    }

}
