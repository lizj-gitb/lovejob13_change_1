package com.lovejob.view._userinfo.myinformation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.model.StaticParams;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Aty_Resume extends BaseActivity {
    @Bind(R.id.img_mygetallworklist_back)
    ImageView imgMygetallworklistBack;
    @Bind(R.id.rl_information)
    RelativeLayout rlInformation;
    @Bind(R.id.rl_show_works)
    RelativeLayout rlShowWorks;
    @Bind(R.id.rl_friend_talk)
    RelativeLayout rlFriendTalk;
    @Bind(R.id.rl_befor_comm)
    RelativeLayout rlBeforComm;
    Activity context;
    private String userType = "0";

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_personalresume);
        ButterKnife.bind(this);
        context = this;
        AppPreferences appPreferences = new AppPreferences(context);
        userType = appPreferences.getString(StaticParams.FileKey.__USERTYPE__, "");
        //TODO  功能未实现，暂时隐藏
        rlShowWorks.setVisibility(View.GONE);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }


    @OnClick({R.id.img_mygetallworklist_back, R.id.rl_information, R.id.rl_show_works, R.id.rl_friend_talk, R.id.rl_befor_comm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_mygetallworklist_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.rl_information:

                try {
                    if (userType.equals("0")) {
                        startActivity(new Intent(context, Aty_Information.class));
                    } else {
                        startActivity(new Intent(context, Aty_Company_Information.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showToast(context,"系统异常");
                    AppManager.getAppManager().finishActivity();
                }
                break;
            case R.id.rl_show_works:
                break;
            case R.id.rl_friend_talk:
                startActivity(new Intent(context, Aty_FriendTalk.class));
                break;
            case R.id.rl_befor_comm:
                startActivity(new Intent(context, Aty_HistoryComm.class));
                break;
        }
    }

}
