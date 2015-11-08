package com.medzone.mcloud.measure.bloodoxygen;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.BloodOxygen;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.measure.proxy.AbstractMeasureProxy;
import com.medzone.mcloud.utils.TimeUtils;

public class BloodOxygenProxy extends AbstractMeasureProxy<BloodOxygen> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8854734917509062186L;

	public BloodOxygenProxy(MCloudDevice device) {
		super(device);
	}

	@Override
	public BluetoothFragment getConnectedView(Bundle bundle) {
		BloodOxygenConnectFragment fragment = new BloodOxygenConnectFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public BluetoothFragment getMeasureView(Bundle bundle) {
		BloodOxygenMeasureFragment fragment = new BloodOxygenMeasureFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public BluetoothFragment getResultView(Bundle bundle) {
		BloodOxygenResultFragment fragment = new BloodOxygenResultFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Map<String, String> assembleResultData(BloodOxygen t) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MCloudSdkMeasureDataArgs.VALUE_SATURATION, t.getOxygen() + "");
		map.put(MCloudSdkMeasureDataArgs.VALUE_HEART_RATE, t.getRate() + "");
		map.put(MCloudSdkMeasureDataArgs.MEASURE_TIME, TimeUtils.getTime(t.getMeasureTime()*1000));
		map.put(MCloudSdkMeasureDataArgs.MEASURE_UID, t.getMeasureUID());
		return map;
	}

}
