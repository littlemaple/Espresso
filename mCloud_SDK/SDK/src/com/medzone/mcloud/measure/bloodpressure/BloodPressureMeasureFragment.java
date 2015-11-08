package com.medzone.mcloud.measure.bloodpressure;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.background.BluetoothMessage;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.dialog.ErrorDialog;
import com.medzone.mcloud.dialog.ErrorDialog.ErrorDialogListener;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.utils.RefResourceUtil;
import com.medzone.mcloud.utils.WakeLockUtil;

public class BloodPressureMeasureFragment extends BluetoothFragment implements OnClickListener {
	private CloudDevice		attachDevice;
	private MeasureActivity	mActivity;
	private Animation		myAnimationScale;

	private View			view;
	private TextView		tvStatic;
	private Timer			timerHeart;
	private TimerTask		taskHeart;
	private ImageView		redHeart, whileHeart, bigHeart;

	private boolean			isSuccess		= false;
	private boolean			isError			= false;
	private boolean			disConnection	= false;
	private boolean			isPause			= false;
	private String			high, low, rate, staticBP, deviceId;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MeasureActivity) activity;
		attachDevice = mActivity.getDevice();
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
		View view = LayoutInflater.from(getActivity()).inflate(RefResourceUtil.getLayoutId(getActivity(), "custom_actionbar_with_image"), null);
		TextView title = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_title"));
		title.setOnClickListener(this);
		ImageButton leftButton = (ImageButton) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_left"));
		leftButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "personalinformationview_ic_cancel"));
		leftButton.setOnClickListener(this);

		actionBar.setCustomView(view, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_pressure_measure"), container, false);
		initActionBar();
		tvStatic = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_measure_changeTV"));
		redHeart = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_measure_red_heart_iv"));
		whileHeart = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_measure_while_heart_iv"));
		bigHeart = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_measure_big_heart_iv"));
		view.findViewById(RefResourceUtil.getId(getActivity(), "img_tv")).setOnClickListener(this);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		WakeLockUtil.acquireWakeLock(getActivity());
		stopTimer();
		startTimer();
		if (isPause && isSuccess) {
			toResultFragment();
		}
		else if (!isPause) {
			sendStartMeasureBroadCast(attachDevice);
			scriptUIAction();
		}
	}

	private void stopTimer() {
		if (timerHeart != null) {
			timerHeart.cancel();
			timerHeart = null;
		}
		if (taskHeart != null) {
			taskHeart.cancel();
			taskHeart = null;
		}
	}

	private void startTimer() {
		if (timerHeart == null) timerHeart = new Timer();
		if (taskHeart == null) taskHeart = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = Constants.MEASURE_STATE;
				handler.sendMessage(message);
			}
		};
		timerHeart.schedule(taskHeart, 0, Constants.MEASURE_TIME_TWO);
	}

	private void toResultFragment() {
		if (mActivity.dialog != null) mActivity.dialog.dismiss();
		mActivity.setConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
		Bundle bundle = new Bundle();
		bundle.putString(Constants.HIGH_PRESSURE, high);
		bundle.putString(Constants.LOW_PRESSURE, low);
		bundle.putString(Constants.RATE, rate);
		bundle.putString(Constants.DEVICE_ID, deviceId);
		mActivity.presentFragment(mActivity.getMeasureProxy().getResultView(bundle));
		mActivity.setMeasureCompleted();
	}

	private void scriptUIAction() {
		Message message = handler.obtainMessage();
		message.what = Constants.MEASURE_PENDING;
		handler.sendMessage(message);

		message = handler.obtainMessage();
		message.what = Constants.MEASURE_TIMEOUT;
		handler.sendMessageDelayed(message, 120000);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			popBackStack();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "img_tv")) {
			toResultFragment();
		}
	}

	@Override
	public void onResume() {
		isPause = false;
		super.onResume();
	}

	@Override
	public void onPause() {
		isPause = true;
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		WakeLockUtil.releaseWakeLock();
		stopTimer();
		super.onDestroyView();
	}

	Handler	handler	= new Handler() {
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							if (!isVisible()) return;
							switch (msg.what) {
							case Constants.MEASURE_PENDING:
								break;
							case Constants.MEASURE_COMPLETE:
								if (isSuccess && !isPause) {
									toResultFragment();
								}
								break;
							case Constants.MEASURE_TIMEOUT: {
								if (!isError) {
									showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "measure_time_out_title")),
											getString(RefResourceUtil.getStringId(getActivity(), "measure_time_out")), getString(RefResourceUtil.getStringId(getActivity(), "alert_restart")));
								}
							}
								break;
							case Constants.MEASURE_STATE:
								scale(redHeart);
								scale(whileHeart);
								scale(bigHeart);
								break;
							default:
								break;
							}
						}
					};

	private void scale(View view) {
		myAnimationScale = AnimationUtils.loadAnimation(mActivity, RefResourceUtil.getAnimId(getActivity(), "heart_scale_action"));
		view.startAnimation(myAnimationScale);
	}

	private Dialog	dialog;

	// 初始化弹出信息
	private void initPopupWindow(String title, String content, String leftTV) {
		if (dialog == null) {
			ErrorDialogListener listener = new ErrorDialogListener() {
				@Override
				public void restart() {
					dialog.dismiss();
					if (disConnection) {
					}
					else {
						startTimer();
						disConnection = false;
						isError = false;
						sendStartMeasureBroadCast(attachDevice);
					}
				}

				@Override
				public void exit() {
					dialog.dismiss();
					mActivity.finish();
				}
			};
			dialog = new ErrorDialog(mActivity, ErrorDialog.TYPE_NORMAL, listener, title, content, leftTV, getString(RefResourceUtil.getStringId(getActivity(), "action_exitmeasure"))).createDialog();
		}
	}

	private void showPopupWindow(String title, String content, String leftTV) {
		if (!isDetached()) {
			isError = true;
			stopTimer();
			if (mActivity == null || mActivity.isFinishing()) return;

			if (dialog == null) {
				initPopupWindow(title, content, leftTV);
			}

			dialog.show();
		}
	}

	private void registerBluetoothDisconnection() {
		// 蓝牙断开连接
		disConnection = true;
		if (!isError) {
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_error")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_disconnect")),
					getString(RefResourceUtil.getStringId(getActivity(), "reconnect")));
		}
	}

	private void errorShow(int what, String msg) {
		switch (what) {
		case 0:
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "measure_time_out_title")), getString(RefResourceUtil.getStringId(getActivity(), "measure_time_out")),
					getString(RefResourceUtil.getStringId(getActivity(), "alert_restart")));
			break;
		case 1:// TODO 电池电量不足，在刚出现腕带充气设备便停止工作的时候,出现此提示
			receiverLowBattery(getString(RefResourceUtil.getStringId(getActivity(), "low_battery")), getString(RefResourceUtil.getStringId(getActivity(), "ear_low_battery")));
			break;
		case 2:
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "equipment_abnormal")), getString(RefResourceUtil.getStringId(getActivity(), "equipment_abnormal_restart")),
					getString(RefResourceUtil.getStringId(getActivity(), "alert_restart")));
			break;
		case 3:
			showPopupWindow(getString(RefResourceUtil.getStringId(getActivity(), "equipment_abnormal")), getString(RefResourceUtil.getStringId(getActivity(), "equipment_abnormal_restart")),
					getString(RefResourceUtil.getStringId(getActivity(), "alert_restart")));
			break;
		default:
			break;
		}
	}

	private void receiverLowBattery(String title, String content) {
		ErrorDialogListener listener = new ErrorDialogListener() {
			@Override
			public void restart() {
				dialog.dismiss();
				mActivity.finish();
			}

			@Override
			public void exit() {
			}
		};
		dialog = new ErrorDialog(mActivity, ErrorDialog.TYPE_OTHER, listener, title, content, getString(RefResourceUtil.getStringId(getActivity(), "public_submit")), null).createDialog();
		dialog.show();
	}

	@Override
	public void handleMessage(Message msg) {
		Log.d(BloodPressureMeasureFragment.class.getSimpleName(), "msg:" + msg.what + "---" + msg.arg1 + "---" + msg.arg2 + "--" + msg.obj);
		switch (msg.what) {
		case MeasureActivity.MEASURE_RESULT: {
			int cmd = msg.arg1;
			int state = msg.arg2;
			String detail = (String) msg.obj;
			registerDataDealWith2(cmd, state, detail);
		}
			break;

		case MeasureActivity.UPDATE_STATUS: {
			switch (msg.arg1) {
			case BluetoothMessage.msg_socket_disconnected: {
				registerBluetoothDisconnection();
			}
				break;
			}
		}
			break;
		}
	}

	private void registerDataDealWith2(int cmd, int state, String detail) {
		// 接收数据

		if (cmd == 1) {
			deviceId = detail;
		}
		else if (cmd == 2) {
			if (state == 0) {
				String[] values = detail.split(";");
				int count = values != null ? values.length : 0;
				if (count > 1) {
					isSuccess = true;
					high = values[0];
					low = values[1];
					rate = values[2];
					handler.removeMessages(Constants.MEASURE_TIMEOUT);

					Message message = handler.obtainMessage();
					message.what = Constants.MEASURE_COMPLETE;
					handler.sendMessage(message);
				}
			}
			else if (state > 0) {
				if (!isError) {
					errorShow(state - 1, detail);
				}
			}

		}
		else if (cmd == 5) {
			staticBP = detail;
			tvStatic.setText(StringConcatenationThree(staticBP));
		}
		else {
			isSuccess = false;
		}
	}

	@Override
	public void popBackStack() {
		mActivity.popBackTip();
	}

	@Deprecated
	public static String StringConcatenationThree(String initData) {
		if (initData != null) {
			int value = Integer.valueOf(initData);
			if (value >= 0 && value <= 9) {
				return "00" + value;
			}
			else if (value > 9 && value < 100) {
				return "0" + value;
			}
			else {
				return "" + value;
			}
		}
		else {
			return "error";
		}
	}
}
