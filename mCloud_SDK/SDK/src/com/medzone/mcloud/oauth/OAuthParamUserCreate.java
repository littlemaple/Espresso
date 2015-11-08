package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamUserCreate extends OAuthParamBase {

	public static final String	KEY_ENDUSERID	= "endUserId";
	public static final String	KEY_PHONE		= "phone";
	public static final String	KEY_GENDER		= "gender";
	public static final String	KEY_BIRTHDAY	= "birthday";
	public static final String	KEY_USERNAME	= "username";
	public static final String	KEY_IDCODE		= "idcode";
	public static final String	KEY_EMAIL		= "email";
	public static final String	KEY_LOCATION	= "location";

	@MCloudSdkApiParamTag(KEY_ENDUSERID)
	public String				endUserId;
	@MCloudSdkApiParamTag(KEY_PHONE)
	public String				phone;
	@MCloudSdkApiParamTag(KEY_GENDER)
	public String				gender;
	@MCloudSdkApiParamTag(KEY_BIRTHDAY)
	public String				birthday;
	@MCloudSdkApiParamTag(KEY_USERNAME)
	public String				username;
	@MCloudSdkApiParamTag(KEY_IDCODE)
	public String				idcode;
	@MCloudSdkApiParamTag(KEY_EMAIL)
	public String				email;
	@MCloudSdkApiParamTag(KEY_LOCATION)
	public String				location;

	public static class CallBackParam {

		public static final String	KEY_ACCESS_TOKEN	= "access_token";
		public static final String	KEY_EXPIRE_IN		= "expire_in";

	}
}
