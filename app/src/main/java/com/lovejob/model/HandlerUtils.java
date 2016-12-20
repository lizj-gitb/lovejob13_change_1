package com.lovejob.model;


import com.lovejob.MyApplication;

import static com.lovejob.MyApplication.getHandler;

/**
 * Created by Administrator on 2016/4/21.
 */
public class HandlerUtils {
	/**
	 * 执行一个延迟的任务
	 * @param task
	 */
	public static void postTaskDelay(Runnable task, long delayTime) {
		getHandler().postDelayed(task, delayTime);
	}

	public static void removeCallbacks(Runnable task) {
		getHandler().removeCallbacks(task);
	}

	/**
	 * 安全的执行一个任务
	 */
	public static void post(Runnable task) {
		if (MyApplication.isMainThread()) {
			task.run();
		} else {
			MyApplication.getHandler().post(task);
		}
	}

}
