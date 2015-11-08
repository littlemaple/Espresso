package com.medzone.mcloud.measure.bloodpressure;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.background.BluetoothMessage;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.ShowDeviceAdapter;
import com.medzone.mcloud.dialog.ErrorDialog;
import com.medzone.mcloud.dialog.ErrorDialog.ErrorDialogListener;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.utils.RefResourceUtil;
import com.medzone.mcloud.utils.TranslateUtil;
import com.medzone.mcloud.utils.WakeLockUtil;

public class BloodPressureConnectFragment extends BluetoothFragment implements OnClickListener {

	private static final String	TAG				= "BloodPressureConnectFragment";
	private int					connectFlag		= 0;
	private int					slidingFlag		= 3;
	private int					openDeviceFlag	= 5;
	private boolean				isError			= false;
	private View				view;
	private TextView			textTV, tvVersion;
	private Button				startMeasureBtn;
	private MeasureActivity		attachActivity;
	private ImageView			handerIV, flagIV;
	private LinearLayout		connectionLL, successLL;
	private FrameLayout			openDeviceFL;
	private Dialog				dialog;
	private Timer				bluetoothConnectionTimer, handSlidingTimer, openDeviceTimer;
	private TimerTask			bluetoothConnectionTask, handSlidingTask, openDeviceTask;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachActivity = (MeasureActivity) activity;
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
		View view = LayoutInflater.from(getActivity()).inflate(RefResourceUtil.getLayoutId(getActivity(), "custom_actionbar_with_image"), null);

		TextView titleTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_title"));
		ColorStateList csl = getResources().getColorStateList(RefResourceUtil.getColorId(getActivity(), "selector_share_white_font"));
		titleTV.setTextColor(csl);

		ImageButton leftButton = (ImageButton) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_left"));
		leftButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "personalinformationview_ic_cancel"));
		leftButton.setOnClickListener(this);

		ImageButton rightButton = (ImageButton) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_right"));
		rightButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "actionbar_icon_add"));
		rightButton.setOnClickListener(this);
		actionBar.setCustomView(view, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initActionBar();
		view = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_pressure_connect"), container, false);
		handerIV = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "device_pressure_hander"));
		flagIV = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_connect_flag_iv"));
		textTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_connect_textTV"));
		successLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_connect_success_ll"));
		connectionLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_connect_in_ll"));
		startMeasureBtn = (Button) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_connect_start_btn"));
		openDeviceFL = (FrameLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_open_device_fl"));
		tvVersion = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "constans_version"));
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		startMeasureBtn.setOnClickListener(this);
		if (dialog != null) {
			dialog.dismiss();
		}
		comeBackDealWith();
	}

	private void comeBackDealWith() {

		Log.w(getClass().getSimpleName(), "comeBackDealWith$bluetooth_state：" + attachActivity.bluetooth_state + "|" + attachActivity.hashCode());
		if (attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_UNCONNECTED) {
			startHandSlidingTimer();
			startOpenDeviceTimer();
		}
		else if (attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_ERROR || attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_DISCONNTECTION
				|| attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_NO_FOND_DEVICE) {
			sendStartDiscover(attachActivity.getDevice());
			startHandSlidingTimer();
			startOpenDeviceTimer();
			isError = false;
		}
		else {
			updateViewByBluetoothState();
		}
	}

	private void startHandSlidingTimer() {
		if (handSlidingTimer == null) handSlidingTimer = new Timer();
		if (handSlidingTask == null) handSlidingTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = slidingFlag;
				handler.sendMessage(message);
				if (slidingFlag == Constants.SLIDING_FLAG_INIT) {
					slidingFlag = -1;
				}
			}
		};
		// 延时100毫秒保证相应的控件被加载，从而能获取到相应的高度，使动画效果得以显现
		handSlidingTimer.schedule(handSlidingTask, 50, Constants.MEASURE_TIME_ONE);
	}

	private void startOpenDeviceTimer() {
		if (openDeviceTimer == null) openDeviceTimer = new Timer();
		if (openDeviceTask == null) openDeviceTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = openDeviceFlag;
				handler.sendMessage(message);
			}
		};
		openDeviceTimer.schedule(openDeviceTask, Constants.MEASURE_TIME_ONE, Constants.MEASURE_TIME_TWO);
	}

	private void startBluetoothConnectionTimer() {
		if (bluetoothConnectionTimer == null) bluetoothConnectionTimer = new Timer();
		if (bluetoothConnectionTask == null) bluetoothConnectionTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = connectFlag;
				handler.sendMessage(message);
				if (connectFlag == Constants.CONNECT_FLAG_END) {
					connectFlag = -1;
				}
				connectFlag++;
			}
		};
		bluetoothConnectionTimer.schedule(bluetoothConnectionTask, 0, Constants.MEASURE_TIME_TWO);
	}

	private void stopTimer() {
		if (bluetoothConnectionTimer != null) {
			bluetoothConnectionTimer.cancel();
			bluetoothConnectionTimer = null;
		}
		if (bluetoothConnectionTask != null) {
			bluetoothConnectionTask.cancel();
			bluetoothConnectionTask = null;
		}
		if (handSlidingTimer != null) {
			handSlidingTimer.cancel();
			handSlidingTimer = null;
		}
		if (handSlidingTask != null) {
			handSlidingTask.cancel();
			handSlidingTask = null;
		}

		if (openDeviceTimer != null) {
			openDeviceTimer.cancel();
			openDeviceTimer = null;
		}
		if (openDeviceTask != null) {
			openDeviceTask.cancel();
			openDeviceTask = null;
		}

	}

	private Handler	handler	= new Handler() {
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									switch (msg.what) {
									case Constants.CONNECT_FLAG_START:
										flagIV.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_connection_01"));
										break;
									case Constants.CONNECT_FLAG_IN:
										flagIV.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_connection_02"));
										break;
									case Constants.CONNECT_FLAG_END:
										flagIV.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_connection_03"));
										break;
									case Constants.SLIDING_FLAG_INIT:
										// new TranslateUtil(handerIV,
										// -1.7f,0.4f);
										handerIV.clearAnimation();
										if (handerIV.getLeft() != 0) {
											TranslateUtil.TranslatesUtil(handerIV, handerIV.getLeft());
										}
										else {
											int[] loc = new int[2];
											handerIV.getLocationOnScreen(loc);
											if (loc[0] == 0) {
											}
											else {
												TranslateUtil.TranslatesUtil(handerIV, loc[0]);
											}
										}

										break;
									case Constants.OPEN_DEVICE_FLAG_INIT:
										alpha(openDeviceFL);
										break;
									default:
										break;
									}
								}
							};

	private void alpha(View view) {
		openDeviceFL.setVisibility(View.VISIBLE);
		Animation animationAlpha = AnimationUtils.loadAnimation(attachActivity, RefResourceUtil.getAnimId(getActivity(), "open_device_alpha"));
		view.startAnimation(animationAlpha);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == RefResourceUtil.getId(getActivity(), "pressure_connect_start_btn")) {
			if (attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_CONNECTED) {
				attachActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
				attachActivity.presentFragment(new BloodPressureMeasureFragment());
			}
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_right")) {
			attachActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.INPUT);
			attachActivity.presentFragment(attachActivity.getMeasureProxy().getMeasureView(null));
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			attachActivity.finish();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "ll_action_title")) {

		}
		else {

		}

	}

	@Override
	public void onDestroyView() {
		WakeLockUtil.releaseWakeLock();
		stopTimer();
		isError = true;
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onDestroyView();
	}

	// 初始化弹出信息
	private void initPopupWindow(String title, String content) {
		if (dialog == null) {
			ErrorDialogListener listener = new ErrorDialogListener() {
				@Override
				public void restart() {
					hideDeviceList();
					dialog.dismiss();
					openDeviceFL.setVisibility(View.GONE);
					connectFlag = 0;
					slidingFlag = 3;
					openDeviceFlag = 5;
					isError = false;
					textTV.setText(RefResourceUtil.getStringId(getActivity(), "measure_connect_open_device_bp"));
					flagIV.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_connection_01"));
					sendStopMeasure();
					sendStartDiscover(attachActivity.getDevice());
					startHandSlidingTimer();
					startOpenDeviceTimer();
				}

				@Override
				public void exit() {
					dialog.dismiss();
					attachActivity.finish();
				}
			};
			dialog = new ErrorDialog(attachActivity, ErrorDialog.TYPE_NORMAL, listener, title, content, getString(RefResourceUtil.getStringId(getActivity(), "reconnect")),
					getString(RefResourceUtil.getStringId(getActivity(), "action_exitmeasure"))).createDialog();
		}
	}

	private void showPopupWindow(String title, String content) {
		isError = true;
		stopTimer();
		if (attachActivity == null || getActivity().isFinishing()) return;

		if (dialog == null) {
			initPopupWindow(title, content);
		}
		// 清除背景动画效果
		handerIV.clearAnimation();
		dialog.show();
	}

	private void receiverDeviceMeasuring() {
		if (attachActivity.bluetooth_state >= MeasureActivity.BLUETOOTH_STATE_CONNECTED) {
			// attachActivity.comeBackBloodPressureMeasure(this);
		}
	}

	private Dialog				mDevListDialog	= null;
	private ListView			mDeviceListView	= null;
	private String[]			mList			= null;
	private ShowDeviceAdapter	deviceAdapter;
	private LayoutInflater		inflater;
	private View				layout;

	private void showDeviceList() {
		Log.v(TAG, "showDeviceList");
		inflater = (LayoutInflater) attachActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "dialog_list"), null);
		mDeviceListView = (ListView) layout.findViewById(RefResourceUtil.getId(getActivity(), "device_list"));

		if (mDevListDialog != null) {
			updateDeviceList();
			mDevListDialog.show();
			return;
		}

		mList = attachActivity.getDeviceList();
		mDevListDialog = getDeviceDialog(mList);

	}

	private void updateDeviceList() {
		mList = attachActivity.getDeviceList();
		deviceAdapter._list.clear();
		deviceAdapter._list.addAll(Arrays.asList(mList));
		deviceAdapter.notifyDataSetChanged();
		mDeviceListView.invalidate();
	}

	private void hideDeviceList() {
		if (mDevListDialog != null) {
			mDevListDialog.dismiss();
			mDevListDialog = null;
		}

	}

	private void showScanProgress() {

	}

	private void hideScanProgress() {

	}

	private void updateViewByBluetoothState() {

		switch (attachActivity.bluetooth_state) {
		case MeasureActivity.BLUETOOTH_STATE_CONNECTED_AND_DETECTED:
			receiverDeviceFound();
			break;
		case MeasureActivity.BLUETOOTH_STATE_CONNECTED:
			receiverDeviceConnected();
			break;
		case MeasureActivity.BLUETOOTH_STATE_ERROR:
			receiverDeviceConnectError();
			break;
		case MeasureActivity.BLUETOOTH_STATE_DISCONNTECTION:
			receiverDeviceDisconnected();
			break;
		case MeasureActivity.BLUETOOTH_STATE_NO_FOND_DEVICE:
			receiverDeviceNotFound();
			break;
		case MeasureActivity.BLUETOOTH_STATE_NOT_SUPPORT_BLUETOOTH:
			receiverNotSupportBluetooth();
			break;
		default:
			break;
		}
	}

	/** 发现设备 */
	@SuppressLint("NewApi")
	private void receiverDeviceFound() {
		stopTimer();
		textTV.setText(RefResourceUtil.getStringId(getActivity(), "measure_connect_open_device_in"));
		openDeviceFL.setVisibility(View.VISIBLE);
		startBluetoothConnectionTimer();
		try {
			openDeviceFL.setAlpha(1f);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 设备连接 */
	private void receiverDeviceConnected() {
		stopTimer();
		// startMeasureBtn
		// .setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "btn_connect_start_highlight);
		startMeasureBtn.setEnabled(true);
		int currentTextColor = startMeasureBtn.getCurrentTextColor();
		successLL.setVisibility(View.VISIBLE);
		connectionLL.setVisibility(View.GONE);
	}

	/** 设备连接错误 */
	private void receiverDeviceConnectError() {
		connectionLL.setVisibility(View.VISIBLE);
		successLL.setVisibility(View.GONE);
		// startMeasureBtn
		// .setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_disabled);

		startMeasureBtn.setEnabled(false);
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_failure")),
					getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_mistakes")));
		}
	}

	/** 蓝牙断开连接 */
	private void receiverDeviceDisconnected() {
		connectionLL.setVisibility(View.VISIBLE);
		successLL.setVisibility(View.GONE);
		// startMeasureBtn
		// .setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_disabled"));
		startMeasureBtn.setEnabled(false);
		if (attachActivity.bluetooth_state != MeasureActivity.BLUETOOTH_STATE_UNCONNECTED && attachActivity.bluetooth_state != MeasureActivity.BLUETOOTH_STATE_CONNECTED_AND_DETECTED) {

			if (!isError) {
				showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_error")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_disconnect")));
			}
		}
	}

	/** 未发现设备 */
	private void receiverDeviceNotFound() {
		connectionLL.setVisibility(View.VISIBLE);
		successLL.setVisibility(View.GONE);
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "not_find_device")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_no_fond_device")));
		}
	}

	private void receiverNotSupportBluetooth() {
		ErrorDialogListener listener = new ErrorDialogListener() {
			@Override
			public void restart() {
				dialog.dismiss();
				attachActivity.finish();
			}

			@Override
			public void exit() {
			}
		};
		dialog = new ErrorDialog(attachActivity, ErrorDialog.TYPE_OTHER, listener, getString(RefResourceUtil.getStringId(getActivity(), "device_not_support")), getString(RefResourceUtil.getStringId(
				getActivity(), "device_not_support_details")), getString(RefResourceUtil.getStringId(getActivity(), "public_submit")), null).createDialog();
		dialog.show();
	}

	@Override
	public void handleMessage(Message msg) {
		Log.v(TAG, "recv msg =" + msg.what + ", arg1=" + msg.arg1 + ", arg2=" + msg.arg2);
		switch (msg.what) {
		case MeasureActivity.DEVICE_DETECTED: {
			receiverDeviceFound();
		}
			break;

		case MeasureActivity.UPDATE_STATUS: {
			switch (msg.arg1) {
			case BluetoothMessage.msg_device_search_error: {
				receiverDeviceNotFound();
			}
				break;
			case BluetoothMessage.msg_socket_connected: {
				receiverDeviceConnected();
			}
				break;
			case BluetoothMessage.msg_device_search_started: {
				showScanProgress();
			}
				break;
			case BluetoothMessage.msg_device_search_finished: {
				hideScanProgress();
			}
				break;

			case BluetoothMessage.msg_socket_connect_failed: {
				receiverDeviceConnectError();
			}
				break;

			case BluetoothMessage.msg_socket_disconnected: {
				receiverDeviceDisconnected();
			}
				break;
			case BluetoothMessage.msg_socket_measuring: {
				receiverDeviceMeasuring();
			}
				break;
			}
		}
			break;
		case MeasureActivity.SHOW_DEVICE_LIST:
			showDeviceList();
			break;
		case MeasureActivity.HIDE_DEVICE_LIST:
			hideDeviceList();
			break;
		case MeasureActivity.UPDATE_DEVICE_LIST:
			updateDeviceList();
			break;
		}
	}

	public Dialog getDeviceDialog(String[] displayNames) {
		deviceAdapter = new ShowDeviceAdapter(attachActivity);
		deviceAdapter._list.addAll(Arrays.asList(displayNames));
		mDeviceListView.setAdapter(deviceAdapter);
		deviceAdapter.setDefault("mCloud-P");
		deviceAdapter.notifyDataSetChanged();

		final AlertDialog dialog = new AlertDialog.Builder(attachActivity).setTitle(RefResourceUtil.getStringId(getActivity(), "use_device_select")).setView(layout).create();

		// 测试关闭背后的Activity时注释 2014/12/17
		// dialog.setCanceledOnTouchOutside(false);// 使除了dialog以外的地方不能被点击
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				attachActivity.finish();
			}
		});
		dialog.show();
		deviceAdapter.setParent(dialog);
		return dialog;
	}

	@Override
	public void popBackStack() {
		attachActivity.finish();
	}
}
