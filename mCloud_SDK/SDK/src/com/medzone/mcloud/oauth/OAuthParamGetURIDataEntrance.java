package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamGetURIDataEntrance extends OAuthParamBase {

	public static final String	KEY_TYPE	= "type";

	@MCloudSdkApiParamTag(KEY_TYPE)
	public String				type;

	public static class CallBackParam {
		/**
		 * "url":"http://...完整入口地址，不包含 access_token 参数"
		 */
		public static final String	KEY_URL	= "url";
	}

}
