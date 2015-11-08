package com.medzone.mcloud.measure.adapte;

import com.medzone.mcloud.base.bean.CloudDevice;

public abstract class MeasureDeviceWrapper<T> implements IDeviceStandard<T> {

	private T	t;

	public MeasureDeviceWrapper(T t) {
		this.t = t;
	}

	@Override
	public CloudDevice getCloudMeasureDevice() {
		return createDevice(t);
	}

	protected CloudDevice createDevice(T t) {
		CloudDevice device = new CloudDevice();
		device.setDeviceCommWay(setupCommunicateMode());
		device.setDeviceTag(setupDeviceTag());
		return device;
	}

	/**
	 * {@link CloudDevice.BLUETOOTH_DEVICE} <br>
	 * {@link CloudDevice.AUDIO_DEVICE}
	 * 
	 * @return
	 */
	public abstract String setupCommunicateMode();

	/**
	 * @see CloudDevice.mCloud_Ps
	 * @see CloudDevice.mCloud_O
	 * @return
	 */
	public abstract String setupDeviceTag();
}
