package com.lovejob.view._othersinfos;

import android.app.Activity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.view.MyViewPager;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/17.
 * 个人中心
 */

public class Others extends BaseActivity {
    @Bind(R.id.img_f_others_back)
    ImageView imgFOthersBack;
    @Bind(R.id.tv_f_others_working)
    TextView tvFOthersWorking;
    @Bind(R.id.img_f_others_logo)
    CircleImageView imgFOthersLogo;
    @Bind(R.id.img_f_my_sex)
    ImageView imgFMySex;
    @Bind(R.id.tv_f_others_name)
    TextView tvFOthersName;
    @Bind(R.id.img_f_others_level)
    ImageView imgFOthersLevel;
    @Bind(R.id.tv_f_others_posstion)
    TextView tvFOthersPosstion;
    @Bind(R.id.tv_f_others_address)
    TextView tvFOthersAddress;
    @Bind(R.id.tv_f_others_line1)
    View tvFOthersLine1;
    @Bind(R.id.tv_f_others_server)
    TextView tvFOthersServer;
    @Bind(R.id.rl_other_tab1)
    RelativeLayout rlOtherTab1;
    @Bind(R.id.tv_f_others_line2)
    View tvFOthersLine2;
    @Bind(R.id.tv_f_others_dynamic)
    TextView tvFOthersDynamic;
    @Bind(R.id.rl_other_tab2)
    RelativeLayout rlOtherTab2;
    @Bind(R.id.tv_f_others_line3)
    View tvFOthersLine3;
    @Bind(R.id.tv_f_others_jianli)
    TextView tvFOthersJianli;
    @Bind(R.id.rl_other_tab3)
    RelativeLayout rlOtherTab3;
    @Bind(R.id.others_fragment)
    MyViewPager othersFragment;
    @Bind(R.id.userbg)
    LinearLayout userbg;
    private ArrayList<Fragment> fragments;
    private ArrayList<View> views;
    private ArrayList<TextView> textViews;
    private MyAdpater adpater;
    Activity context;
    String userPid;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_othersinfos);
        ButterKnife.bind(this);
        context = this;
        userPid = getIntent().getStringExtra("userId");
        addData();
        initFragMent();
    }


    private void addData() {
        LoveJob.getUserHomeInfos(userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(final ThePerfectGirl thePerfectGirl) {
                tvFOthersWorking.setText(thePerfectGirl.getData().getUserInfoDTO().getJobState());
                tvFOthersAddress.setText(thePerfectGirl.getData().getUserInfoDTO().getAddress());
                tvFOthersName.setText(thePerfectGirl.getData().getUserInfoDTO().getRealName());
                tvFOthersPosstion.setText(thePerfectGirl.getData().getUserInfoDTO().getPosition());
                int userSex = thePerfectGirl.getData().getUserInfoDTO().getUserSex() == 1 ? R.mipmap.icon_male : R.mipmap.icon_famale;
                imgFMySex.setImageResource(userSex);
                Glide.with(context).load(StaticParams.ImageURL + thePerfectGirl.getData().getUserInfoDTO().getPortraitId()+"!logo").into(imgFOthersLogo);
                imgFOthersLevel.setImageResource(getUserLever(thePerfectGirl.getData().getUserInfoDTO().getLevel()));
                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = Glide.with(context).load(StaticParams.ImageURL + thePerfectGirl.getData().getUserInfoDTO().getBackground()+"!logo")     //设置头像
                                    .asBitmap().into(150, 100).get();
                            HandlerUtils.post(new Runnable() {
                                @Override
                                public void run() {
                                    userbg.setBackground(new BitmapDrawable(bitmap));          //设置背景
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    /**
     * 设置Vip等级
     * @param level  等级
     * @return
     */
    private int getUserLever(int level) {
        int resid = 0;
        switch (level) {
            case 1:
                resid = R.mipmap.icon_level_v1_42;
                break;

            case 2:
                resid = R.mipmap.icon_level_v1_13;
                break;

            case 3:
                resid = R.mipmap.icon_level_v1_86;
                break;

            case 4:
                resid = R.mipmap.icon_level_v1_57;
                break;

            case 5:
                resid = R.mipmap.icon_level_v1_12;
                break;

            default:
                resid = R.mipmap.icon_level_v1_42;
        }
        return resid;
    }

    /**
     * 初始化 Fragment
     */
    private void initFragMent() {
        fragments = new ArrayList<Fragment>();
        Fragment Tab_1 = new F_OtherServer();        //服务Fragment
        Fragment Tab_2 = new F_OtherDynamic();       //动态Fragment
        Fragment Tab_3 = new F_OtherResume();        //简历Fragment
        fragments.add(Tab_1);
        fragments.add(Tab_2);
        fragments.add(Tab_3);
        views = new ArrayList<View>();
        views.add(tvFOthersLine1);
        views.add(tvFOthersLine2);
        views.add(tvFOthersLine3);
        textViews = new ArrayList<TextView>();
        textViews.add(tvFOthersServer);
        textViews.add(tvFOthersDynamic);
        textViews.add(tvFOthersJianli);
        adpater = new MyAdpater(getSupportFragmentManager(), fragments);
        othersFragment.setAdapter(adpater);
        othersFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i = othersFragment.getCurrentItem();
                switch (i) {
                    case 0:
                        setSelect(0);
                        break;
                    case 1:
                        setSelect(1);
                        break;
                    case 2:
                        setSelect(2);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setSelect(int i) {
        //        switch (i){
//            case 0:
//                tvFOthersLine1.setBackground();
//                break;
//        }
        //TODO   滑动时下面的线条出不来
        for (int j = 0; j < textViews.size(); j++) {

            if (i != j) {
                textViews.get(j).setTextColor(Color.parseColor("#969696"));
            } else {
                textViews.get(j).setTextColor(Color.parseColor("#50b8f1"));
            }
        }
        othersFragment.setCurrentItem(i);

    }


    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }


    @OnClick({R.id.img_f_others_back, R.id.rl_other_tab1, R.id.rl_other_tab2, R.id.rl_other_tab3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_f_others_back:            //返回
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.rl_other_tab1:                 //服务Tab
                setSelect(0);
                break;
            case R.id.rl_other_tab2:                 //动态Tab
                setSelect(1);
                break;
            case R.id.rl_other_tab3:                  //简历Tab
                setSelect(2);
                break;
        }
    }
}
