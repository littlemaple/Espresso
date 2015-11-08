package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamGetURIDataSuggest extends OAuthParamBase {

	public static final String	KEY_TYPE		= "type";
	public static final String	KEY_RECORDID	= "recordid";
	public static final String	KEY_MEASUREUID	= "measureuid";

	@MCloudSdkApiParamTag(KEY_TYPE)
	public String				type;
	@MCloudSdkApiParamTag(KEY_RECORDID)
	public String				recordid;
	@MCloudSdkApiParamTag(KEY_MEASUREUID)
	public String				measureuid;

	public static class CallBackParam {

		// * "url":"http://...嵌入网址，不带 access_token"
		public static final String	KEY_URL		= "url";
		// "result":"结论",
		public static final String	KEY_RESULT	= "result";
		// * "depart":"科室",
		public static final String	KEY_DEPART	= "depart";
		// * "drug":"用药",
		public static final String	KEY_DRUG	= "drug";
		// * "sport":"运动",
		public static final String	KEY_SPORT	= "sport";
		// * "food":"饮食",
		public static final String	KEY_FOOD	= "food";
		// * "psy":"精神",
		public static final String	KEY_PSY		= "psy";
		// * "state":"健康等级",
		public static final String	KEY_STATE	= "state";
	}

}
