package com.medzone.mcloud.bridge;

import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;

public interface IMCloudSdk {

	/**
	 * ������Ȩ�ص��¼�
	 * 
	 * @param eventArgs
	 *            �ص�����
	 */
	void listenAutherizedState(MCloudSdkAuthorizedArgs args);

	/**
	 * ���������ݻص�
	 * 
	 * @param args
	 */
	void listenMeasureDataState(MCloudSdkMeasureDataArgs args);
}
