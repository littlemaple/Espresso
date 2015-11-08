package com.medzone.mcloud.measure.bloodoxygen;

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
import com.medzone.mcloud.base.bean.BloodOxygen;
import com.medzone.mcloud.base.bean.Config;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.utils.RefResourceUtil;
import com.medzone.mcloud.utils.TextWatcherUtil;

/**
 * 血氧结果
 */
public class BloodOxygenResultFragment extends BluetoothFragment implements View.OnClickListener {

	private LinearLayout	completeLL, againLL;
	private LinearLayout	customLinearLayout;
	private TextView		rateTV, oxygenTV, againTV;
	private String			rate, oxygen, readMe, deviceId;
	private MeasureActivity	mActivity;
	private EditText		readmeET;

	private ImageView		flagIV;

	private int				state;
	private BloodOxygen		bloodoxygen;

	private String			measureType;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MeasureActivity) activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		measureType = mActivity.getConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
		View view = inflater.inflate(RefResourceUtil.getLayoutId(getActivity(), "fragment_oxygen_result"), container, false);
		initActionBar();
		completeLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_completeLL"));
		againLL = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_againLL"));
		againTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "measure_bottom_againTV"));
		flagIV = (ImageView) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_result_flag_iv"));
		rateTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_result_rateTV"));
		oxygenTV = (TextView) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_result_oxygenTV"));
		readmeET = (EditText) view.findViewById(RefResourceUtil.getId(getActivity(), "ce_oxygen_result_readme"));
		customLinearLayout = (LinearLayout) view.findViewById(RefResourceUtil.getId(getActivity(), "oxygen_container"));
		flagIV.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		oxygen = getArguments().getString(Constants.OXYGEN);
		rate = getArguments().getString(Constants.RATE);
		deviceId = getArguments().getString(Constants.DEVICE_ID);

		if (measureType.equals(Constants.MEASURE)) {
			againTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "remeasure")));
		}
		else if (measureType.equals(Constants.INPUT)) {
			againTV.setText(getString(RefResourceUtil.getStringId(getActivity(), "reinput")));
		}

		rateTV.setText(rate);
		oxygenTV.setText(oxygen);

		completeLL.setOnClickListener(this);
		againLL.setOnClickListener(this);
		readmeET.addTextChangedListener(new TextWatcherUtil(mActivity, readmeET));

		doRuleMatch();
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
	public void onClick(View v) {
		if (v.getId() == RefResourceUtil.getId(getActivity(), "measure_bottom_completeLL")) {
			completeMeasure();
			mActivity.getMeasureProxy().onMeasureComplete(bloodoxygen);
			mActivity.finish();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "measure_bottom_againLL")) {
			final String measureType = mActivity.getConfigurationValue(MeasureActivity.BUNDLE_KEY_MEASURE_TYPE, Constants.MEASURE);
			if (TextUtils.equals(Constants.INPUT, measureType)) {
			}
			else {
				mActivity.presentFragment(mActivity.getMeasureProxy().getMeasureView(null));
			}
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_left")) {
			mActivity.finish();
		}
		else if (v.getId() == RefResourceUtil.getId(getActivity(), "actionbar_right")) {

		}
		else {

		}
	}

	private void completeMeasure() {
		readMe = readmeET.getText().toString().trim();
		readMe = readmeET.getText().toString().trim();
		bloodoxygen.setReadme(readMe);
		bloodoxygen.setSource(deviceId);
		bloodoxygen.setMeasureUID(BaseMeasureDataUtil.createUID());
		bloodoxygen.setOxygen(Integer.valueOf(oxygen));
		bloodoxygen.setAbnormal(state);
		sendStopMeasure();
		mActivity.close();
	}

	private void doRuleMatch() {

		bloodoxygen = new BloodOxygen();
		bloodoxygen.setOxygen(Integer.valueOf(oxygen));
		bloodoxygen.setRate(Integer.valueOf(rate));

		// Rule rule = RuleController.getInstance().getRulebyData(bloodoxygen);
		// flagIV.setImageResource(MCloudModuleDataProxy.findChatRecordStateResource(BloodOxygen.TAG_OXY,
		// rule.getState()));
		//
		// if (readmeET.hasFocus()) {
		// flagIV.setVisibility(View.GONE);
		// }
		// else {
		// flagIV.setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popBackStack() {
		mActivity.finish();
	}

}
