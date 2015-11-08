package com.medzone.mcloud.bridge;

import android.content.Context;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.exception.MCloudInitException;

public class BridgeProxy {

	private static BridgeProxy	instance;
	private IMCloudSdkEx		mCloudSdkEx	= InternalSdkImpl.getInstance();

	private BridgeProxy() {
	}

	public synchronized static BridgeProxy getInstance() {
		if (instance == null) instance = new BridgeProxy();
		return instance;
	}

	public void init(Context appContext, IMCloudSdk sdkProxy) throws MCloudInitException {
		mCloudSdkEx.init(appContext, sdkProxy);
	}

	public void doAutherized(MCloudSdkAuthorizedArgs eventArgs) {
		mCloudSdkEx.doAutherized(eventArgs);
	}

	public void measure(MCloudDevice device) {
		mCloudSdkEx.measure(device);
	}

	public void view(MCloudDevice device) {
		mCloudSdkEx.view(device);
	}

	public String obtainViewHistoryUrl(MCloudDevice device) {
		return mCloudSdkEx.obtainViewHistoryUrl(device);
	}

	public void uninit() {
		mCloudSdkEx.uninit();
	}

}
