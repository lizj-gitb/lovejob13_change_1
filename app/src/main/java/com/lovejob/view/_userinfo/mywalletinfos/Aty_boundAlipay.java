package com.lovejob.view._userinfo.mywalletinfos;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/8.
 */
public class Aty_boundAlipay extends BaseActivity {
    @Bind(R.id.img_wallet_balabce_back)
    ImageView imgWalletBalabceBack;
    @Bind(R.id.bt_bound_alipay)
    Button btBoundAlipay;
    @Bind(R.id.et_boundalipay_account)
    EditText etBoundalipayAccount;
    @Bind(R.id.et_boundalipay_name)
    EditText etBoundalipayName;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_boundalipay);
        ButterKnife.bind(this);

    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }


    @OnClick({R.id.img_wallet_balabce_back, R.id.bt_bound_alipay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_wallet_balabce_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.bt_bound_alipay:
                dialog = Utils.showProgressDliago(context,"正在绑定");
                LoveJob.boundAlipay(etBoundalipayAccount.getText().toString(), etBoundalipayName.getText().toString(), "0", new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context,"您已经成功绑定支付宝");
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
