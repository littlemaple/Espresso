package com.medzone.mcloud.measure;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.medzone.mcloud.background.BluetoothMessage;
import com.medzone.mcloud.background.util.BluetoothUtils;
import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.logging.Log;

public class MeasureHelper {
	
	private Messenger				messenger;
	private Messenger				sendMessenger;
	ServiceConnection				conn									= new ServiceConnection() {
																				@Override
																				public void onServiceConnected(ComponentName name, IBinder binder) {
																					Log.d("robert", "bind onServiceConnected");
																					messenger = new Messenger(binder);
																					getStatus();
																				}

																				@Override
																				public void onServiceDisconnected(ComponentName arg0) {

																				}
																			};
	private CloudDevice				mDevice	= null;
	private Context                 mMeasureContext;
	private Handler 				handler;
	
	public MeasureHelper(Context measureContxt, CloudDevice device, Handler handler){
		mMeasureContext = measureContxt;
		mDevice = device;
		this.handler = handler;
	}
																			
	public void bind() {
		Log.d("robert", "bind");
		if (messenger != null) return;

		sendMessenger = new Messenger(handler);
		Intent intent = new Intent();
		intent.setClass(mMeasureContext, com.medzone.mcloud.background.MMeasureService.class);
		mMeasureContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	public void unbind() {
		Log.d("robert", "unbind");
		if (conn != null) {
			mMeasureContext.unbindService(conn);
			messenger = null;
			conn = null;
		}
	}

	public void search() {
		sendCommand(BluetoothMessage.msg_open, mDevice.getDeviceTagIntValue());
	}
	
	public void open(CloudDevice dev, String addr) {
		mDevice = dev;
		sendCommand(BluetoothMessage.msg_open, 1, addr);
	}

	public void query() {
		sendCommand(BluetoothMessage.msg_send, BluetoothUtils.QUERY_TERMAINAL);
	}

	public void measure() {
		sendCommand(BluetoothMessage.msg_send, BluetoothUtils.START_MEASURE);
	}

	public void record() {
		sendCommand(BluetoothMessage.msg_send, BluetoothUtils.RECORD);
	}

	public void cancelmeasure() {
		sendCommand(BluetoothMessage.msg_send, BluetoothUtils.PAUSE_MEASURE);
	}

	public void close() {
		sendCommand(BluetoothMessage.msg_close, 0);
	}

	public void getStatus() {
		sendCommand(BluetoothMessage.msg_get_status, 0);
	}

	private void sendCommand(int what, int cmd) {
		sendCommand(what, cmd, null);
	}
	
	public void sendCommand(int what, int cmd, Object param) {
		if( messenger == null)
			return;
		
		Message msg = Message.obtain(null, 0);
		msg.what = what;
		msg.arg1 = mDevice.getDeviceTagIntValue();
		msg.arg2 = cmd;
		msg.obj = param;
		msg.replyTo = sendMessenger;
		try {
			messenger.send(msg);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
