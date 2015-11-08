package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamGetURIDataRule extends OAuthParamBase {

	public static final String	KEY_TYPE	= "type";

	@MCloudSdkApiParamTag(KEY_TYPE)
	public String				type;

}
