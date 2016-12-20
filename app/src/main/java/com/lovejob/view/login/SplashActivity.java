package com.lovejob.view.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.CheckPermission;
import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.lovejob.MyApplication;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.ms.MainActivityMs;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

public class SplashActivity extends Activity {
    protected AppPreferences appPreferences = MyApplication.getAppPreferences();
    private Call authLocalToken, uploadCrachFile;

    @PermissionGranted()
    public void permissionGranted() {

        into();
    }

    @PermissionDenied()
    public void permissionDenied() {
        Toast.makeText(this, "为了更好的体验app请开启相关权限！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckAnnotatePermission
                .from(this, this)
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check();
    }

    private void into() {
        //        /**
//         * 隐藏标题
//         */
//        hiteTitleBar();
//        setContentView(R.layout.splashlayout);
        V.d("into splash activity...");
        if (TextUtils.isEmpty(appPreferences.getString(StaticParams.FileKey.__City__, ""))) {
            //开启定位
            V.d("start location...");
            MyApplication.mLocationClient.start();
        }
        /**
         * 检测是否有异常日志需要上送 有则上送
         */
        checkUpLoadLogs();
//        try {
        V.d("start sleep " + StaticParams.SPLASH_TIME + " ms");
//            Thread.sleep(StaticParams.SPLASH_TIME);
        V.d("sleep success,start activity.");

        String localToken = appPreferences.getString(StaticParams.FileKey.__LOCALTOKEN__, "");
        if (!TextUtils.isEmpty(localToken)) {
            V.d("start auth local token...");
            //免登录
            V.d("免登录");
            authLocalToken = LoveJob.authLocalToken(/**context ,防止内存泄漏*/appPreferences.getString(StaticParams.FileKey.__LOCALTOKEN__, ""), new OnAllParameListener() {
                @Override
                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                    V.d("auth local token  success,start connect rong service...");
                    startActivity(new Intent(SplashActivity.this, MainActivityMs.class));
                    AppManager.getAppManager().finishActivity(SplashActivity.this);
//                    connectRongYun(appPreferences.getString(StaticParams.FileKey.__RONGTOKEN__, ""));
                }

                @Override
                public void onError(String msg) {
                    Utils.showToast(SplashActivity.this, msg);
                    startActivity(new Intent(SplashActivity.this, LoginAcitvity.class));
                    AppManager.getAppManager().finishActivity(SplashActivity.this);
                    V.d("清空本地存储的localToken");
                    appPreferences.put(StaticParams.FileKey.__LOCALTOKEN__, "");
                }
            });
        } else {
            startActivity(new Intent().setClass(this, isFirstStartApp() ? WelcomeAcitvity.class : LoginAcitvity.class));
            AppManager.getAppManager().finishActivity(SplashActivity.this);
        }
    }

    private void checkUpLoadLogs() {
        LoveJob.upLoadExcption();
    }

    private void hiteTitleBar() {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        V.d("splash activity title was hite");
    }

    /**
     * 是否为第一次启动，是-返回true
     *
     * @return
     */
    private boolean isFirstStartApp() {
        return appPreferences.getBoolean(StaticParams.FileKey.__ISFIRSTSTARTAPP__, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        V.d("SplashActivity was destroyed");
        if (authLocalToken != null) {
            authLocalToken.cancel();
        }
        if (uploadCrachFile != null) uploadCrachFile.cancel();
        System.gc();
        System.runFinalization();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
