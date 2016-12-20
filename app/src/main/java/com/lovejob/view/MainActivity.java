package com.lovejob.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.DisplayUtils;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.lovejob.view._home.F_Home;
import com.lovejob.view._job.F_Job;
import com.lovejob.view._money.F_Money;
import com.lovejob.view._userinfo.F_UserInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.guide.GuideHelper;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.notifactioninfo.Effects;
import com.v.rapiddev.notifactioninfo.NiftyNotificationView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.swipebacklayout.SwipeBackLayout;
import com.v.rapiddev.utils.V;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * ClassType: 展示5个fragment
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view
 * Created on 2016-11-21 18:20
 */

public class MainActivity extends BaseActivity {
    private static boolean isExit = false;
    //    Handler mHandler = new Handler () {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage (msg);
//            isExit = false;
//        }
//    };
    @Bind(R.id.vp_mainacitivity)
    ViewPager vpMainacitivity;
    @Bind(R.id.img_mainaty_home)
    ImageView imgMainatyHome;
    @Bind(R.id.tv_mainaty_home)
    TextView tvMainatyHome;
    @Bind(R.id.lt_tab1)
    LinearLayout ltTab1;
    @Bind(R.id.img_mainaty_job)
    ImageView imgMainatyJob;
    @Bind(R.id.tv_mainaty_job)
    TextView tvMainatyJob;
    @Bind(R.id.lt_tab2)
    LinearLayout ltTab2;
    @Bind(R.id.img_mainaty_money)
    ImageView imgMainatyMoney;
    @Bind(R.id.tv_mainaty_money)
    TextView tvMainatyMoney;
    @Bind(R.id.lt_tab3)
    LinearLayout ltTab3;
    @Bind(R.id.img_mainaty_work)
    ImageView imgMainatyWork;
    @Bind(R.id.tv_mainaty_work)
    TextView tvMainatyWork;
    @Bind(R.id.lt_tab4)
    LinearLayout ltTab4;
    @Bind(R.id.img_mainaty_user)
    ImageView imgMainatyUser;
    @Bind(R.id.tv_mainaty_user)
    TextView tvMainatyUser;
    @Bind(R.id.lt_tab5)
    LinearLayout ltTab5;
    @Bind(R.id.tv_drag)
    DragPointView tvDrag;
    private ArrayList<Fragment> fragments;
    private MyAdpater mAdapter;
    private ArrayList<ImageView> imageviews;
    private ArrayList<TextView> textviews;
    private String city;
    private Call call_getUserNameAndUserLogo;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mainactivity);
        ButterKnife.bind(this);
        /**
         * 设置当前activity不可滑动关闭页面
         */
        setThisActivityCanNotSwipeBackClosed();
//        //给mainActivity的city赋值后其他fragment直接调用
//        AppPreferences appPreferences = new AppPreferences(context);
//        appPreferences = null;
        /**
         * 设置vp默认加载一个页面
         */
//        vpMainacitivity.setOffscreenPageLimit(1);
        vpMainacitivity.setOffscreenPageLimit(4);
        initView();
        showDuide();

        //设置融云推送监听
        setRongIMPushListener();
//        ThreadPoolUtils.getInstance ().addTask (new Runnable () {
//            @Override
//            public void run() {
//                Glide.get(MainActivity.this).clearDiskCache();
//            }
//        });
    }

    private void setRongIMPushListener() {
        RongIM.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(final Message message, int i) {
                V.d("接收到消息：" + message.getExtra() + "\n" + message.getObjectName() + "\n" + message.getSenderUserId()
                        + "\n" + message.getUId() + "\n" + "\n" +
                        message);
                //根据UerId获取用户的昵称和头像
                call_getUserNameAndUserLogo = LoveJob.getUserNameAndUserLogo(message.getSenderUserId(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        if (thePerfectGirl.getData().getUserInfoDTOs() == null || thePerfectGirl.getData().getUserInfoDTOs().size() == 0) {
                            Utils.showToast(context, "获取用户资料失败");
                            V.e("获取用户资料失败");
                            return;
                        }
                        push(message.getObjectName(), thePerfectGirl.getData().getUserInfoDTOs().get(0).getRealName(), StaticParams.QiNiuYunUrl + thePerfectGirl.getData().getUserInfoDTOs().get(0).getPortraitId());
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
                callList.add(call_getUserNameAndUserLogo);
                return false;
            }
        });


    }

    private void push(String type, final String userName, final String userLogo) {

        String sendType = "";
        switch (type) {
            case "RC:TxtMsg":
                //文字消息
                sendType = "文字";

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
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(context).load(userLogo).asBitmap().into(120, 90).get();

                    final Drawable drawable = new BitmapDrawable(getResources(), bitmap);
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
                }
            }
        });
    }

    private void showDuide() {
//        AppPreferences appPreferences = MyApplication.getAppPreferences ();
//        if (!TextUtils.isEmpty (appPreferences.getString (StaticParams.FileKey.__ISSHOWGUIDEVIEW__, ""))) {
//            return;
//        }
        //显示引导
        GuideHelper guideHelper = new GuideHelper(context);
        //第一页
        View first = guideHelper.inflate(R.layout.giude_first);
        guideHelper.addPage(new GuideHelper.TipData(first, Gravity.CENTER));
        //第2页
        GuideHelper.TipData tipData2 = new GuideHelper.TipData(R.mipmap.spalsh_02, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData2.setLocation(40, -DisplayUtils.dipToPix(context, 90));
        guideHelper.addPage(tipData2);

        //第3页
        GuideHelper.TipData tipData3 = new GuideHelper.TipData(R.mipmap.spalsh_07, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData3.setLocation(30, -DisplayUtils.dipToPix(context, 325));
        guideHelper.addPage(tipData3);


//        //第4页
//        GuideHelper.TipData tipData4 = new GuideHelper.TipData(R.mipmap.spalsh_08, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
//        tipData4.setLocation(30, -DisplayUtils.dipToPix(context, 240));
//        guideHelper.addPage(tipData4);

        //第5页
        GuideHelper.TipData tipData5 = new GuideHelper.TipData(R.mipmap.spalsh_03, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData5.setLocation(180, -DisplayUtils.dipToPix(context, 90));
        guideHelper.addPage(tipData5);

        //第6页
        GuideHelper.TipData tipData6 = new GuideHelper.TipData(R.mipmap.spalsh_04, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData6.setLocation(160, -DisplayUtils.dipToPix(context, 90));
        guideHelper.addPage(tipData6);

        //第7页
        GuideHelper.TipData tipData7 = new GuideHelper.TipData(R.mipmap.spalsh_05, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData7.setLocation(220, -DisplayUtils.dipToPix(context, 90));
        guideHelper.addPage(tipData7);

        //第8页
        GuideHelper.TipData tipData8 = new GuideHelper.TipData(R.mipmap.spalsh_06, Gravity.LEFT | Gravity.BOTTOM, ltTab1);
        tipData8.setLocation(290, -DisplayUtils.dipToPix(context, 90));
        guideHelper.addPage(tipData8);

        guideHelper.show(false);
//        StaticParams.FileKey
//        appPreferences.put (StaticParams.FileKey.__ISSHOWGUIDEVIEW__, "1");
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

        fragments = new ArrayList<Fragment>();
        Fragment mTab_01 = new F_Home();
        Fragment mTab_02 = new F_Job();
        Fragment mTab_03 = new F_Money();
//        Fragment mTab_04 = new F_Friend();
        Fragment mTab_05 = new F_UserInfo();

        fragments.add(mTab_01);
        fragments.add(mTab_02);
        fragments.add(mTab_03);
//        fragments.add(mTab_04);
        fragments.add(mTab_05);

        imageviews = new ArrayList<ImageView>();
        imageviews.add(imgMainatyHome);
        imageviews.add(imgMainatyJob);
        imageviews.add(imgMainatyMoney);
//        imageviews.add(imgMainatyWork);
        imageviews.add(imgMainatyUser);

        textviews = new ArrayList<TextView>();
        textviews.add(tvMainatyHome);
        textviews.add(tvMainatyJob);
        textviews.add(tvMainatyMoney);
//        textviews.add(tvMainatyWork);
        textviews.add(tvMainatyUser);

        mAdapter = new MyAdpater(getSupportFragmentManager(), fragments);
        vpMainacitivity.setAdapter(mAdapter);
        //设置滑动监听器int currentItem
        vpMainacitivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //滑动时 改变图标状态
            @Override
            public void onPageSelected(int position) {
                int currentItem = vpMainacitivity.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        setSelect(0);
                        break;
                    case 1:
                        setSelect(1);
                        break;
                    case 2:
                        setSelect(2);
                        break;
                    case 3:
                        setSelect(3);
//                        setSelect(4);
                        break;
                    case 4:
//                        setSelect(4);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void exit() {
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
//            // 利用handler延迟发送更改状态信息
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            finish();
////            System.exit(0);
//            AppManager.getAppManager().AppExit(this);
//        }
//    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.lt_tab1, R.id.lt_tab2, R.id.lt_tab3, R.id.lt_tab4, R.id.lt_tab5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lt_tab1:
                setSelect(0);
                break;
            case R.id.lt_tab2:
                setSelect(1);
                break;
            case R.id.lt_tab3:
                setSelect(2);
                break;
            case R.id.lt_tab4:
                setSelect(3);
                break;
            case R.id.lt_tab5:
                setSelect(3);
//                setSelect(4);
                break;
        }
    }

    private void setSelect(int i) {
        int resouceId = 0;
        switch (i) {
            case 0:
                resouceId = R.mipmap.toolbar_home_on;
                imageviews.get(1).setImageResource(R.mipmap.toolbar_job_common);
                imageviews.get(2).setImageResource(R.mipmap.toolbar_money_common);
//                imageviews.get(3).setImageResource(R.mipmap.toolbar_work_common);
                imageviews.get(3).setImageResource(R.mipmap.toolbar_user_common);
                break;
            case 1:
                resouceId = R.mipmap.toolbar_job_on;
                imageviews.get(0).setImageResource(R.mipmap.toolbar_home_common);
                imageviews.get(2).setImageResource(R.mipmap.toolbar_money_common);
//                imageviews.get(3).setImageResource(R.mipmap.toolbar_work_common);
                imageviews.get(3).setImageResource(R.mipmap.toolbar_user_common);
                break;
            case 2:
                resouceId = R.mipmap.toolbar_money_on;
                imageviews.get(0).setImageResource(R.mipmap.toolbar_home_common);
                imageviews.get(1).setImageResource(R.mipmap.toolbar_job_common);
//                imageviews.get(3).setImageResource(R.mipmap.toolbar_work_common);
                imageviews.get(3).setImageResource(R.mipmap.toolbar_user_common);
                break;
            case 3:

                resouceId = R.mipmap.toolbar_user_on;
                imageviews.get(1).setImageResource(R.mipmap.toolbar_job_common);
                imageviews.get(2).setImageResource(R.mipmap.toolbar_money_common);
//                imageviews.get(3).setImageResource(R.mipmap.toolbar_work_common);
                imageviews.get(0).setImageResource(R.mipmap.toolbar_home_common);
//                resouceId = R.mipmap.toolbar_work_on;
//                imageviews.get(1).setImageResource(R.mipmap.toolbar_job_common);
//                imageviews.get(2).setImageResource(R.mipmap.toolbar_money_common);
//                imageviews.get(0).setImageResource(R.mipmap.toolbar_home_common);
//                imageviews.get(4).setImageResource(R.mipmap.toolbar_user_common);
                break;
            case 4:
                resouceId = R.mipmap.toolbar_user_on;
                imageviews.get(1).setImageResource(R.mipmap.toolbar_job_common);
                imageviews.get(2).setImageResource(R.mipmap.toolbar_money_common);
                imageviews.get(3).setImageResource(R.mipmap.toolbar_work_common);
                imageviews.get(0).setImageResource(R.mipmap.toolbar_home_common);
                break;
        }
        for (int ss = 0; ss < textviews.size(); ss++) {
            if (i != ss) {
                textviews.get(ss).setTextColor(Color.parseColor("#b3b7b9"));
            } else {
                textviews.get(ss).setTextColor(Color.parseColor("#00a0e9"));
            }
        }
        imageviews.get(i).setImageResource(resouceId);
        vpMainacitivity.setCurrentItem(i);
    }

    public String getCity() {
        return new AppPreferences(context).getString(StaticParams.FileKey.__City__, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
//        V.d("传值到f");
//        ((F_Job) fragments.get(1)).onActivityResult(requestCode, resultCode, data);
    }
}
