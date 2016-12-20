package com.lovejob.view._userinfo.mywalletinfos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.view._userinfo.mywalletinfos.moneydetails.F_Record;
import com.lovejob.view._userinfo.mywalletinfos.moneydetails.F_State;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_MoneyDetails extends BaseActivity {
    @Bind(R.id.img_moneydetails_back)
    ImageView imgMoneydetailsBack;
    @Bind(R.id.tv_liushui)
    TextView tvLiushui;
    @Bind(R.id.rl_tab1)
    RelativeLayout rlTab1;
    @Bind(R.id.tv_zhuangtai)
    TextView tvZhuangtai;
    @Bind(R.id.rl_tab2)
    RelativeLayout rlTab2;
    @Bind(R.id.vp_money_details)
    ViewPager vpMoneyDetails;
    private ArrayList<Fragment> fragments;
    private MyAdpater mAdapter;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_moneydetails);
        ButterKnife.bind(this);
        initFragMent();
        getDefaultParam();
    }

    private void getDefaultParam() {
       boolean isSelectOrder= getIntent().getBooleanExtra("isSelectOrder",false);
        if (isSelectOrder){
            setSelect(1);
        }
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    private void initFragMent() {
        fragments = new ArrayList<Fragment>();
        Fragment Tab1 = new F_Record();
        Fragment Tab2 = new F_State();
        fragments.add(Tab1);
        fragments.add(Tab2);
        mAdapter = new MyAdpater(getSupportFragmentManager(),fragments);
        vpMoneyDetails.setAdapter(mAdapter);
        vpMoneyDetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = vpMoneyDetails.getCurrentItem();switch (currentItem) {
                    case 0:
                        setSelect(0);
                        break;
                    case 1:
                        setSelect(1);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int i) {

        switch (i){
            case 0:
                tvLiushui.setTextColor(getResources().getColor(R.color.actionbar));
                tvZhuangtai.setTextColor(getResources().getColor(R.color.hiteTextColor));
                break;
            case 1:
                tvLiushui.setTextColor(getResources().getColor(R.color.hiteTextColor));
                tvZhuangtai.setTextColor(getResources().getColor(R.color.actionbar));
                break;
        }
        vpMoneyDetails.setCurrentItem(i);
    }



    @OnClick({R.id.img_moneydetails_back, R.id.rl_tab1, R.id.rl_tab2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_moneydetails_back:
                finish();
                break;
            case R.id.rl_tab1:
                setSelect(0);
                break;
            case R.id.rl_tab2:
                setSelect(1);
                break;
        }
    }
}
