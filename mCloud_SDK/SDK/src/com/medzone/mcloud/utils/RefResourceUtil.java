package com.medzone.mcloud.utils;

import android.content.Context;

public class RefResourceUtil {

	public static int getLayoutId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "layout", context.getPackageName());
	}

	public static int getStringId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "string", context.getPackageName());
	}

	public static int getDrawableId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "drawable", context.getPackageName());
	}

	public static int getStyleId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "style", context.getPackageName());
	}

	public static int getId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "id", context.getPackageName());
	}

	public static int getColorId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "color", context.getPackageName());
	}

	public static int getArrayId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "array", context.getPackageName());
	}

	public static int getDimenId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "dimen", context.getPackageName());
	}
	public static int getAnimId(Context context, String attribute) {
		return context.getResources().getIdentifier(attribute, "anim", context.getPackageName());
	}
}
