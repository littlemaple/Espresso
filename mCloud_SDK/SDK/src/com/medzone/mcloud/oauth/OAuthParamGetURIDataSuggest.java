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

		// * "url":"http://...Ƕ����ַ������ access_token"
		public static final String	KEY_URL		= "url";
		// "result":"����",
		public static final String	KEY_RESULT	= "result";
		// * "depart":"����",
		public static final String	KEY_DEPART	= "depart";
		// * "drug":"��ҩ",
		public static final String	KEY_DRUG	= "drug";
		// * "sport":"�˶�",
		public static final String	KEY_SPORT	= "sport";
		// * "food":"��ʳ",
		public static final String	KEY_FOOD	= "food";
		// * "psy":"����",
		public static final String	KEY_PSY		= "psy";
		// * "state":"�����ȼ�",
		public static final String	KEY_STATE	= "state";
	}

}
