package com.lovejob.model;

import android.view.View;

import java.util.Calendar;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.model
 * Created on 2016-11-30 00:51
 */

public abstract class MyOnClickListener implements View.OnClickListener{

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        //
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onclickListener(v);
        }
    }

    protected abstract void onclickListener(View v);
}
