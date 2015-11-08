package com.medzone.mcloud.measure.proxy;

import android.os.Bundle;

import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.CloudDevice;

public interface IMeasureProxy<V extends BluetoothFragment, T> {

	public V getConnectedView(Bundle bundle);

	public V getMeasureView(Bundle bundle);

	public V getResultView(Bundle bundle);

	public void onMeasureComplete(T t);

	public CloudDevice getMesureDevice();
}
