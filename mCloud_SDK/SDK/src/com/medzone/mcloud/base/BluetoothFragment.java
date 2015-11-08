/**
 * 
 */
package com.medzone.mcloud.base;

import android.bluetooth.BluetoothDevice;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.measure.MeasureActivity;

/**
 * @author Robert.
 * 
 */
public abstract class BluetoothFragment extends Fragment {

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initActionBar();
	}

	protected void initActionBar() {

	}

	protected void sendStartDiscover(CloudDevice attachDevice) {
		((MeasureActivity) getActivity()).open();
	}

	protected void sendSelectedDevice(BluetoothDevice device) {

	}

	protected void sendStartMeasureBroadCast(CloudDevice attachDevice) {
		((MeasureActivity) getActivity()).query();
		((MeasureActivity) getActivity()).measure();
	}

	protected void sendPauseMeasure() {
		((MeasureActivity) getActivity()).cancelmeasure();
	}

	protected void sendStopMeasure() {
		((MeasureActivity) getActivity()).close();
	}

	/**
	 * @Role 测量过程中用户手动退出测量，需要已保存的数据
	 * @Current 用于血氧长期，胎心胎动等数据测量前已保存的场景
	 */
	public void onDeleteFromUser() {

	}

	public abstract void handleMessage(Message msg);

	public abstract void popBackStack();
}
