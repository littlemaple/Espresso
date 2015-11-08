package com.medzone.mcloud.measure.adapte;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.base.bean.CloudDevice;

public class CloudDeviceAdapter extends MeasureDeviceWrapper<MCloudDevice> {

	private MCloudDevice	device;

	public CloudDeviceAdapter(MCloudDevice t) {
		super(t);
		this.device = t;
	}

	@Override
	public String setupCommunicateMode() {
		return CloudDevice.BLUETOOTH_DEVICE;
	}

	@Override
	public String setupDeviceTag() {
		return device == MCloudDevice.BP ? CloudDevice.mCloud_P : CloudDevice.mCloud_O;
	}

}
