package com.lovejob.model;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.modles.Utils
 * Created on 2016-11-21 20:05
 */

public class TimerUtil {
    private int time = 60;

    private Timer timer;

    private TextView btnSure;

    private String btnText;

    public TimerUtil(TextView btnSure, String btnText) {
        super();
        this.btnSure = btnSure;
        btnSure.setTextSize(12);
        this.btnText = btnText;
    }

    public void cancle() {
        timer.cancel();
        btnSure = null;
        btnText = null;
        timer = null;
    }


    public void RunTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                time--;
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };


        timer.schedule(task, 100, 1000);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        btnSure.setEnabled(false);
                        btnSure.setText(time + "秒后重新发送");
                        btnSure.setTextSize(14);
                    } else {
                        timer.cancel();
                        btnSure.setText(btnText);
                        btnSure.setEnabled(true);
                        btnSure.setTextSize(14);
                        btnSure = null;
                        btnText = null;
                        timer = null;
                    }

                    break;


                default:
                    break;
            }

        }

        ;
    };


}
