package com.medzone.mcloud.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.medzone.mcloud.utils.TaskUtil;

public abstract class BaseActivity extends FragmentActivity {

	public boolean	isActive;

	public BaseActivity() {
		super();
	}

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * A SubClass should override the following functions instead of override
	 * this function.
	 * 
	 * 
	 * @see #preLoadData();
	 * @see #preInitUI();
	 * @see #initUI();
	 * @see #postInitUI();
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preLoadData(savedInstanceState);
		preInitUI();
		initUI();
		postInitUI();
	}

	public void keyBoardCancel() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 
	 * This method will be called before {@link #onCreate(Bundle)}
	 */
	protected void preLoadData(Bundle savedInstanceState) {

	}

	/**
	 * this function will be call in {@link #onCreate(Bundle)} before
	 * {@link #initUI()}
	 * 
	 */
	protected void preInitUI() {

	}

	/**
	 * this function will be call in {@link #onCreate(Bundle)} to initialize the
	 * UI.This is where most initialization should go: calling
	 * {@link #setContentView(int)} to inflate the activity's UI, using
	 * {@link #findViewById} to programmatically interact with widgets in the UI
	 */
	protected void initUI() {

	}

	protected void postInitUI() {

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActive = true;
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isActive = false;
		System.gc();
	}

	public void finishWithAnimation() {
		super.finish();
		// TODO Define exit animation
	}

	@Override
	public void finish() {
		keyBoardCancel();
		super.finish();
	}

	@Override
	public void startActivity(Intent intent) {
		if (!TaskUtil.isMoveTaskToBack(this, intent)) intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		super.startActivity(intent);
	}

	protected void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

}
