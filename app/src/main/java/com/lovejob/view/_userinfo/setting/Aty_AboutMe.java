package com.lovejob.view._userinfo.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.v.rapiddev.base.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_AboutMe extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_aboutme);
        ButterKnife.bind(this);

    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }



    @OnClick(R.id.back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
