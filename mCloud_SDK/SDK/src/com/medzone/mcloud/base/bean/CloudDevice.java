package com.medzone.mcloud.base.bean;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;

import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.utils.PreferencesUtils;

/**
 * @category 心云测量设备
 */
public class CloudDevice implements Serializable {

	private static final String				TAG					= CloudDevice.class.getSimpleName();

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 6619061243633510740L;

	public final static String				BLUETOOTH_DEVICE	= "bluetooth_device";
	public final static String				AUDIO_DEVICE		= "audio_device";

	public final static String				mCloud_P			= "mCloud-P";
	public final static String				mCloud_O			= "mCloud-O";
	public final static String				mCloud_S			= "mCloud-S";
	public final static String				mCloud_ET			= "mCloud-ET";
	public final static String				mCloud_FH			= "mCloud-FH";
	public final static String				mCloud_ECG			= "mCloud_ECG";

	private static HashMap<String, Integer>	mDeviceDict			= new HashMap<String, Integer>();

	static {
		mDeviceDict.put(mCloud_P, 1);
		mDeviceDict.put(mCloud_O, 2);
		mDeviceDict.put(mCloud_ET, 3);
		mDeviceDict.put(mCloud_S, 4);
		mDeviceDict.put(mCloud_FH, 5);
		mDeviceDict.put(mCloud_ECG, 6);
	}

	private String							deviceCommWay;
	private String							deviceTag;

	public CloudDevice() {

	}

	public CloudDevice(String category, String type) {
		this.deviceCommWay = category;
		this.deviceTag = type;
	}

	public String getDeviceCommWay() {
		return deviceCommWay;
	}

	public void setDeviceCommWay(String deviceCommWay) {
		this.deviceCommWay = deviceCommWay;
	}

	public String getDeviceTag() {
		return deviceTag;
	}

	public void setDeviceTag(String deviceTag) {
		this.deviceTag = deviceTag;
	}

	public int getDeviceTagIntValue() {
		if (deviceTag != null) {
			return mDeviceDict.get(deviceTag);
		}
		Log.w(TAG, "Please ensure device instance is inited.");
		return -1;
	}

	public String getPreferencesDeviceAddress(Context context) {
		if (context != null && getDeviceCommWay() == BLUETOOTH_DEVICE) {
			return PreferencesUtils.getString(context, getDeviceTag(), ":");
		}
		return null;
	}

	public void savePreferencesDeviceAddress(Context context, String mDevAddress) {
		if (context != null && mDevAddress != null && getDeviceCommWay() == BLUETOOTH_DEVICE) {
			PreferencesUtils.putString(context, getDeviceTag(), mDevAddress);
		}
	}

}
