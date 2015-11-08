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
	 * ����һ��ָ�����ȵ�������ַ���
	 * 
	 * @param len
	 *            �����ַ����ĳ���
	 * @return ����ַ���
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
	 * ����У��
	 * ����֤�ӿڣ������ṩ����У���룬��������Ϊ signature��ͬʱ�����ṩ���������ʱ��� timestamp �Լ������ nonce
	 * �������У���롣
	 * ����ǰ�Ƚ������ύ����Ĳ����������������������·�ʽƴ�Ӻ���� MD5��
	 * MD5(appSecret&key1=value1&key2=value2)
	 * note: ƴ��ʱ���ݲ�Ҫ�����κα��봦��ƴ�Ӳ������� timestamp �� nonce���������� signature��
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
	 * Ĭ�ϻ�����еĲ�������У�飬������У�����ֶ�
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
				// У�����ֶ��
				if (TextUtils.isEmpty(t.timestamp)) t.timestamp = OAthUtils.getTimestamp();
				if (TextUtils.isEmpty(t.nonce)) t.nonce = OAthUtils.getRandomString(32);
			}
			// ��ö����е������ֶ�
			Field[] fields = t.getClass().getFields();
			for (Field field : fields) {
				MCloudSdkApiParamTag tag = field.getAnnotation(MCloudSdkApiParamTag.class);
				if (tag != null) {
					if (json == null) json = new JSONObject();
					try {
						// �����������
						Object dataContainer = field.get(t);
						// ת����JSONObject
						if (dataContainer != null) {
							Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("toJSONObject %s:%s", dataContainer.toString(), tag.value()));
							if (TextUtils.isEmpty(dataContainer.toString()) || TextUtils.isEmpty(tag.value()) || TextUtils.equals(OAuthParamBase.KEY_APPKEY, tag.value())) {
								// ������Ч�Ŀ��ֶΣ��������ֶ�У��
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
					// �����������
					Object dataContainer = field.get(t);
					// ת����JSONObject
					if (dataContainer != null) {
						Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("isReady %s:%s", dataContainer.toString(), tag.value()));

						final boolean canEmpty = tag.canEmpty();
						final String value = tag.value();
						// ����Ƿ�ע����Ƿ�Ϊ�գ���ʵ������Ƿ�һ��
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
