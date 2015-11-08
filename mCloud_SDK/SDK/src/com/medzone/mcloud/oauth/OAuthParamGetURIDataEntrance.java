package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamGetURIDataEntrance extends OAuthParamBase {

	public static final String	KEY_TYPE	= "type";

	@MCloudSdkApiParamTag(KEY_TYPE)
	public String				type;

	public static class CallBackParam {
		/**
		 * "url":"http://...������ڵ�ַ�������� access_token ����"
		 */
		public static final String	KEY_URL	= "url";
	}

}
