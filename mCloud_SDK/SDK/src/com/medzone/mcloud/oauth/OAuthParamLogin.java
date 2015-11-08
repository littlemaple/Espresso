package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamLogin extends OAuthParamBase {

	public static final String	KEY_ENDUSERID	= "endUserId";

	@MCloudSdkApiParamTag(KEY_ENDUSERID)
	public String				endUserId;

	public static class CallBackParam {

		public static final String	KEY_ACCESS_TOKEN	= "access_token";
		public static final String	KEY_EXPIRE_IN		= "expire_in";

	}
}
