package com.lovejob.view._userinfo.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;
import com.zwy.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_SystemEdition extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_now_system)
    TextView tvNowSystem;
    @Bind(R.id.tv_new_system)
    TextView tvNewSystem;
    @Bind(R.id.system_update)
    RelativeLayout systemUpdate;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_systemedition);
        ButterKnife.bind(this);
        AddData();
    }

    private void AddData() {
        V.d("banbenhao:"+Utils.getAppVersionName(context));
//        Utils.getAppVersionName(context);
        //TODO 添加版本信息
        //如果用户是最新版本，则不显示更新按钮，只显示：您目前已是最新版本无需更新。
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.back, R.id.system_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.system_update:

                break;
        }
    }
}
