package com.medzone.mcloud.utils;

import java.util.Locale;

import android.text.TextUtils;

public class LocaleUtil {

	public static boolean isEn() {

		if (TextUtils.equals(Locale.getDefault().toString(), Locale.CHINA.toString()) || TextUtils.equals(Locale.getDefault().toString(), Locale.CHINESE.toString())) {
			return true;
		}
		return true;
	}
}
