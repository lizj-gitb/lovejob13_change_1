package com.v.rapiddev.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.v.rapiddev.R;

import java.util.LinkedList;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static LinkedList<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new LinkedList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.getLast();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.getLast();
        finishActivity(activity);
        System.gc();
        System.runFinalization();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack != null) {
                activityStack.remove(activity);
            }
            activity.finish();
            activity = null;
        }
        System.gc();
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
        System.gc();
        System.runFinalization();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        System.gc();
        System.runFinalization();
    }

    /**
     * 跳转下个页面
     *
     * @param activity
     * @param cls
     */
    public void toNextPage(Activity activity, Class<?> cls, boolean isClose) {
        activityStack.getLast().startActivity(new Intent(activityStack.getLast(), cls));
        if (isClose) finishActivity(activityStack.getLast());
        activityStack.getLast().overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
    }
    /**
     * 跳转下个页面
     *
     * @param activity
     * @param cls
     */
    public void toNextPage(Class<?> cls, boolean isClose) {
        activityStack.getLast().startActivity(new Intent(activityStack.getLast(), cls));
        if (isClose) finishActivity(activityStack.getLast());
        activityStack.getLast().overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
    }
    /**
     * 跳转下个页面
     *
     * @param activity
     * @param cls
     */
    public void toNextPage(Activity activity, Class<?> cls, int requestCode) {
        activityStack.getLast().startActivityForResult(new Intent(activityStack.getLast(), cls), requestCode);
        activityStack.getLast().overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
    }

    /**
     * 跳转下个页面
     */
    public void toNextPage(Intent intent, int requestCode) {
        activityStack.getLast().startActivityForResult(intent, requestCode);
        activityStack.getLast().overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
    }

    /**
     * 跳转下个页面
     */
    public void toNextPage(Intent intent, boolean isFinish) {
        activityStack.getLast().startActivity(intent);
        activityStack.getLast().overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
        if (isFinish) {
            finishActivity(activityStack.getLast());
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.gc();
            System.runFinalization();
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public int ActivityStackSize() {
        return activityStack == null ? 0 : activityStack.size();
    }
}