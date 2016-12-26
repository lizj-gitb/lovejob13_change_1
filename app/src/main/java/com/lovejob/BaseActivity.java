package com.lovejob;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.base
 * Created on 2016-11-21 10:44
 */

public abstract class BaseActivity extends SwipeBackActivity {
    public Activity context;
    public MaterialDialog dialog;
    protected List<Call> callList = new ArrayList<> ();

    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);
        context = this;
        setRequestedOrientation(ActivityInfo
//                .SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        //setRequestedOrientation(ActivityInfo
        .SCREEN_ORIENTATION_PORTRAIT);//竖屏
        onCreate_(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResume_();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
        System.gc();
        System.runFinalization();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }
    @Override
    protected void onDestroy() {
        for (Call call: callList){
            if(call!=null)
                call.cancel ();
        }
        super.onDestroy();
        onDestroy_();
        AppManager.getAppManager().finishActivity(this);
    }

    public abstract void onCreate_(@Nullable Bundle savedInstanceState);

    public abstract void onResume_();

    public abstract void onDestroy_();


}
