package com.lovejob.view._userinfo.mywalletinfos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_Money extends BaseActivity {
    @Bind(R.id.img_wallet_balabce_back)
    ImageView imgWalletBalabceBack;
    @Bind(R.id.tv_money_balance)
    TextView tvMoneyBalance;
    @Bind(R.id.rl_wallet_balance_next)
    RelativeLayout rlWalletBalanceNext;
    @Bind(R.id.tv_money_count)
    TextView tvMoneyCount;
    @Bind(R.id.rla_wallet_balance_lingpai)
    RelativeLayout rlaWalletBalanceLingpai;
    @Bind(R.id.bt_wallet_balance_commit)
    Button btWalletBalanceCommit;
    @Bind(R.id.rla_wallet_alipay)
    RelativeLayout rlaWalletAlipay;
    @Bind(R.id.tv_add_alipay)
    TextView tvAddAlipay;
    private Call call_getBalance;
    private double money;

    @Override
    public void onCreate_(Bundle savedInstanceState) {
        setContentView(R.layout.aty_wallet_balance);
        ButterKnife.bind(this);
    }

    private void addData() {
        call_getBalance = LoveJob.getBalance(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null) {
                    money = thePerfectGirl.getData().getAmount();
                    tvMoneyBalance.setText(thePerfectGirl.getData().getAmount() + "元");
                    tvMoneyCount.setText(thePerfectGirl.getData().getTokenCount() + "枚");
                    if (!TextUtils.isEmpty(thePerfectGirl.getData().getAccount())) {
                        tvAddAlipay.setTextColor(getResources().getColor(R.color.hiteTextColor));
                        tvAddAlipay.setText(thePerfectGirl.getData().getAccount());
                        rlaWalletAlipay.setEnabled(false);
                    }else {
                        rlaWalletAlipay.setEnabled(true);
                    }
                }

            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onResume_() {
        addData();
    }

    @Override
    public void onDestroy_() {
        if (call_getBalance != null && call_getBalance.isCanceled())
            call_getBalance.cancel();
    }


    @OnClick({R.id.img_wallet_balabce_back, R.id.rl_wallet_balance_next, R.id.rla_wallet_balance_lingpai, R.id.bt_wallet_balance_commit, R.id.rla_wallet_alipay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_wallet_balabce_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.rl_wallet_balance_next:
                startActivity(new Intent(this, Aty_MoneyDetails.class));
                break;
            case R.id.rla_wallet_balance_lingpai:
                startActivity(new Intent(this, Aty_WalletToken.class));
                break;
            case R.id.bt_wallet_balance_commit:
                if (tvAddAlipay.getText().toString().equals("添加")) {
                    Utils.showToast(context, "请先绑定支付宝");
                } else {
                    Intent intent = new Intent(this, Aty_MoneyCash.class);
                    intent.putExtra("money", String.valueOf(money));
                    intent.putExtra("account", tvAddAlipay.getText().toString());
                    startActivity(intent);
                }

                break;
            case R.id.rla_wallet_alipay:
                startActivity(new Intent(this, Aty_boundAlipay.class));
                break;
        }
    }
}
