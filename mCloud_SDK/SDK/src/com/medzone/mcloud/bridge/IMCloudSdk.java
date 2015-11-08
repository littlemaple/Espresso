package com.medzone.mcloud.bridge;

import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;

public interface IMCloudSdk {

	/**
	 * 监听鉴权回调事件
	 * 
	 * @param eventArgs
	 *            回调参数
	 */
	void listenAutherizedState(MCloudSdkAuthorizedArgs args);

	/**
	 * 监测测量数据回调
	 * 
	 * @param args
	 */
	void listenMeasureDataState(MCloudSdkMeasureDataArgs args);
}
