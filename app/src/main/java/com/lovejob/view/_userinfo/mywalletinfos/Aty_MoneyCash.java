package com.lovejob.view._userinfo.mywalletinfos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/8.
 */
public class Aty_MoneyCash extends BaseActivity {
    @Bind(R.id.img_wallet_balabce_back)
    ImageView imgWalletBalabceBack;
    @Bind(R.id.tv_amount)
    EditText tvAmount;
    @Bind(R.id.bt_confirm)
    Button btConfirm;
    String account,money;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_moneycash);
        ButterKnife.bind(this);
        account = getIntent().getStringExtra("account");
        money = getIntent().getStringExtra("money");
        tvAmount.setText(money);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }



    @OnClick({R.id.img_wallet_balabce_back, R.id.bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_wallet_balabce_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.bt_confirm:
                Double a= Double.valueOf(String.valueOf(tvAmount.getText()));
                Double b = Double.valueOf(money);

                if (a>b){
                    Utils.showToast(context,"提现金额大于账户余额");
                    return;
                }
                if(a<1){
                    Utils.showToast(context,"提现金额不能小于1元");
                    return;
                }
                dialog = Utils.showProgressDliago(context,"正在提现，请稍后");
                LoveJob.Withdrawals(tvAmount.getText().toString(), account, "0", new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context,"提现已成功，正在受理");
                        AppManager.getAppManager().finishActivity();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context,msg);
                    }
                });
                break;
        }

    }

}
