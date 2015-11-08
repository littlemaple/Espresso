package com.medzone.mcloud.measure.bloodpressure;

import java.text.DecimalFormat;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.base.BluetoothFragment;
import com.medzone.mcloud.base.bean.BaseMeasureDataUtil;
import com.medzone.mcloud.base.bean.BloodPressure;
import com.medzone.mcloud.base.bean.Config;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.utils.RefResourceUtil;
import com.medzone.mcloud.utils.TextWatcherUtil;

/**
 * 血压结果
 * 
 * @time 2015-3-26
 * @file BloodPressureResultFragment.java
 */
public class BloodPressureResultFragment extends BluetoothFragment implements View.OnClickListener {

	private LinearLayout	completeLL, againLL;
	private TextView		rateTV, highTV, lowTV, againTV, hplTV, highUnitTV, lowUnitTV, hplUnitTV;

	private MeasureActivity	mActivity;
	private EditText		readmeET;
	private ImageView		flagIV;
	private boolean			isKpa	= false;
	private BloodPressure	bloodpressure;

	private String			measureType;

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

		ImageButton rightButton = (ImageButton) view.findViewById(RefResourceUtil.getId(getActivity(), "actionbar_right"));
		rightButton.setImageResource(RefResourceUtil.getDrawableId(getActivity(), "testresultsview_ic_voicebroadcast"));
		rightButton.setOnClickListener(this);
		if (Config.isDeveloperMode) {
			rightButton.setVisibility(View.VISIBLE);
		}
		else {
			if (measureType.equals(Constants.MEASURE)) {
				rightButton.setVisibility(View.VISIBLE);
			}
			else if (measureType.equals(Constants.INPUT)) {
				rightButton.setVisibility(View.GONE);
			}
		}
		actionBar.setCustomView(view, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MeasureActivity) activity;
		measureType = mActivity.getConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_pressure_result"), container, false);
		initActionBar();
		completeLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_completeLL"));
		againLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_againLL"));
		againTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_againTV"));
		flagIV = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_flag_iv"));
		rateTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_rateTV"));
		highTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_highTV"));
		lowTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_lowTV"));
		hplTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_hplTV"));
		highUnitTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_high_unitTV"));
		lowUnitTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_low_unitTV"));
		hplUnitTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "pressure_result_hpl_unitTV"));
		readmeET = (EditText) view.findViewById(RefResourceUtil.getId(getActivity(), "ce_pressure_result_readme"));
		flagIV.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() != null) {
			String argumentHigh = getArguments().getString(Constants.HIGH_PRESSURE);
			String argumentLow = getArguments().getString(Constants.LOW_PRESSURE);
			String argumentRate = getArguments().getString(Constants.RATE);
			float high = Float.valueOf(TextUtils.isEmpty(argumentHigh) ? "0" : argumentHigh);
			float low = Float.valueOf(TextUtils.isEmpty(argumentLow) ? "0" : argumentLow);
			int rate = Integer.valueOf(TextUtils.isEmpty(argumentRate) ? "0" : argumentRate);
			String deviceId = getArguments().getString(Constants.DEVICE_ID);
			if (savedInstanceState != null && savedInstanceState.containsKey(BloodPressure.TAG)) {
				bloodpressure = (BloodPressure) savedInstanceState.get(BloodPressure.TAG);
			}
			else {
				bloodpressure = new BloodPressure();
				bloodpressure.setHigh(high);
				bloodpressure.setLow(low);
				bloodpressure.setSource(deviceId);
				bloodpressure.setMeasureUID(BaseMeasureDataUtil.createUID());
				bloodpressure.setRate(rate);
				// bloodpressure.setLocation(CloudLocationClient.getInstance().getLocationParams());
			}
		}
		fillView();
		regisiterEvent();
		doRuleMatch();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(BloodPressure.TAG, bloodpressure);
		super.onSaveInstanceState(outState);
	}

	private void fillView() {
		// 底部确认区域变更
		if (measureType.equals(Constants.MEASURE)) {
			againTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "remeasure")));
		}
		else if (measureType.equals(Constants.INPUT)) {
			againTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "reinput")));
		}
		if (isKpa) {
			highTV.setText(String.valueOf(new DecimalFormat("0.0").format(bloodpressure.getHighKPA())));
			lowTV.setText(String.valueOf(new DecimalFormat("0.0").format(bloodpressure.getLowKPA())));
			String hplKpa = new DecimalFormat("0.0").format(bloodpressure.getHighKPA() - bloodpressure.getLowKPA());
			hplTV.setText(hplKpa);
			highUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_kpa")));
			lowUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_kpa")));
			hplUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_kpa")));

		}
		else {
			highTV.setText(String.valueOf(new DecimalFormat("0").format(bloodpressure.getHigh())));
			lowTV.setText(String.valueOf(new DecimalFormat("0").format(bloodpressure.getLow())));
			hplTV.setText(String.valueOf(new DecimalFormat("0").format(bloodpressure.getHigh() - bloodpressure.getLow())));
			highUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_mmhg")));
			lowUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_mmhg")));
			hplUnitTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "pressure_unit_mmhg")));
		}
		rateTV.setText(String.valueOf(bloodpressure.getRate()));
	}

	private void regisiterEvent() {
		completeLL.setOnClickListener(this);
		againLL.setOnClickListener(this);
		readmeET.addTextChangedListener(new TextWatcherUtil(mActivity, readmeET));
	}

	private Handler	handler	= new Handler() {
								public void handleMessage(android.os.Message msg) {
									switch (msg.what) {
									case 1:
										flagIV.setVisibility(View.GONE);
										break;
									case 2:
										flagIV.setVisibility(View.VISIBLE);
										break;
									}
								};
							};

	private void doRuleMatch() {
		// Rule rule =
		// RuleController.getInstance().getRulebyData(bloodpressure);
		// bloodpressure.setAbnormal(rule.getState());
		// flagIV.setImageResource(MCloudModuleDataProxy.findChatRecordStateResource(BloodPressure.TAG,
		// bloodpressure.getAbnormal()));
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == RefResourceUtil.getId(getActivity(), "measure_bottom_completeLL")) {
			completeMeasure();
			mActivity.getMeasureProxy().onMeasureComplete(bloodpressure);
			mActivity.finish();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "measure_bottom_againLL")) {
			final String measureType = mActivity.getConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
			if (TextUtils.equals(Constants.INPUT, measureType)) {
				// mActivity.presentFragment(new Blood);
			}
			else {
				mActivity.presentFragment(mActivity.getMeasureProxy().getMeasureView(null));
			}
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			popBackStack();

		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_right")) {
		}
		else {

		}
	}

	/** 结果确认 */
	private void completeMeasure() {
		sendStopMeasure();
		mActivity.close();
	}

	@Override
	public void handleMessage(Message msg) {
	}

	@Override
	public void popBackStack() {
		mActivity.finish();
	}

}
