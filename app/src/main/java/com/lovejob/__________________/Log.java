package com.lovejob.__________________;

/**
 * 创建时间：2016-12-26 10:58
 * 创建人：赵文贇
 * 类描述：
 * 包名：lovejob11
 */

public class Log {
    public static final String TAG = "cxwl-lovejob";

    public static void d(String msg) {
        android.util.Log.d(TAG, "╔══════════════════════════════════════════════════════════════════════\n║" + msg + "\n╚══════════════════════════════════════════════════════════════════════");
    }

    public static void e(String msg) {
        android.util.Log.e(TAG, "╔══════════════════════════════════════════════════════════════════════\n║" + msg + "\n╚══════════════════════════════════════════════════════════════════════");
    }

}
