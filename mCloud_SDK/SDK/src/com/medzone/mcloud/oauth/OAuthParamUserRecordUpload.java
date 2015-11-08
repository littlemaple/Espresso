package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamUserRecordUpload extends OAuthParamBase {

	public static final String	KEY_TYPE	= "type";
	public static final String	KEY_UP_DATA	= "up_data";

	@MCloudSdkApiParamTag(KEY_TYPE)
	public String				type;
	@MCloudSdkApiParamTag(KEY_UP_DATA)
	public String				up_data;

	public static class CallBackParam {

		public static final String	KEY_TYPE	= "type";
		public static final String	KEY_UP		= "up";

	}
}
