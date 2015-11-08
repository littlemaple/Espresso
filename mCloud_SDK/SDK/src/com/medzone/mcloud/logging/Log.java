package com.medzone.mcloud.logging;

public final class Log {

	public static boolean	isDevMode		= false;
	public static String	SDK_FRAMEWORK	= "SDK_FRAMEWORK";
	public static String	CORE_FRAMEWORK	= "CORE_FRAMEWORK";

	private Log() {
	}

	public static void v(String tag, String msg) {
		android.util.Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		android.util.Log.e(tag, msg);
	}

	public static void i(String tag, String msg) {
		android.util.Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		android.util.Log.w(tag, msg);
	}

	public static void d(String tag, String msg) {
		android.util.Log.d(tag, msg);
	}

	public static void v(String tag, String msg, Throwable e) {
		android.util.Log.d(tag, msg, e);
	}

	public static void e(String tag, String msg, Throwable e) {
		android.util.Log.e(tag, msg, e);
	}

	public static void i(String tag, String msg, Throwable e) {
		android.util.Log.e(tag, msg, e);
	}

	public static void w(String tag, String msg, Throwable e) {
		android.util.Log.e(tag, msg, e);
	}

	public static void d(String tag, String msg, Throwable e) {
		android.util.Log.e(tag, msg, e);
	}

}
