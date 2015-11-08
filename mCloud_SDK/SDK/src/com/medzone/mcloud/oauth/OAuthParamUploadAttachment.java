package com.medzone.mcloud.oauth;

import com.medzone.mcloud.annotation.MCloudSdkApiParamTag;

public class OAuthParamUploadAttachment extends OAuthParamBase {

	public static final String	KEY_FILENAME	= "filename";
	public static final String	KEY_SIZE		= "size";

	/**
	 * "ÎÄ¼þÃû£º{type}-{recordid|measureuid}-{sizeX}.{ext}",
	 */
	@MCloudSdkApiParamTag(KEY_FILENAME)
	public String				filename;
	@MCloudSdkApiParamTag(KEY_SIZE)
	public String				size;

	public static class CallBackParam {

		public static final String	KEY_URL			= "url";
		public static final String	KEY_METHOD		= "method";
		public static final String	KEY_OFF			= "off";
		public static final String	KEY_LEN			= "len";
		public static final String	KEY_FINISHED	= "finished";

	}
}
