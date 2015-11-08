//package com.medzone.mcloud.measure.bloodpressure;
//
//import android.app.ActionBar;
//import android.app.ActionBar.LayoutParams;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.medzone.mcloud.Constants;
//import com.medzone.mcloud.base.BluetoothFragment;
//import com.medzone.mcloud.base.bean.BaseMeasureDataUtil;
//import com.medzone.mcloud.base.bean.BloodPressure;
//import com.michaelnovakjr.numberpicker.NumberPickerUtil;
//
///**
// * 血压输入
// * 
// * @author ZhangYan
// * @time 2015-3-26
// * @file BloodPressureInputFragment.java
// */
//public class BloodPressureInputFragment extends BluetoothFragment implements View.OnClickListener {
//
//	private MeasureActivity	mActivity;
//	private TextView		tvRate, tvHigh, tvLow;
//
//	private int				inputRateValue	= (int) BloodPressure.INVALID_ID;
//	private float			inputHighValue	= BloodPressure.INVALID_ID;
//	private float			inputLowValue	= BloodPressure.INVALID_ID;
//
//	private boolean			isKpa			= false;
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		mActivity = (MeasureActivity) activity;
//	}
//
//	@Override
//	protected void initActionBar() {
//		ActionBar actionBar = getActivity().getActionBar();
//		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
//		View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_actionbar_with_image, null);
//		TextView title = (TextView) view.findViewById(R.id.actionbar_title);
//
//		ImageButton leftButton = (ImageButton) view.findViewById(id.actionbar_left);
//		leftButton.setImageResource(drawable.personalinformationview_ic_cancel);
//		leftButton.setOnClickListener(this);
//
//		ImageButton rightButton = (ImageButton) view.findViewById(id.actionbar_right);
//		rightButton.setImageResource(drawable.personalinformationview_ic_ok);
//		rightButton.setOnClickListener(this);
//		actionBar.setCustomView(view, params);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setDisplayShowCustomEnabled(true);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		initActionBar();
//		View view = inflater.inflate(layout.fragment_pressure_input, container, false);
//		tvHigh = (TextView) view.findViewById(id.tv_input_high);
//		tvLow = (TextView) view.findViewById(id.tv_input_low);
//		tvRate = (TextView) view.findViewById(id.tv_input_rate);
//		return view;
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		tvHigh.setOnClickListener(this);// 收缩压
//		tvLow.setOnClickListener(this);// 舒张压
//		tvRate.setOnClickListener(this);// 心率
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//		outState.putString("bp_high", tvHigh.getText().toString());
//		outState.putString("bp_low", tvLow.getText().toString());
//		outState.putString("bp_rate", tvRate.getText().toString());
//		super.onSaveInstanceState(outState);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case id.actionbar_left:
//			// mActivity.comeBackBloodPressureConnect(this);
//			break;
//		case id.actionbar_right:
//			toResultFragment();// 输入完成
//			break;
//		case id.tv_input_high:
//			inputHigh();// 收缩压
//			break;
//		case id.tv_input_low:
//			inputLow();// 舒张压
//			break;
//		case id.tv_input_rate:
//			inputRate();// 心率
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void toResultFragment() {
//
//	}
//
//	private void inputHigh() {
//		if (isKpa) {
//			final float multiple = 10.0f;
//			float defValue = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.SYSTOLIC_PRESSURE_DEFAULT_HIGH);// default
//			float minRange = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.SYSTOLIC_PRESSURE_LOW);
//			float maxRange = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.SYSTOLIC_PRESSURE_HIGH);
//
//			if (!tvHigh.getText().toString().isEmpty()) {
//				defValue = inputHighValue;
//			}
//			final int curValue = (int) (defValue * multiple);
//			NumberPickerUtil.showDoubleNumberPicker(getActivity(), curValue, minRange, maxRange, getString(string.systolic_pressure), getString(string.pressure_unit_kpa), multiple,
//					new NumberPickerUtil.onDialogChooseListener() {
//
//						@Override
//						public void onConfirm(Object value) {
//							tvHigh.setText(String.valueOf(value));
//							// 传送测量值统一用mmhg的方式，在需要展示的地方根据单位显示不同的值
//							inputHighValue = BaseMeasureDataUtil.convertKpa2mmHg(Float.valueOf((String) value));
//						}
//
//						@Override
//						public void onCancel() {
//
//						}
//
//					});
//
//		}
//		else {
//			int defValue = Constants.SYSTOLIC_PRESSURE_DEFAULT_HIGH;
//			if (!tvHigh.getText().toString().isEmpty()) {
//				defValue = (int) inputHighValue;
//			}
//			NumberPickerUtil.showNumberPicker(getActivity(), defValue, Constants.SYSTOLIC_PRESSURE_LOW, Constants.SYSTOLIC_PRESSURE_HIGH, getString(string.systolic_pressure),
//					getString(string.pressure_unit_mmhg), new NumberPickerUtil.onDialogChooseListener() {
//
//						@Override
//						public void onConfirm(Object value) {
//							tvHigh.setText(String.valueOf(value));
//							inputHighValue = (Integer) value;
//						}
//
//						@Override
//						public void onCancel() {
//
//						}
//					});
//		}
//	}
//
//	private void inputLow() {
//		if (isKpa) {
//
//			final float multiple = 10.0f;
//			float defValue = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.DIASTOLIC_PRESSURE_DEFAULT_LOW);
//			float minRange = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.SYSTOLIC_PRESSURE_LOW);
//			float maxRange = BaseMeasureDataUtil.convertmmHg2Kpa(Constants.SYSTOLIC_PRESSURE_HIGH);
//			if (!tvLow.getText().toString().isEmpty()) {
//				defValue = inputLowValue;
//			}
//			final int curValue = (int) (defValue * multiple);
//			NumberPickerUtil.showDoubleNumberPicker(getActivity(), curValue, minRange, maxRange, getString(string.diastolic_pressure), getString(string.pressure_unit_kpa), 10.0f,
//					new NumberPickerUtil.onDialogChooseListener() {
//
//						@Override
//						public void onConfirm(Object value) {
//							tvLow.setText(String.valueOf(value));
//							// 传送测量值统一用mmhg的方式，在需要展示的地方根据单位显示不同的值
//							inputLowValue = BaseMeasureDataUtil.convertKpa2mmHg(Float.valueOf((String) value));
//						}
//
//						@Override
//						public void onCancel() {
//
//						}
//
//					});
//		}
//		else {
//			int defValue = Constants.DIASTOLIC_PRESSURE_DEFAULT_LOW;
//			if (!tvLow.getText().toString().isEmpty()) {
//				defValue = (int) inputLowValue;
//			}
//			NumberPickerUtil.showNumberPicker(getActivity(), defValue, Constants.DIASTOLIC_PRESSURE_LOW, Constants.DIASTOLIC_PRESSURE_HIGH, getString(string.diastolic_pressure),
//					getString(string.pressure_unit_mmhg), new NumberPickerUtil.onDialogChooseListener() {
//
//						@Override
//						public void onConfirm(Object value) {
//							tvLow.setText(String.valueOf(value));
//							inputLowValue = (Integer) value;
//						}
//
//						@Override
//						public void onCancel() {
//
//						}
//					});
//		}
//	}
//
//	private void inputRate() {
//		int defValue = Constants.BLOODPRESSURE_DEFAULT_RATE;
//		if (!tvRate.getText().toString().isEmpty()) {
//			defValue = Integer.valueOf(tvRate.getText().toString());
//		}
//		NumberPickerUtil.showNumberPicker(getActivity(), defValue, Constants.HEART_RATE_LOW, Constants.HEART_RATE_HIGH, getString(string.heart_rate), getString(string.heart_rate_unit),
//				new NumberPickerUtil.onDialogChooseListener() {
//
//					@Override
//					public void onConfirm(Object value) {
//						tvRate.setText(String.valueOf(value));
//						inputRateValue = (Integer) value;
//					}
//
//					@Override
//					public void onCancel() {
//
//					}
//				});
//	}
//
//	@Override
//	public void handleMessage(Message msg) {
//	}
//}
