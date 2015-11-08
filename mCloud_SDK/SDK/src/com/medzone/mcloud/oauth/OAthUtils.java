package com.medzone.mcloud.oauth;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;
import com.medzone.mcloud.utils.MD5Util;

class OAthUtils {

	private static final StringBuffer	LETTER_SEQUENCE	= new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

	public static String getTimestamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 产生一个指定长度的随机数字符串
	 * 
	 * @param len
	 *            产生字符串的长度
	 * @return 随机字符串
	 */
	public static String getRandomString(int len) {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = LETTER_SEQUENCE.length();
		for (int i = 0; i < len; i++) {
			sb.append(LETTER_SEQUENCE.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 请求校验
	 * 非认证接口，均需提供数据校验码，参数名称为 signature，同时还需提供产生请求的时间戳 timestamp 以及随机码 nonce
	 * 参与计算校验码。
	 * 计算前先将所有提交请求的参数名称正向排序，再以如下方式拼接后计算 MD5：
	 * MD5(appSecret&key1=value1&key2=value2)
	 * note: 拼接时数据不要进行任何编码处理，拼接参数包含 timestamp 和 nonce，但不包含 signature。
	 * 
	 * @param appSecret
	 * @param json
	 * @return
	 */
	public static JSONObject sign(String appSecret, JSONObject json) {

		List<String> list = new ArrayList<String>();
		Iterator<?> it = json.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			list.add(key);
		}
		Collections.sort(list);

		final String and = "&";
		final String equal = "=";
		StringBuilder builder = new StringBuilder();
		builder.append(appSecret);
		try {
			for (String key : list) {
				final String value = json.getString(key);
				builder.append(and);
				builder.append(key);
				builder.append(equal);
				builder.append(value);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		String ret = builder.toString();
		String signature = MD5Util.encrypt(ret);
		try {
			json.put(OAuthParamBase.KEY_SIGNATURE, signature);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		com.medzone.mcloud.logging.Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("signed :", json.toString()));
		return json;
	}

	/**
	 * 默认会对所有的参数进行校验，并生成校验码字段
	 * 
	 * @author Robert
	 * @param t
	 * @return
	 */
	public static <T extends OAuthParamBase> JSONObject toJSONObject(T t) {
		return toJSONObject(t, true);
	}

	public static <T extends OAuthParamBase> JSONObject toJSONObject(T t, boolean isSign) {

		JSONObject json = null;
		if (t != null) {

			if (isSign) {
				// 校验码字段填补
				if (TextUtils.isEmpty(t.timestamp)) t.timestamp = OAthUtils.getTimestamp();
				if (TextUtils.isEmpty(t.nonce)) t.nonce = OAthUtils.getRandomString(32);
			}
			// 获得对象中的所有字段
			Field[] fields = t.getClass().getFields();
			for (Field field : fields) {
				MCloudSdkApiParamTag tag = field.getAnnotation(MCloudSdkApiParamTag.class);
				if (tag != null) {
					if (json == null) json = new JSONObject();
					try {
						// 获得数据载体
						Object dataContainer = field.get(t);
						// 转换成JSONObject
						if (dataContainer != null) {
							Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("toJSONObject %s:%s", dataContainer.toString(), tag.value()));
							if (TextUtils.isEmpty(dataContainer.toString()) || TextUtils.isEmpty(tag.value()) || TextUtils.equals(OAuthParamBase.KEY_APPKEY, tag.value())) {
								// 过滤无效的空字段，不参与字段校验
								continue;
							}
							json.put(dataContainer.toString(), tag.value());
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (isSign) {
			return OAthUtils.sign(t.appKey, json);
		}
		return json;
	}

	public static <T extends OAuthParamBase> boolean isReady(T t) {

		Field[] fields = t.getClass().getFields();
		for (Field field : fields) {
			MCloudSdkApiParamTag tag = field.getAnnotation(MCloudSdkApiParamTag.class);
			if (tag != null) {
				try {
					// 获得数据载体
					Object dataContainer = field.get(t);
					// 转换成JSONObject
					if (dataContainer != null) {
						Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("isReady %s:%s", dataContainer.toString(), tag.value()));

						final boolean canEmpty = tag.canEmpty();
						final String value = tag.value();
						// 检查是否注解的是否为空，与实际情况是否一致
						if (!canEmpty) {
							if (TextUtils.isEmpty(value)) {
								return false;
							}
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
