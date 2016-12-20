package com.lovejob.view._userinfo.mylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_MyList extends BaseActivity {
    @Bind(R.id.img_aty_mylist_back)
    ImageView imgAtyMylistBack;
    @Bind(R.id.myall)
    TextView myall;
    @Bind(R.id.lt_mylist_myBuyService)
    LinearLayout ltMylistMyBuyService;
    @Bind(R.id.tv_mylist_myBuyService_tobesure)
    TextView tvMylistMyBuyServiceTobesure;
    @Bind(R.id.lt_mylist_myBuyService_tobesure)
    LinearLayout ltMylistMyBuyServiceTobesure;
    @Bind(R.id.tv_mylist_myBuyService_tobecomm)
    TextView tvMylistMyBuyServiceTobecomm;
    @Bind(R.id.lt_mylist_myBuyService_tobecomm)
    LinearLayout ltMylistMyBuyServiceTobecomm;
    @Bind(R.id.myall_out)
    TextView myallOut;
    @Bind(R.id.lt_mylist_myBuyOutService)
    LinearLayout ltMylistMyBuyOutService;
    @Bind(R.id.tv_mylist_myBuyOutService_tobetrans)
    TextView tvMylistMyBuyOutServiceTobetrans;
    @Bind(R.id.lt_mylist_myBuyOutService_tobetrans)
    LinearLayout ltMylistMyBuyOutServiceTobetrans;
    @Bind(R.id.tv_mylist_myBuyOutService_tobecomm)
    TextView tvMylistMyBuyOutServiceTobecomm;
    @Bind(R.id.lt_mylist_myBuyOutService_tobecomm)
    LinearLayout ltMylistMyBuyOutServiceTobecomm;
    @Bind(R.id.myall_send)
    TextView myallSend;
    @Bind(R.id.lt_mylist_mysendwork)
    LinearLayout ltMylistMysendwork;
    @Bind(R.id.tv_mylist_mysendwork_tobeadmitted)
    TextView tvMylistMysendworkTobeadmitted;
    @Bind(R.id.lt_mylist_mysendwork_tobeadmitted)
    LinearLayout ltMylistMysendworkTobeadmitted;
    @Bind(R.id.tv_mylist_mysendwork_tobesure)
    TextView tvMylistMysendworkTobesure;
    @Bind(R.id.lt_mylist_mysendwork_tobesure)
    LinearLayout ltMylistMysendworkTobesure;
    @Bind(R.id.tv_mylist_mysendwork_tobecomm)
    TextView tvMylistMysendworkTobecomm;
    @Bind(R.id.lt_mylist_mysendwork_tobecomm)
    LinearLayout ltMylistMysendworkTobecomm;
    @Bind(R.id.myall_get)
    TextView myallGet;
    @Bind(R.id.lt_mylist_mygetwork)
    LinearLayout ltMylistMygetwork;
    @Bind(R.id.tv_mylist_mygetwork_alreaySign)
    TextView tvMylistMygetworkAlreaySign;
    @Bind(R.id.lt_mylist_mygetwork_alreaySign)
    LinearLayout ltMylistMygetworkAlreaySign;
    @Bind(R.id.tv_mylist_mygetwork_alreaySignIn)
    TextView tvMylistMygetworkAlreaySignIn;
    @Bind(R.id.lt_mylist_mygetwork_alreaySignIn)
    LinearLayout ltMylistMygetworkAlreaySignIn;
    @Bind(R.id.tv_mylist_mygetwork_tobecomm)
    TextView tvMylistMygetworkTobecomm;
    @Bind(R.id.lt_mylist_mygetwork_tobecomm)
    LinearLayout ltMylistMygetworkTobecomm;
    @Bind(R.id.sv)
    PullToRefreshScrollView sv;
    private Call call_getAllNumber;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mylist);
        ButterKnife.bind(this);
        setScrollListener();

    }

    private void setScrollListener() {
        sv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                setData();
                Utils.showToast(context, "刷新成功");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void setData() {
        call_getAllNumber = LoveJob.getAllNumber(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                sv.onRefreshComplete();
                tvMylistMysendworkTobeadmitted.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getAdmittedCount()));
                tvMylistMysendworkTobesure.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getConfirmedCount()));
                tvMylistMysendworkTobecomm.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getEvaluateCount()));
                tvMylistMyBuyOutServiceTobetrans.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getSoldConfirmedCount()));
                tvMylistMygetworkTobecomm.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getApplyEvaluateCount()));
                tvMylistMygetworkAlreaySign.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getSignUpCount()));
                tvMylistMygetworkAlreaySignIn.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getRecordedCount()));
                tvMylistMyBuyOutServiceTobecomm.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getSoldEvaluateCount()));
                tvMylistMyBuyServiceTobesure.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getBuyConfirmedCount()));
                tvMylistMyBuyServiceTobecomm.setText(String.valueOf(thePerfectGirl.getData().getWorkInfoDTO().getBuyEvaluateCount()));
            }

            @Override
            public void onError(String msg) {
                sv.onRefreshComplete();
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onResume_() {
        setData();
    }

    @Override
    public void onDestroy_() {
        if (call_getAllNumber != null && call_getAllNumber.isCanceled())
            call_getAllNumber.cancel();

    }


    @OnClick({R.id.img_aty_mylist_back, R.id.lt_mylist_myBuyService, R.id.lt_mylist_myBuyService_tobesure, R.id.lt_mylist_myBuyService_tobecomm, R.id.lt_mylist_myBuyOutService, R.id.lt_mylist_myBuyOutService_tobetrans, R.id.lt_mylist_myBuyOutService_tobecomm, R.id.lt_mylist_mysendwork, R.id.lt_mylist_mysendwork_tobeadmitted, R.id.lt_mylist_mysendwork_tobesure, R.id.lt_mylist_mysendwork_tobecomm, R.id.lt_mylist_mygetwork, R.id.lt_mylist_mygetwork_alreaySign, R.id.lt_mylist_mygetwork_alreaySignIn, R.id.lt_mylist_mygetwork_tobecomm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_aty_mylist_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.lt_mylist_myBuyService:
                // 我购买的全部服务";
                startActivity(new Intent(this, Aty_MyBuyServer_All.class));
                break;
            case R.id.lt_mylist_myBuyService_tobesure:
                //"我购买的服务-待确认";
                startActivity(new Intent(this, Aty_MySendServer_ToBeSure.class));
                break;
            case R.id.lt_mylist_myBuyService_tobecomm:
                //"我购买的服务-待评价";
                startActivity(new Intent(this, Aty_MySendServer_ToComm.class));
                break;
            case R.id.lt_mylist_myBuyOutService:
                //"我卖出的全部服务";
                startActivity(new Intent(this, Aty_MySellServer_All.class));
                break;
            case R.id.lt_mylist_myBuyOutService_tobetrans:
                //我卖出的服务-待交易";
                startActivity(new Intent(this, Aty_MySerever_WaitBuy.class));
                break;
            case R.id.lt_mylist_myBuyOutService_tobecomm:
                //"我卖出的服务-待评价";
                startActivity(new Intent(this, Aty_Myserver_ToComm.class));
                break;
            case R.id.lt_mylist_mysendwork:
                // "我发布的全部工作";
                startActivity(new Intent(this, Aty_MySendAllWorkList.class));
                break;
            case R.id.lt_mylist_mysendwork_tobeadmitted:
                //  "我发布的工作-待录取";
                startActivity(new Intent(this, Aty_MySendWork_Admitted.class));
                break;
            case R.id.lt_mylist_mysendwork_tobesure:
                startActivity(new Intent(this, Aty_MySendWork_ToBeSure.class));
                // "我发布的工作-待确认";
                break;
            case R.id.lt_mylist_mysendwork_tobecomm:
                startActivity(new Intent(this, Aty_MySendWork_ToComm.class));
                // 我发布的工作-待评价";
                break;
            case R.id.lt_mylist_mygetwork:
                // 我申请的全部工作";
                startActivity(new Intent(this, Aty_MyGetAllWorkList.class));
                break;
            case R.id.lt_mylist_mygetwork_alreaySign:
                // 我申请的工作-已报名";
                startActivity(new Intent(this, Aty_MyGetWork_AreadySignIn.class));
                break;
            case R.id.lt_mylist_mygetwork_alreaySignIn:
                // 我申请的工作-已录用";
                startActivity(new Intent(this, Aty_MyGetWork_AlreadyEmployment.class));
                break;
            case R.id.lt_mylist_mygetwork_tobecomm:
                //"我申请的工作-待评价"
                startActivity(new Intent(this, Aty_MyGetWork_AlreadyComm.class));
                break;
        }
    }
}
