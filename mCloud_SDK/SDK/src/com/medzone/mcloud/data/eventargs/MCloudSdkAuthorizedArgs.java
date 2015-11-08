package com.medzone.mcloud.data.eventargs;

import java.util.Map;

/**
 * ������Ȩ����������������Ĳ���
 * 
 * @author Robert
 * 
 */
public class MCloudSdkAuthorizedArgs extends MCloudSdkEventArgs {

	// APP ����ӿ�
	// �����������ʱ�������� APP �����ṩ������Ϣ��
	//
	// appId: xxxx �ַ��������� 10-20��������Ϊ�����������ṩ
	// appSecret: xxxxx �ַ��������� 20-40��������Ϊ�����������ṩ
	// (array) fnGetAccountInfo() ��ȡ��ǰ�û���Ϣ�Ļص����������ع�ϣ�����ֶΣ�Ӧ�����������ݣ�
	//
	// uid required �ʺ�Ψһʶ���룬�ַ��������� 40�ֽ�
	// phone required �û��ֻ���
	// gender �û��Ա���ֵΪ����/Ů
	// birthday �û��������ڣ���ʽΪ��YYYY-MM-DD
	// username �û������ַ��������� 20�ֽ�
	// idcode ���֤��
	// email ��������
	// location ���ڵ����������� 40�ֽ�
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
