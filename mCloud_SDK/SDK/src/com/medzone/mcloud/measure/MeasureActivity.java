package com.medzone.mcloud.measure;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.background.BluetoothMessage;
import com.medzone.mcloud.base.BaseActivity;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.BaseMeasureData;
import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.bridge.InternalSdkImpl;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.dialog.ErrorDialog;
import com.medzone.mcloud.dialog.ErrorDialog.ErrorDialogListener;
import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.measure.proxy.AbstractMeasureProxy;
import com.medzone.mcloud.utils.RefResourceUtil;

/**
 * @TODO has leaked IntentReceiver com.medzone.cloud.measure.fetalheart.
 *       FetalHeartMeasureFragment$CatchShutDownReceiver
 * @category 数据测量容器
 */
public class MeasureActivity extends BaseActivity {

	protected static final String	TAG			= "MeasureActivity";

	// -----------------------------Container 存储配置-----------------------
	private HashMap<String, Object>	configMap	= new HashMap<String, Object>();

	/**
	 * 返回对应的配置结果，如果配置项返回Null，则对应请使用默认配置保证流程正确执行。
	 * 
	 * @param confKey
	 *            配置名
	 * @param defValue
	 *            默认配置值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConfigurationValue(String confKey, T defValue) {
		if (configMap.containsKey(confKey)) {
			return (T) configMap.get(confKey);
		}
		return defValue;
	}

	/**
	 * 你必须确保配置的值是符合约定的，否则在{@link #getConfigurationValue(String, Object)}
	 * 时将会抛出类型不能转化的异常。
	 * 
	 * @param confKey
	 * @param confValue
	 */
	public void setConfigurationValue(String confKey, Object confValue) {
		configMap.put(confKey, confValue);
	}

	// ----------------------------------------------------------------

	// 测量模式，设备测量或者手动输入
	public static final String	BUNDLE_KEY_MEASURE_TYPE					= "measure_type";

	// ---------------------Communication ---------------------------------

	public int					bluetooth_state;
	public int					audio_state;

	public final static int		BLUETOOTH_STATE_ERROR					= -1;
	public final static int		BLUETOOTH_STATE_UNCONNECTED				= 0;
	public final static int		BLUETOOTH_STATE_CONNECTING				= 1;
	public final static int		BLUETOOTH_STATE_CONNECTED				= 2;
	public final static int		BLUETOOTH_STATE_CONNECTED_AND_DETECTED	= 3;
	public final static int		BLUETOOTH_STATE_DISCONNTECTION			= 4;
	public final static int		BLUETOOTH_STATE_NO_FOND_DEVICE			= 5;
	public final static int		BLUETOOTH_STATE_NOT_SUPPORT_BLUETOOTH	= 6;

	public final static int		AUDIO_STATE_INSERT_IN					= 1;
	public final static int		AUDIO_STATE_INSERT_OUT					= 2;
	public final static int		AUDIO_STATE_CONNECTING					= 3;
	public final static int		AUDIO_STATE_CONNECT_SUCCESS				= 4;
	public final static int		AUDIO_STATE_UNCONNECTED					= 0;
	public final static int		AUDIO_STATE_CONNECT_ERROR				= -1;

	public final static int		SHOW_DEVICE_LIST						= 0x100;
	public final static int		UPDATE_DEVICE_LIST						= 0x101;
	public final static int		HIDE_DEVICE_LIST						= 0x102;
	public final static int		UPDATE_STATUS							= 0x200;
	public final static int		DEVICE_DETECTED							= 0x201;
	public final static int		MEASURE_RESULT							= 0x202;
	public final static int		MEASURE_RELAY_RESULT					= 0x203;
	public final static int		MEASURE_NOT_WEARED						= 0x1001;

	private String				mDeviceAddress;
	private ArrayList<String>	mDeviceSearchList						= new ArrayList<String>();
	private MeasureHelper		mMeasureHelper;
	private IMessageProcessor	mMsgProcessor;
	// ------------------------------业务相关 ------------------------------

	private BluetoothFragment	mFragment;

	public void setMeasureCompleted() {
		mFragment = null;
		// TODO 做一些释放操作
	}

	public static void callMe(Context context, AbstractMeasureProxy<?> measureProxy) {
		if (context == null) return;
		Intent intent = new Intent();
		intent.setClass(context, MeasureActivity.class);
		intent.putExtra(AbstractMeasureProxy.class.getCanonicalName(), measureProxy);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private AbstractMeasureProxy<BaseMeasureData>	proxy;

	public AbstractMeasureProxy<BaseMeasureData> getMeasureProxy() {
		return this.proxy;
	}

	public CloudDevice getDevice() {
		if (proxy == null) throw new NullPointerException("must be check the proxy first");
		return proxy.getMesureDevice();
	}

	/**
	 * 初始化数据，数据可以从外部传入，或者本地配置文件中进行恢复。
	 * 
	 * @param savedInstanceState
	 */
	@SuppressWarnings("unchecked")
	private void initData(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setConfigurationValue(BUNDLE_KEY_MEASURE_TYPE, savedInstanceState.getString(BUNDLE_KEY_MEASURE_TYPE));
		}
		if (getIntent() != null && getIntent().hasExtra(AbstractMeasureProxy.class.getCanonicalName())) {
			proxy = (AbstractMeasureProxy<BaseMeasureData>) getIntent().getSerializableExtra(AbstractMeasureProxy.class.getCanonicalName());
			if (proxy == null || proxy.getMesureDevice() == null) {
				this.finish();
				return;
			}
		}
	}

	@Override
	protected void preLoadData(Bundle savedInstanceState) {
		super.preLoadData(savedInstanceState);
		initData(savedInstanceState);
		mMeasureHelper = new MeasureHelper(this, proxy.getMesureDevice(), handler);
		mMeasureHelper.bind();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(RefResourceUtil.getLayoutId(this, "activity_measure"));
		presentFragment(proxy.getConnectedView(null));
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(BUNDLE_KEY_MEASURE_TYPE, getConfigurationValue(BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Deprecated
	public void popBackTip() {
		showPopupWindow(getString(RefResourceUtil.getStringId(this, "alert_title")), getString(RefResourceUtil.getStringId(this, "alert_content")),
				getString(RefResourceUtil.getStringId(this, "action_confirm")), getString(RefResourceUtil.getStringId(this, "action_cancel")));
	}

	public Dialog	dialog;

	private void showPopupWindow(String title, String content, String left, String right) {
		if (isFinishing()) return;
		if (dialog == null) {
			ErrorDialogListener listener = new ErrorDialogListener() {
				@Override
				public void restart() {
					cancelmeasure();
					dialog.dismiss();
					finish();
				}

				@Override
				public void exit() {
					dialog.dismiss();
				}
			};
			dialog = new ErrorDialog(this, ErrorDialog.TYPE_NORMAL, listener, title, content, left, right).createDialog();

		}
		dialog.show();
	}

	public void presentFragment(BluetoothFragment to) {
		if (isFinishing()) {
			return;
		}
		mFragment = to;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(RefResourceUtil.getId(this, "measure_container"), to);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && mFragment != null) {
			mFragment.popBackStack();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// 以下初始在finish方法中，但在严格模式下finish方法不会被执行
		long s1 = System.currentTimeMillis();
		close();
		long s2 = System.currentTimeMillis();
		Log.d(getClass().getSimpleName(), getClass() + "close()：占用时间：<" + Math.abs(s2 - s1) + ">毫秒");
		mMeasureHelper.unbind();
		s1 = System.currentTimeMillis();
		Log.d(getClass().getSimpleName(), getClass() + "unbind()：占用时间：<" + Math.abs(s2 - s1) + ">毫秒");
		setSpeakerphoneOn();
		super.onDestroy();
		if (!configMap.isEmpty()) {
			configMap.clear();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		}
		else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	@Override
	public void finish() {
		Log.v(TAG, "measrue finisheid+");

		super.finish();

		Log.v(TAG, "measrue finisheid-");
	}

	private void setSpeakerphoneOn() {
		AudioManager audioManager = (AudioManager) (getSystemService(Service.AUDIO_SERVICE));

		if (!audioManager.isSpeakerphoneOn()) {
			audioManager.setSpeakerphoneOn(true);
		}
	}

	public void open() {
		mDeviceSearchList.clear();
		String devAddr = proxy.getMesureDevice().getPreferencesDeviceAddress(this.getApplicationContext());
		mDeviceSearchList.add(devAddr);
		mDeviceAddress = devAddr;
		mMeasureHelper.open(proxy.getMesureDevice(), devAddr);
	}

	public void open(String dev) {
		mMeasureHelper.open(proxy.getMesureDevice(), dev);
	}

	public void query() {
		mMeasureHelper.query();
	}

	public void measure() {
		mMeasureHelper.measure();
	}

	public void record() {
		mMeasureHelper.record();
	}

	public void cancelmeasure() {
		mMeasureHelper.cancelmeasure();
	}

	public void close() {
		if (mMsgProcessor != null) {
			mMsgProcessor = null;
		}
		mMeasureHelper.close();
	}

	public String[] getDeviceList() {
		return (String[]) mDeviceSearchList.toArray(new String[mDeviceSearchList.size()]);
	}

	public interface IMessageProcessor {
		public Message processMessage(Message msg);
	};

	MCloudSdkMeasureDataArgs	arg		= new MCloudSdkMeasureDataArgs();
	Handler						handler	= new Handler() {
											@Override
											public void handleMessage(Message msg) {
												if (mFragment == null) return;
												if (isFinishing()) return;
												String str = "Msg:" + msg.what + "--" + msg.arg1 + "--" + msg.arg2 + "--" + msg.obj;
												arg.put(MCloudSdkMeasureDataArgs.DEVPARAMS, str);
												InternalSdkImpl.getInstance().listenMeasureDataState(arg);
												Log.d(MeasureActivity.class.getCanonicalName(), "Msg:" + msg.what + "--" + msg.arg1 + "--" + msg.arg2 + "--" + msg.obj);
												int devType = proxy.getMesureDevice().getDeviceTagIntValue();
												String devAddr = proxy.getMesureDevice().getPreferencesDeviceAddress(getApplicationContext());
												boolean bounded = devAddr != null && !devAddr.equals(":");
												switch (msg.what) {
												case BluetoothMessage.msg_reply: {
													int type = msg.arg1;
													@SuppressWarnings("unchecked")
													HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
													if (devType == type) {
														Message subMsg = handler.obtainMessage(MEASURE_RESULT);
														subMsg.arg1 = msg.arg2; // command
														subMsg.arg2 = (Integer) result.get("status");
														subMsg.obj = result.get("detail");
														if (mMsgProcessor != null) subMsg = mMsgProcessor.processMessage(subMsg);
														mFragment.handleMessage(subMsg);
														Log.v(TAG, "result received" + msg.arg1 + "status =" + subMsg.arg2 + "params = " + msg.obj);
													}
													else if (devType > 0) {
														Log.v(TAG, "result received" + msg.arg1 + "params = " + msg.obj);
													}
												}
													break;

												case BluetoothMessage.msg_relay_result: {
													Message subMsg = handler.obtainMessage(MEASURE_RELAY_RESULT);
													subMsg.arg1 = msg.arg2; // command
													if (mMsgProcessor != null) subMsg = mMsgProcessor.processMessage(subMsg);
													mFragment.handleMessage(subMsg);
												}
													break;

												case BluetoothMessage.msg_status: {
													Log.v(TAG, "result msg_status received" + msg.arg1 + "params = " + msg.arg2);
													if (msg.arg1 == 0) {
														open();
													}

													switch (msg.arg2) {
													case BluetoothMessage.msg_device_search_not_start: {
														open();
													}
														break;

													case BluetoothMessage.msg_device_search_started: {
														if (bounded && mFragment != null) {
															Message subMsg = handler.obtainMessage(SHOW_DEVICE_LIST);
															mFragment.handleMessage(subMsg);
														}
													}
														break;

													case BluetoothMessage.msg_device_detected: {
														audio_state = AUDIO_STATE_INSERT_IN;
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_CONNECTING;
														@SuppressWarnings("unchecked")
														HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
														mDeviceAddress = (String) result.get("detail");
														if (bounded && mFragment != null && mDeviceAddress != null) {
															int msgType = 0;
															if (mDeviceAddress.equals(devAddr)) {
																msgType = HIDE_DEVICE_LIST;
															}
															else if (!mDeviceSearchList.contains(mDeviceAddress)) {
																mDeviceSearchList.add(mDeviceAddress);
																msgType = UPDATE_DEVICE_LIST;
															}
															Message subMsg = handler.obtainMessage(msgType);
															mFragment.handleMessage(subMsg);
														}

														Message subMsg2 = handler.obtainMessage(DEVICE_DETECTED);
														subMsg2.obj = mDeviceAddress;
														mFragment.handleMessage(subMsg2);
													}
														break;

													case BluetoothMessage.msg_device_search_error: {
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_NO_FOND_DEVICE;
														audio_state = AUDIO_STATE_INSERT_OUT;
													}
														break;

													case BluetoothMessage.msg_device_disconnected: {
														bluetooth_state = BLUETOOTH_STATE_ERROR;
														audio_state = AUDIO_STATE_INSERT_OUT;
													}
														break;

													case BluetoothMessage.msg_socket_connecting: {
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_UNCONNECTED;
														audio_state = AUDIO_STATE_INSERT_OUT;
													}
														break;
													case BluetoothMessage.msg_socket_connected: {
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_CONNECTED;
														audio_state = AUDIO_STATE_CONNECT_SUCCESS;
														proxy.getMesureDevice().savePreferencesDeviceAddress(getApplicationContext(), mDeviceAddress);
														mDeviceSearchList.clear();
													}
														break;

													case BluetoothMessage.msg_socket_connect_failed: {
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_ERROR;
														audio_state = AUDIO_STATE_CONNECT_ERROR;
													}
														break;

													case BluetoothMessage.msg_socket_disconnected: {
														bluetooth_state = MeasureActivity.BLUETOOTH_STATE_DISCONNTECTION;
														audio_state = AUDIO_STATE_CONNECT_ERROR;
													}
														break;

													}
													Log.e("BloodPressureConnectFragment", "Handler$bluetooth_state:" + bluetooth_state + "|" + MeasureActivity.this.hashCode());

													if (msg.arg1 == devType) {
														Message subMsg = handler.obtainMessage(UPDATE_STATUS);
														subMsg.arg1 = msg.arg2;
														if (mMsgProcessor != null) subMsg = mMsgProcessor.processMessage(subMsg);
														mFragment.handleMessage(subMsg);
													}
												}
													break;
												}
												Log.v("robert", "bluetooth_state:" + bluetooth_state);
												super.handleMessage(msg);
											}
										};
}
