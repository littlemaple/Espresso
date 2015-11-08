package com.medzone.mcloud.oauth;

import org.json.JSONObject;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public abstract class OAuthParamBase implements IOAthParamValidity {

	public static final String	KEY_APPKEY		= "appKey";
	public static final String	KEY_APPID		= "appId";
	public static final String	KEY_TIMESTAMP	= "timestamp";
	public static final String	KEY_NONCE		= "nonce";
	public static final String	KEY_SIGNATURE	= "signature";

	@MCloudSdkApiParamTag(KEY_APPID)
	public String				appId;

	@MCloudSdkApiParamTag(KEY_APPKEY)
	public String				appKey;

	@MCloudSdkApiParamTag(value = KEY_TIMESTAMP, canEmpty = true)
	public String				timestamp;

	@MCloudSdkApiParamTag(value = KEY_NONCE, canEmpty = true)
	public String				nonce;

	@MCloudSdkApiParamTag(value = KEY_SIGNATURE, canEmpty = true)
	public String				signature;

	@Override
	public boolean isReady() {
		return OAthUtils.isReady(this);
	}

	public JSONObject toJSONObject() {
		return OAthUtils.toJSONObject(this, true);
	}

	public JSONObject toJSONObject(boolean isSign) {
		return OAthUtils.toJSONObject(this, isSign);
	}
}
