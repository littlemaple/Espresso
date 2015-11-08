package com.medzone.mcloud.measure.bloodoxygen;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * 
 * @category 血氧测量连接
 */
public class BloodOxygenConnectFragment extends BluetoothFragment implements OnClickListener {

	// --------------------Animation Controller-------------

	private int	connectFlag		= 0;
	private int	slidingFlag		= 3;
	private int	openDeviceFlag	= 5;
	private Timer	bluetoothConnectionTimer, handSlidingTimer, openDeviceTimer;
	private TimerTask	bluetoothConnectionTask, handSlidingTask, openDeviceTask;

	// ---------------------View Structure----------------------------

	private View		rootView;
	private TextView	textTV;
	private TextView	tvVersion;
	private Button		startMeasureBtn;
	private ImageView	flagIV, oximeterIV;
	private LinearLayout	connectionLL, successLL;
	private FrameLayout		openDeviceFL;
	private Dialog			dialog;

	private ImageButton		btnInput;
	private ImageView		titleImage;
	private LinearLayout	llTitle;
	// ---------------------- Other------------------

	private boolean			isError	= false;
	private MeasureActivity	attachActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		attachActivity = (MeasureActivity) activity;

	}

	// -----------------------初始化组件-----------------------------------------

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
		View actionBarView = LayoutInflater.from(getActivity()).inflate(RefResourceUtil.getLayoutId(getActivity(), "custom_actionbar_with_image"), null);
		llTitle = (LinearLayout) actionBarView.findViewById(RefResourceUtil.getId(getActivity(), "ll_action_title"));
		TextView titleTV = (TextView) actionBarView.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_title"));
		ColorStateList csl = getResources().getColorStateList(RefResourceUtil.getColorId(getActivity(), "selector_share_white_font"));
		titleTV.setTextColor(csl);
		titleImage = (ImageView) actionBarView.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_iv"));
		titleImage.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_ic_cutoveruser"));
		llTitle.setOnClickListener(this);
		ImageButton leftButton = (ImageButton) actionBarView.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_left"));
		leftButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "personalinformationview_ic_cancel"));
		leftButton.setOnClickListener(this);

		btnInput = (ImageButton) actionBarView.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_right"));
		btnInput.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "actionbar_icon_add"));
		btnInput.setOnClickListener(this);
		actionBar.setCustomView(actionBarView, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		rootView = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_oxygen_connect"), container, false);
		connectionLL = (LinearLayout) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_connect_in_ll"));
		successLL = (LinearLayout) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_connect_success_ll"));
		flagIV = (ImageView) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_connect_flag_iv"));
		oximeterIV = (ImageView) rootView.findViewById(RefResourceUtil.getId(getActivity(), "device_oxygen_hander"));
		textTV = (TextView) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_connect_textTV"));
		tvVersion = (TextView) rootView.findViewById(RefResourceUtil.getId(getActivity(), "constans_version"));
		startMeasureBtn = (Button) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_connect_start_btn"));
		openDeviceFL = (FrameLayout) rootView.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_open_device_fl"));
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (dialog != null) {
			dialog.dismiss();
		}
		startMeasureBtn.setOnClickListener(this);

		comeBackDealWith();
	}

	// 初始化弹出信息
	private void initPopupWindow(String title, String content) {
		if (dialog == null) {
			ErrorDialogListener listener = new ErrorDialogListener() {
				@Override
				public void restart() {
					dialog.dismiss();
					connectFlag = 0;
					slidingFlag = 3;
					openDeviceFlag = 5;
					isError = false;
					textTV.setText(RefResourceUtil.getStringId(getActivity(), "measure_connect_open_device_bo"));
					flagIV.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "guideview_connection_01"));
					sendStopMeasure();
					if (getActivity() != null) sendStartDiscover(attachActivity.getDevice());
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
		if (attachActivity == null || !attachActivity.isActive) return;
		if (dialog == null) {
			initPopupWindow(title, content);
		}
		dialog.show();
	}

	// --------------------生命周期-----------------------------

	@Override
	public void onStart() {
		super.onStart();
		WakeLockUtil.acquireWakeLock(getActivity());
		initActionBar();
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

	// ==================事件==================================
	@Override
	public void onClick(View v) {
		if (v.getId() == RefResourceUtil.getId(getActivity(), "oxygen_connect_start_btn")) {
			if (attachActivity.bluetooth_state == MeasureActivity.BLUETOOTH_STATE_CONNECTED) {
				attachActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
				attachActivity.presentFragment(attachActivity.getMeasureProxy().getMeasureView(null));
			}
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_right")) {
			attachActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.INPUT);
			// attachActivity.comeBackBloodOxygenInput(this);
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			attachActivity.finish();
		}
		else {

		}

	}

	private void comeBackDealWith() {
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

	// ==========================硬件接受设备====================================
	@SuppressLint("NewApi")
	// 找到接受设备
	private void receiverDeviceFound() {
		stopTimer();
		textTV.setText(RefResourceUtil.getStringId(getActivity(), "measure_connect_open_device_in"));
		startBluetoothConnectionTimer();
		openDeviceFL.setVisibility(View.VISIBLE);
		try {
			openDeviceFL.setAlpha(1f);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 连接设备
	private void receiverDeviceConnected() {
		stopTimer();
		// startMeasureBtn.setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_highlight);
		startMeasureBtn.setEnabled(true);
		successLL.setVisibility(View.VISIBLE);
		connectionLL.setVisibility(View.GONE);
	}

	private void receiverOtherDeviceConnected() {
		connectionLL.setVisibility(View.VISIBLE);
		successLL.setVisibility(View.GONE);
		// startMeasureBtn.setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_disabled);
		startMeasureBtn.setEnabled(false);
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "find_other_device")), getString(RefResourceUtil.getStringId(getActivity(), "other_device_connected")));
		}
	}

	// 设备测量
	private void receiverDeviceMeasuring() {
		if (attachActivity.bluetooth_state >= MeasureActivity.BLUETOOTH_STATE_CONNECTED) {
			attachActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
			// attachActivity.comeBackBloodOxygenMeasure(this);
		}
	}

	private void receiverOtherDeviceMeasuring() {
		connectionLL.setVisibility(View.VISIBLE);
		successLL.setVisibility(View.GONE);
		// startMeasureBtn.setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_disabled);
		startMeasureBtn.setEnabled(false);
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "find_other_device")), getString(RefResourceUtil.getStringId(getActivity(), "other_device_measuring")));
		}
	}

	// 连接设备失败
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

	// 蓝牙断开连接
	private void receiverDeviceDisconnected() {
		successLL.setVisibility(View.GONE);

		connectionLL.setVisibility(View.VISIBLE);
		// startMeasureBtn.setBackgroundResource(RefResourceUtil.getDrawableId(getActivity(),
		// "guideview_btn_start_disabled);

		startMeasureBtn.setEnabled(false);
		if (attachActivity.bluetooth_state != MeasureActivity.BLUETOOTH_STATE_UNCONNECTED && attachActivity.bluetooth_state != MeasureActivity.BLUETOOTH_STATE_CONNECTED_AND_DETECTED) {

			if (!isError) {
				showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_error")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_disconnect")));
			}
		}
	}

	// 接收设备未找到
	private void receiverDeviceNotFound() {
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "not_find_device")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_no_fond_device")));
		}
	}

	// 设备不支持
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

	private void alpha(View view) {
		openDeviceFL.setVisibility(View.VISIBLE);
		Animation animationAlpha = AnimationUtils.loadAnimation(attachActivity, RefResourceUtil.getAnimId(getActivity(), "open_device_alpha"));
		view.startAnimation(animationAlpha);
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
		handSlidingTimer.schedule(handSlidingTask, 100, Constants.MEASURE_TIME_ONE);
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

	private Handler	handler	= new Handler() {
								@SuppressLint("NewApi")
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

										if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
											oximeterIV.clearAnimation();
											TranslateUtil.TranslatesUtil(oximeterIV, oximeterIV.getX());
										}
										else {
											int[] loc = new int[2];
											oximeterIV.getLocationOnScreen(loc);
											if (loc[0] == 0) {
												// 旧版本的android获取不到控件在屏幕中的位置暂时先这样定死了写
											}
											else {
												TranslateUtil.TranslatesUtil(oximeterIV, loc[0]);
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

	@Override
	public void handleMessage(Message msg) {
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
			default:
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

	private Dialog				mDevListDialog	= null;
	private ListView			mDeviceListView	= null;
	private String[]			mList			= null;
	private ShowDeviceAdapter	deviceAdapter;
	private LayoutInflater		inflater;
	private View				layout;

	private void showDeviceList() {
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
		mDeviceListView.setAdapter(deviceAdapter);
		deviceAdapter.notifyDataSetChanged();
		mDeviceListView.invalidate();
	}

	private void hideDeviceList() {
		if (mDevListDialog != null) {
			mDevListDialog.dismiss();
			mDevListDialog = null;
		}

	}

	public Dialog getDeviceDialog(String[] displayNames) {
		deviceAdapter = new ShowDeviceAdapter(attachActivity);
		deviceAdapter.setDefault("mCloud-O");
		deviceAdapter._list.addAll(Arrays.asList(displayNames));
		mDeviceListView.setAdapter(deviceAdapter);
		deviceAdapter.notifyDataSetChanged();

		final AlertDialog dialog = new AlertDialog.Builder(attachActivity).setTitle(RefResourceUtil.getStringId(getActivity(), "use_device_select")).setView(layout).create();
		deviceAdapter.setParent(dialog);

		// 2014-12-18测试时注释保证能推出设备选择页面
		// dialog.setCanceledOnTouchOutside(false);// 使除了dialog以外的地方不能被点击
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				attachActivity.finish();
			}
		});
		dialog.show();
		return dialog;
	}

	@Override
	public void popBackStack() {
		attachActivity.finish();
	}
}
