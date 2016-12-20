package com.v.rapiddev.utils;

import android.util.Log;

import com.v.rapiddev.logger.Logger;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:RapidDevSample
 * Package_Name:com.v.rapiddev.utils
 * Created on 2016-11-24 10:26
 */

public final class V {
    private static final String TAG = "_________RapidDev:";
    public static boolean isDebug = false;

    public static void d(String msg) {
        if (isDebug) {
            Logger.d(msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) Logger.e(msg);
    }

    public static void w(String msg) {
        if (isDebug) Logger.w( msg);
    }

}
