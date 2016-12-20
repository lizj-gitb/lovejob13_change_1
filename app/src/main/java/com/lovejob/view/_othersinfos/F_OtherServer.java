package com.lovejob.view._othersinfos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Feels;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Free;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Skill;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.view.MyViewPager;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Feels;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Free;
import com.lovejob.view._userinfo.myserver.otherserver.F_ServiveWindow_Skill;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/17.
 */

public class F_OtherServer extends BaseFragment {
    View view;
    @Bind(R.id.img_service_window_back)
    ImageView imgServiceWindowBack;
    @Bind(R.id.rlbiaotilan)
    RelativeLayout rlbiaotilan;
    @Bind(R.id.img_seraty_jineng)
    ImageView imgSeratyJineng;
    @Bind(R.id.tv_seraty_jineng)
    TextView tvSeratyJineng;
    @Bind(R.id.lt_tab1)
    LinearLayout ltTab1;
    @Bind(R.id.img_seraty_qinghuai)
    ImageView imgSeratyQinghuai;
    @Bind(R.id.tv_seraty_qinghuai)
    TextView tvSeratyQinghuai;
    @Bind(R.id.lt_tab2)
    LinearLayout ltTab2;
    @Bind(R.id.img_seraty_mianfei)
    ImageView imgSeratyMianfei;
    @Bind(R.id.tv_seraty_mianfei)
    TextView tvSeratyMianfei;
    @Bind(R.id.lt_tab3)
    LinearLayout ltTab3;
    @Bind(R.id.line_seraty_jineng)
    TextView lineSeratyJineng;
    @Bind(R.id.line_seraty_qinghuai)
    TextView lineSeratyQinghuai;
    @Bind(R.id.line_seraty_mianfei)
    TextView lineSeratyMianfei;
    @Bind(R.id.vp_serviceacitivity)
    ViewPager vpServiceacitivity;
    private ArrayList<Fragment> fragments;
    private MyAdpater mAdapter;
    private ArrayList<ImageView> imageviews;
    private ArrayList<TextView> textviews;
    private ArrayList<TextView> lineviews;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aty_servicewindow, null);
        ButterKnife.bind(this, view);
        rlbiaotilan.setVisibility(View.GONE);
        initMainView();
        return view;
    }

    private void initMainView() {
        fragments = new ArrayList<Fragment>();
        Fragment sTab_01 = new F_ServiveWindow_Skill();
        Fragment sTab_02 = new F_ServiveWindow_Feels();
        Fragment sTab_03 = new F_ServiveWindow_Free();
        fragments.add(sTab_01);
        fragments.add(sTab_02);
        fragments.add(sTab_03);
        imageviews = new ArrayList<ImageView>();
        imageviews.add(imgSeratyJineng);
        imageviews.add(imgSeratyQinghuai);
        imageviews.add(imgSeratyMianfei);
        textviews = new ArrayList<TextView>();
        textviews.add(tvSeratyJineng);
        textviews.add(tvSeratyQinghuai);
        textviews.add(tvSeratyMianfei);
        lineviews = new ArrayList<TextView>();
        lineviews.add(lineSeratyJineng);
        lineviews.add(lineSeratyQinghuai);
        lineviews.add(lineSeratyMianfei);
        mAdapter = new MyAdpater(getActivity().getSupportFragmentManager(), fragments);
        vpServiceacitivity.setAdapter(mAdapter);
        vpServiceacitivity.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = vpServiceacitivity.getCurrentItem();
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
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int i) {
        int resouceId = 0;
        switch (i) {
            case 0:
                resouceId = R.mipmap.icon_skill_service_on;
                imageviews.get(1).setImageResource(R.mipmap.icon_feeling_service_off);
                imageviews.get(2).setImageResource(R.mipmap.icon_free_service_off);
                break;
            case 1:
                resouceId = R.mipmap.icon_feeling_service_on;
                imageviews.get(2).setImageResource(R.mipmap.icon_free_service_off);
                imageviews.get(0).setImageResource(R.mipmap.icon_skill_service_off);
                break;
            case 2:
                resouceId = R.mipmap.icon_free_service_on;
                imageviews.get(0).setImageResource(R.mipmap.icon_skill_service_off);
                imageviews.get(1).setImageResource(R.mipmap.icon_feeling_service_off);
                break;
        }
        for (int ss = 0; ss < textviews.size(); ss++) {
            if (i != ss) {
                textviews.get(ss).setTextColor(Color.parseColor("#969696"));
            } else {
                textviews.get(ss).setTextColor(Color.parseColor("#50b8f1"));
            }
        }
        for (int s = 0; s < lineviews.size(); s++) {
            if (i != s) {
                lineviews.get(s).setBackgroundColor(Color.parseColor("#969696"));

            } else {
                lineviews.get(s).setBackgroundColor(Color.parseColor("#50b8f1"));
            }
        }
        imageviews.get(i).setImageResource(resouceId);
        vpServiceacitivity.setCurrentItem(i);
    }

    @Override
    public void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }




    @OnClick({R.id.lt_tab1, R.id.lt_tab2, R.id.lt_tab3})
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
        }
    }
}
