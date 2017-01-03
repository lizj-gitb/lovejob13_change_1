package com.lovejob.view._userinfo.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
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
    @Bind(R.id.tv_aboutme)
    TextView mTvAboutme;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_aboutme);
        ButterKnife.bind(this);
        LoveJob.getSystemVersion(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                String text = thePerfectGirl.getData().getAboutusDTO().getAbout_us();
                String[] s = text.split("\\|");
                for (int i = 0; i <s .length ; i++) {
                    mTvAboutme.append(s[i]);
                    mTvAboutme.append("\n");
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
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
