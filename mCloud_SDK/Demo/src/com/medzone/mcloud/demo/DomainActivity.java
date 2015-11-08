package com.medzone.mcloud.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.bridge.BridgeProxy;
import com.medzone.mcloud.bridge.IMCloudSdk;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.demo.R.layout;
import com.medzone.mcloud.exception.MCloudInitException;

public class DomainActivity extends Activity {

	private static final String	tag			= "domain_app";

	private DomainCallBack		callBack	= new DomainCallBack();

	void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_demo_main);
	}

	/**
	 * 模拟lib初始化
	 * 
	 * @param view
	 */
	public void onInit(View view) {
		Log.w(tag, "onInit");
		try {
			BridgeProxy.getInstance().init(getApplicationContext(), callBack);
		}
		catch (MCloudInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 模拟请求授权
	 * 
	 * @param view
	 */
	public void onAuthorized(View view) {
		Log.w(tag, "onAuthorized");
		MCloudSdkAuthorizedArgs args = new MCloudSdkAuthorizedArgs();
		args.put(MCloudSdkAuthorizedArgs.AppId, "appId");
		args.put(MCloudSdkAuthorizedArgs.AppKey, "appKey");
		args.put(MCloudSdkAuthorizedArgs.EndUserPrimaryId, "primaryId");
		BridgeProxy.getInstance().doAutherized(args);
	}

	public void onMeasureDevice(View view) {
		Log.w(tag, "onMeasureDevice");
		BridgeProxy.getInstance().measure(MCloudDevice.BP);
	}

	public void onViewDevice(View view) {
		Log.w(tag, "onViewDevice");
		BridgeProxy.getInstance().view(MCloudDevice.BP);
	}

	public void onObtainViewHistoryUrl(View view) {
		Log.w(tag, "onObtainViewHistoryUrl");
		BridgeProxy.getInstance().obtainViewHistoryUrl(MCloudDevice.BP);
	}

	public void onUninit(View view) {
		Log.w(tag, "onUninit");
		BridgeProxy.getInstance().uninit();
	}

	class DomainCallBack implements IMCloudSdk {

		@Override
		public void listenAutherizedState(MCloudSdkAuthorizedArgs args) {
			if (args != null) {
				String authState = args.getAuthorizedstate();
				String autoMessage = args.getAuthorizedMessage();
				Log.d(tag, String.format("auth state is %s,%s", authState, autoMessage));
			}
		}

		@Override
		public void listenMeasureDataState(MCloudSdkMeasureDataArgs args) {
			// TODO Auto-generated method stub

		}

	}
}
