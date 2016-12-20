package com.lovejob.controllers.task;

import com.lovejob.model.ThePerfectGirl;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.controllers.task
 * Created on 2016-11-25 12:37
 */

public interface OnAllParameListener {
    void onSuccess(ThePerfectGirl thePerfectGirl);

    void onError(String msg);
}
