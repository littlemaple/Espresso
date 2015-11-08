package com.medzone.mcloud.data.eventargs;

import java.util.Map;

/**
 * 心云授权进行所需输入输出的参数
 * 
 * @author Robert
 * 
 */
public class MCloudSdkAuthorizedArgs extends MCloudSdkEventArgs {

	// APP 组件接口
	// 接入心云组件时，第三方 APP 必须提供以下信息：
	//
	// appId: xxxx 字符串，长度 10-20，由心云为合作方分配提供
	// appSecret: xxxxx 字符串，长度 20-40，由心云为合作方分配提供
	// (array) fnGetAccountInfo() 获取当前用户信息的回调函数，返回哈希数组字段，应包含以下内容：
	//
	// uid required 帐号唯一识别码，字符串不超过 40字节
	// phone required 用户手机号
	// gender 用户性别，其值为：男/女
	// birthday 用户出生日期，格式为：YYYY-MM-DD
	// username 用户名，字符串不超过 20字节
	// idcode 身份证号
	// email 电子邮箱
	// location 所在地区，不超过 40字节
	//
	/**
	 * 
	 */
	private static final long	serialVersionUID			= -7934583305297894365L;

	public static final String	Key_AuthorizedState				= "authorizedState";
	public static final String	Key_AuthorizedMessage			= "authorizedMessage";
	public static final String	Key_ExpireIn					= "expireIn";

	public static final String	Key_RealName					= "realName";
	public static final String	Key_IdNumber					= "idNumber";
	public static final String	Key_Email						= "email";
	public static final String	Key_PhoneNum					= "phoneNum";
	public static final String	Key_AppId						= "consumerKey";
	public static final String	Key_AppKey						= "consumerSecret";
	public static final String	Key_EndUserPrimaryId			= "endUserPrimaryId";
	public static final String	Key_EndUserAccessToken			= "endUserAccessToken";
	public static final String	Key_EndUserAccessTokenSecret	= "endUserAccessTokenSecret";

	public MCloudSdkAuthorizedArgs() {
	}

	public MCloudSdkAuthorizedArgs(Map<String, String> eventArgsMap) {
		super(eventArgsMap);
	}

	public String getAuthorizedstate() {
		return getValue(Key_AuthorizedState, "N");
	}

	public String getAuthorizedMessage() {
		return getValue(Key_AuthorizedMessage, "");
	}

	public String getExpireIn() {
		return getValue(Key_ExpireIn, "");
	}

	public String getMobile() {
		return getValue(Key_PhoneNum, "");
	}

	public String getRealName() {
		return getValue(Key_RealName, "");
	}

	public String getIdNumber() {
		return getValue(Key_IdNumber, "");
	}

	public String getEmail() {
		return getValue(Key_Email, "");
	}

	public String getAppId() {
		return getValue(Key_AppId, "");
	}

	public String getAppKey() {
		return getValue(Key_AppKey, "");
	}

	public String getEndUserPrimaryId() {
		return getValue(Key_EndUserPrimaryId, "");
	}

	public String getEndUserAccessToken() {
		return getValue(Key_EndUserAccessToken, "");
	}

	public String getEndUserAccessTokenSecret() {
		return getValue(Key_EndUserAccessTokenSecret, "");
	}

}
