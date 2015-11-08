package com.medzone.mcloud.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class TaskUtil {
	// use for cases that calling system intent like camera.
	public static boolean isMoveTaskToBack(Context context, Intent intent) {
		boolean flag;
		if (intent.getComponent() == null) {
			flag = true;
		}
		else {
			String s = intent.getComponent().getPackageName();
			String s1 = context.getPackageName();
			if (!s.equals(s1)) flag = true;
			else
				flag = false;
		}
		return flag;
	}

	public static String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}

	public static String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public static boolean isTopActivity(Activity activity) {
		ActivityManager activityManager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
		// 必须拥有android.permission.GET_TASKS权限，否则抛出异常
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (activity.getComponentName().equals(tasksInfo.get(0).topActivity)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRunningForeground(Context context) {
		String packageName = context.getPackageName();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		// 必须拥有android.permission.GET_TASKS权限，否则抛出异常
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (tasksInfo.get(0).topActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isProcessRunning(Context context) {
		Args.notNull(context, "context");
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(getPackageName(context)) && info.baseActivity.getPackageName().equals(getPackageName(context))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isApplicationBroughtToBackground(Context cxt) {
		String packageName = cxt.getPackageName();
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am == null ? null : am.getRunningTasks(1);
		if (tasks != null && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!TextUtils.equals(packageName, topActivity.getPackageName())) {
				return true;
			}
		}
		List<ActivityManager.RunningAppProcessInfo> process = am == null ? null : am.getRunningAppProcesses();
		if (process != null && !process.isEmpty()) {
			for (ActivityManager.RunningAppProcessInfo info : process) {
				if (android.os.Process.myPid() == info.pid) {
					return info.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
				}
			}
		}
		return false;
	}

	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == pid) {
				return procInfo.processName;
			}
		}
		return null;
	}
}
