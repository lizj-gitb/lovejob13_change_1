package com.lovejob.view.payinfoviews;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.interfaces
 * Created on 2016-10-29 15:24
 */

public interface OnPayListener {
    void onSuccess();

    void onError(String errorMsg);
}
