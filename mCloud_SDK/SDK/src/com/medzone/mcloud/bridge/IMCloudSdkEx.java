package com.medzone.mcloud.bridge;

import android.content.Context;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.exception.MCloudInitException;

interface IMCloudSdkEx extends IMCloudSdk {

	/**
	 * ��ʼ��SDK�������ú�SDK���¼�������
	 * 
	 * @param appContext
	 *            Ӧ�ó���ApplicationContext
	 * @param sdkProxy
	 *            ʵ����{@link IMCloudSdkEx}��Լ��ʵ�ֽӿڵ�ʵ��
	 */
	void init(Context appContext, IMCloudSdk sdkProxy) throws MCloudInitException;

	/**
	 * �����Ƿ��Ѿ���ȷ��ʼ��SDK
	 * 
	 * @return boolean
	 */
	boolean isInit();

	/**
	 * ����Ƿ��Ѿ���Ȩ�ɹ�
	 * 
	 * @return
	 */
	boolean isOAuthorized();

	/**
	 * ��ʼ��ģ�飬����ǰ�Ѿ���Ȩ�ɹ�����������
	 * 
	 * @param context
	 */
	void doAutherized(MCloudSdkAuthorizedArgs eventArgs);

	/**
	 * ���ع����
	 */
	void downloadRules();

	/**
	 * ���ݸ����Ĳ���ģ�����ع����
	 * 
	 * @param device
	 */
	void downloadRuleInType(MCloudDevice device);

	/**
	 * ����ָ��ģ��Ĳ�������Ҫ��Ȩ�ɹ���
	 * 
	 * @param device
	 */
	void measure(MCloudDevice device);

	/**
	 * �����鿴ָ��ĳ������ģ�飬��Ҫ��Ȩ�ɹ���
	 * 
	 * @param device
	 */
	void view(MCloudDevice device);

	/**
	 * ��ȡ�鿴��ʷ������ַ��ʧ�ܷ��� null����Ҫ��Ȩ�ɹ���
	 * 
	 * @param device
	 */
	String obtainViewHistoryUrl(MCloudDevice device);

	/**
	 * �ͷ�SDK�ڲ����ⲿ�����ã�context��
	 */
	void uninit();
}
