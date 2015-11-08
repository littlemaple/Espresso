package com.medzone.mcloud.measure.proxy;

import java.io.Serializable;
import java.util.Map;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.BaseMeasureData;
import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.bridge.InternalSdkImpl;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.measure.adapte.CloudDeviceAdapter;

public abstract class AbstractMeasureProxy<T extends BaseMeasureData> implements Serializable, IMeasureProxy<BluetoothFragment, T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private CloudDevice			attachDevice;

	public AbstractMeasureProxy(MCloudDevice device) {
		attachDevice = new CloudDeviceAdapter(device).getCloudMeasureDevice();
	}

	@Override
	public CloudDevice getMesureDevice() {
		return attachDevice;
	}

	public abstract Map<String, String> assembleResultData(T t);

	@Override
	public void onMeasureComplete(T t) {
		Map<String, String> map = assembleResultData(t);
		MCloudSdkMeasureDataArgs args = new MCloudSdkMeasureDataArgs();
		args.putAll(map);
		InternalSdkImpl.getInstance().listenMeasureDataState(args);
	}
}
