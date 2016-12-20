package com.lovejob.view._money;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.view._money.fragments_sendmoney.SendOriWork;
import com.lovejob.view._money.fragments_sendmoney.SendpakWork;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view._money
 * Created on 2016-11-22 02:49
 */

public class Aty_SendMoneyWork extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.tv_aty_send_money_work_slector_1_text)
    TextView tvAtySendMoneyWorkSlector1Text;
    @Bind(R.id.view_aty_send_money_work_slector_1_view)
    View viewAtySendMoneyWorkSlector1View;
    @Bind(R.id.lt_aty_send_money_work_slector_1)
    RelativeLayout ltAtySendMoneyWorkSlector1;
    @Bind(R.id.tv_aty_send_money_work_slector_2_text)
    TextView tvAtySendMoneyWorkSlector2Text;
    @Bind(R.id.view_aty_send_money_work_slector_2_view)
    View viewAtySendMoneyWorkSlector2View;
    @Bind(R.id.lt_aty_send_money_work_slector_2)
    RelativeLayout ltAtySendMoneyWorkSlector2;
    @Bind(R.id.fl_aty_send_money_work)
    FrameLayout flAtySendMoneyWork;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.sendmoneywork);
        ButterKnife.bind(this);
        V.d("进入发布赚点现钱和兼职工作的页面");
        setActionbar();
        initView();
    }

    private void initView() {
        fragments.add(new SendOriWork());
        fragments.add(new SendpakWork());

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_aty_send_money_work, fragments.get(0)).commit();
    }

    private void setActionbar() {
        actionbarTitle.setText("创意工作");
        actionbarTitle.setTextSize(16);
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarSave.setText("发布");
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.actionbar_back, R.id.lt_aty_send_money_work_slector_1, R.id.lt_aty_send_money_work_slector_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.lt_aty_send_money_work_slector_1:
                selector(1);
                break;
            case R.id.lt_aty_send_money_work_slector_2:
                selector(2);
                break;
        }
    }


    private void selector(int i) {
        switch (i) {
            case 1:
                tvAtySendMoneyWorkSlector1Text.setTextColor(getResources().getColor(R.color.actionbar));
                viewAtySendMoneyWorkSlector1View.setVisibility(View.VISIBLE);

                tvAtySendMoneyWorkSlector2Text.setTextColor(getResources().getColor(R.color.defaultTextColor));
                viewAtySendMoneyWorkSlector2View.setVisibility(View.INVISIBLE);

                if (!fragments.get(0).isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_aty_send_money_work, fragments.get(0)).commit();
                }
                break;
            case 2:
                tvAtySendMoneyWorkSlector1Text.setTextColor(getResources().getColor(R.color.defaultTextColor));
                viewAtySendMoneyWorkSlector1View.setVisibility(View.INVISIBLE);

                tvAtySendMoneyWorkSlector2Text.setTextColor(getResources().getColor(R.color.actionbar));
                viewAtySendMoneyWorkSlector2View.setVisibility(View.VISIBLE);

                if (!fragments.get(1).isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_aty_send_money_work, fragments.get(1)).commit();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragments.get(0).isVisible()) {
            ((SendOriWork) fragments.get(0)).onActivityResult(requestCode, resultCode, data);
        } else if (fragments.get(1).isVisible()) {
            ((SendpakWork) fragments.get(1)).onActivityResult(requestCode, resultCode, data);
        }
    }
}
