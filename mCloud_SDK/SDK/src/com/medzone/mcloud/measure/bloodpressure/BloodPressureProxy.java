package com.medzone.mcloud.measure.bloodpressure;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.BloodPressure;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.measure.proxy.AbstractMeasureProxy;
import com.medzone.mcloud.utils.TimeUtils;

public class BloodPressureProxy extends AbstractMeasureProxy<BloodPressure> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2665696190880006373L;

	public BloodPressureProxy(MCloudDevice device) {
		super(device);
	}

	@Override
	public BluetoothFragment getConnectedView(Bundle bundle) {
		BloodPressureConnectFragment fragment = new BloodPressureConnectFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public BluetoothFragment getMeasureView(Bundle bundle) {
		BloodPressureMeasureFragment fragment = new BloodPressureMeasureFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public BluetoothFragment getResultView(Bundle bundle) {
		BloodPressureResultFragment fragment = new BloodPressureResultFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Map<String, String> assembleResultData(BloodPressure t) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MCloudSdkMeasureDataArgs.VALUE_DIASTOLIC, t.getHigh() + "");
		map.put(MCloudSdkMeasureDataArgs.VALUE_SYSTOLIC, t.getLow() + "");
		map.put(MCloudSdkMeasureDataArgs.VALUE_HEART_RATE, t.getRate() + "");
		map.put(MCloudSdkMeasureDataArgs.MEASURE_TIME, TimeUtils.getTime(t.getMeasureTime() * 1000));
		map.put(MCloudSdkMeasureDataArgs.MEASURE_UID, t.getMeasureUID());
		return map;
	}

}
