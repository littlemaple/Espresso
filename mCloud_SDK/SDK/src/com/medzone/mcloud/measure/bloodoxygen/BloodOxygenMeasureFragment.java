package com.medzone.mcloud.measure.bloodoxygen;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.background.BluetoothMessage;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.CloudDevice;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.measure.bloodoxygen.widget.OxygenHistogramViewUtil;
import com.medzone.mcloud.measure.bloodoxygen.widget.OxygenWaveViewUtil;
import com.medzone.mcloud.utils.RefResourceUtil;
import com.medzone.mcloud.utils.WakeLockUtil;

/**
 * 血氧测量
 * 
 * @author ZhangYan
 * @time 2014-11-6
 * @file BloodOxygenMeasureFragment.java
 */
public class BloodOxygenMeasureFragment extends BluetoothFragment implements OnClickListener {

	// ------------------------------------view-----------------------------------------//
	private Button					btnCompleteOxygenMeasure;
	private LinearLayout			histogramViewLayout;		// 柱状图的布局
	private OxygenHistogramViewUtil	histogramView;				// 柱状图
	private TextView				tvOxygen, tvRate;
	private View					view;
	private OxygenWaveViewUtil		waveView;					// 波形
	private LinearLayout			waveViewLayout;			// 画波形的布局

	// ------------------------------------flag-----------------------------------------//
	private boolean					mErrMeasure	= false;
	private boolean					isPause		= false;
	private boolean					isComplete	= false;

	// -------------------------global object--------------------------//
	private String					detail;
	private String					oxygen, rate, deviceId;
	private CloudDevice				attachDevice;
	private MeasureActivity			mActivity;
	private String					boData		= "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MeasureActivity) activity;
	}

	private void initData() {
		attachDevice = ((MeasureActivity) getActivity()).getDevice();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initData();
		view = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_oxygen_measure"), container, false);
		initActionBar();
		tvOxygen = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_measure_value_oxygenTV"));
		tvRate = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_measure_value_rateTV"));
		waveViewLayout = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "oxwave_ly"));
		histogramViewLayout = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "pulse_wave_histogram"));
		btnCompleteOxygenMeasure = (Button) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_measure_complete"));
		return view;
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
		View view = LayoutInflater.from(getActivity()).inflate(RefResourceUtil.getLayoutId(getActivity(), "custom_actionbar_with_image"), null);
		TextView title = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_title"));

		ImageButton leftButton = (ImageButton) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_left"));
		leftButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "personalinformationview_ic_cancel"));
		leftButton.setOnClickListener(this);
		actionBar.setCustomView(view, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnCompleteOxygenMeasure.setVisibility(View.VISIBLE);
		btnCompleteOxygenMeasure.setOnClickListener(this);
		initWaveForm();// 初始化波形
	}

	@Override
	public void onStart() {
		super.onStart();
		WakeLockUtil.acquireWakeLock(getActivity());
		if (!isPause) {
			sendStartMeasureBroadCast(attachDevice);// 启动测量
		}
	}

	@Override
	public void onResume() {
		isPause = false;
		super.onResume();
		if (OxygenWaveViewUtil.oxWaveHandler == null || OxygenHistogramViewUtil.oxHistogramHandler == null || TextUtils.isEmpty(detail)) return;
		if (isComplete) initDataChart();
	}

	@Override
	public void onPause() {
		isPause = true;
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		WakeLockUtil.releaseWakeLock();
		stopMeasure();
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		stopMeasure();
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == RefResourceUtil.getId(getActivity(), "oxygen_measure_complete")) {
			toResultFragment();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			popBackStack();
		}

	}

	// ----------------------------------初始化波形----------------------------------//
	private Point getScreenSize() {
		Point point = new Point();
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
			display.getSize(point);
		}
		else {
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

	private void initWaveForm() {
		waveView = new OxygenWaveViewUtil(mActivity, null, getScreenSize().x, getScreenSize().y);
		waveViewLayout.addView(waveView);

		histogramView = new OxygenHistogramViewUtil(mActivity, null, getScreenSize().x, getScreenSize().y);
		histogramViewLayout.addView(histogramView);
		histogramViewLayout.setBackgroundColor(OxygenHistogramViewUtil.REPORT_SHARE_MESSAHE);
	}

	private void initDataChart() {

		if (OxygenWaveViewUtil.oxWaveHandler != null) {
			// 绘画折线图与柱状图的数据
			Message oxWaveDataMsg = OxygenWaveViewUtil.oxWaveHandler.obtainMessage();
			if (oxWaveDataMsg != null) {
				oxWaveDataMsg.obj = detail;
				oxWaveDataMsg.sendToTarget();
			}
		}

		if (OxygenHistogramViewUtil.oxHistogramHandler != null) {
			Message oxHistogramMsg = OxygenHistogramViewUtil.oxHistogramHandler.obtainMessage();
			if (oxHistogramMsg != null) {
				oxHistogramMsg.obj = detail;
				oxHistogramMsg.sendToTarget();
			}
		}
	}

	// ----------------------------------测量完成操作----------------------------------//
	private void toResultFragment() {
		if (mActivity.dialog != null) mActivity.dialog.dismiss();
		if (TextUtils.isEmpty(oxygen)) oxygen = "0";
		if (TextUtils.isEmpty(rate)) rate = "0";
		Bundle bundle = new Bundle();
		bundle.putString(Constants.OXYGEN, oxygen);
		bundle.putString(Constants.RATE, rate);
		bundle.putString(Constants.DEVICE_ID, deviceId);
		mActivity.presentFragment(mActivity.getMeasureProxy().getResultView(bundle));
		mActivity.setMeasureCompleted();
	}

	// ----------------------------------与血氧仪器交互----------------------------------//
	@Override
	public void handleMessage(Message msg) {
		if (isDetached() && !isVisible()) return;

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
			case BluetoothMessage.msg_socket_disconnected:
				stopMeasure();
				showBlueToothDisconnectDialog();
				break;
			}
		}
			break;
		}
	}

	private void registerDataDealWith2(int cmd, int state, String detail) {
		if ((cmd == 2) && (state != 0)) {
			if (!isDetached()) chooseErrorDialog(state, detail);
		}
		else if (cmd == 1 && state == 0) {
			deviceId = detail;
		}
		else if ((cmd == 0) && (state == -1)) {
			if (!isDetached()) chooseErrorDialog(state, getString(RefResourceUtil.getStringId(getActivity(), "insert_spo2")));
		}
		else if (cmd == 2 && state == 0) {
			String[] values = detail.split(";");
			int count = values != null ? values.length : 0;
			if (count <= 1) return;
			if (dialog != null && dialog.isShowing() && !isLowBattery) {
				type = 0;
				dialog.dismiss();
			}
			// isSuccess = true;
			oxygen = values[0];
			rate = values[1];

			fillView();
		}
		else if (cmd == 6) {
			if (dialog != null && dialog.isShowing() && !isLowBattery) {
				dialog.dismiss();
				type = 0;
			}
			this.detail = detail;
			isComplete = true;
			initDataChart();
		}
		else {
			// isSuccess = false;
		}
	}

	private void fillView() {
		if (!isVisible()) return;
		if (Integer.valueOf(oxygen) > 0) {
			tvOxygen.setText(oxygen);
		}
		else {
			tvOxygen.setText(getString(RefResourceUtil.getStringId(getActivity(), "no_value")));
		}
		if (Integer.valueOf(rate) > 0) {
			tvRate.setText(rate);
		}
		else {
			tvRate.setText(getString(RefResourceUtil.getStringId(getActivity(), "no_value")));
		}
		if (Integer.valueOf(oxygen) > 0 && Integer.valueOf(rate) > 0) {
			mErrMeasure = false;
			btnCompleteOxygenMeasure.setEnabled(true);
		}
		else {
			btnCompleteOxygenMeasure.setEnabled(false);
		}

	}

	private void longMeasureData(String detail) {
		boData = boData + detail + "|";
	}

	// ----------------------------------错误对话框----------------------------------//
	// ----------------------------------全局对话框，显示最后一条错误信息----------------------------------//
	private Dialog	dialog;
	/**
	 * @attention 对话框类型，属于同一种错误信息就不重新弹出 1---Meausre Exception 2---Bluetooth
	 *            Disconnect 3---Low Battery
	 */
	private int		type			= 0;
	/**
	 * 低电量情况下不再继续测量
	 */
	private boolean	isLowBattery	= false;

	/**
	 * @attention text should used with listener
	 * @param title
	 * @param content
	 * @param positiveText
	 *            确定按钮文本
	 * @param negativeText
	 *            取消按钮文本
	 * @param positiveListener
	 *            确定按钮事件
	 * @param negativeListener
	 *            取消按钮事件
	 */
	private void showBaseErrorDialog(String title, String content, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(content);
		builder.setTitle(title);
		if (positiveListener != null) builder.setPositiveButton(positiveText, positiveListener);
		if (negativeListener != null) builder.setNegativeButton(negativeText, negativeListener);
		if (mActivity == null) return;
		if (isDetached()) return;
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			type = 0;
		}
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				type = 0;
			}
		});
		dialog = builder.create();
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				type = 0;
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showLowBatteryDialog(String content) {
		if (type != 3) {
			showBaseErrorDialog(getString(RefResourceUtil.getStringId(getActivity(), "low_battery")), getString(RefResourceUtil.getStringId(getActivity(), "ear_low_battery")),
					getString(RefResourceUtil.getStringId(getActivity(), "public_submit")), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mActivity.finish();
							type = 0;
							isLowBattery = false;
						}
					}, null, null);
			type = 3;
		}
	}

	private void showMeasureExceptionDialog(String content) {
		if (type != 1) {
			showBaseErrorDialog(getString(RefResourceUtil.getStringId(getActivity(), "measure_abnormal")), getString(RefResourceUtil.getStringId(getActivity(), "insert_your_glu")),
					getString(RefResourceUtil.getStringId(getActivity(), "continue_measure")), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							type = 0;
							sendStartMeasureBroadCast(attachDevice);
						}
					}, getString(RefResourceUtil.getStringId(getActivity(), "action_exitmeasure")), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							type = 0;
							mActivity.finish();
						}
					});
			type = 1;
		}
	}

	private void showBlueToothDisconnectDialog() {
		if (type != 2) {

			showBaseErrorDialog(getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_connection_error")), getString(RefResourceUtil.getStringId(getActivity(), "bluetooth_disconnect")),
					getString(RefResourceUtil.getStringId(getActivity(), "reconnect")), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							type = 0;
							mActivity.presentFragment(mActivity.getMeasureProxy().getConnectedView(null));
						}
					}, getString(RefResourceUtil.getStringId(getActivity(), "action_exitmeasure")), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							type = 0;
							mActivity.finish();
						}
					});
			type = 2;
		}
	}

	private void chooseErrorDialog(int arg, String detail) {
		mErrMeasure = true;
		oxygen = "0";
		rate = "0";
		btnCompleteOxygenMeasure.setEnabled(false);
		if (isDetached()) return;
		switch (arg) {
		case 1:
		case 3:
			if (!isLowBattery) {
				showLowBatteryDialog(detail);
				sendPauseMeasure();
				stopMeasure();
				isLowBattery = true;
			}
			break;
		case 2:
		case -1:
			if (!isLowBattery) showMeasureExceptionDialog(detail);
			break;
		default:
			break;
		}
	}

	private void stopMeasure() {
		sendPauseMeasure();
	}

	@Override
	public void popBackStack() {
		mActivity.popBackTip();
	}

}
