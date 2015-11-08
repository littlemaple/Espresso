package com.medzone.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.bridge.BridgeProxy;
import com.medzone.mcloud.bridge.IMCloudSdk;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.exception.MCloudInitException;

public class MainActivity extends Activity {

	private TextView	tv;
	private TextView	tv2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		tv = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
	}

	public void measure(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LinearLayout ll = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		params1.weight = 1;
		params2.weight = 1;
		params.topMargin = 40;
		params.bottomMargin = 40;
		Button btn1 = new Button(this);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BridgeProxy.getInstance().measure(MCloudDevice.BP);
			}
		});
		btn1.setText("BP");
		Button btn2 = new Button(this);
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BridgeProxy.getInstance().measure(MCloudDevice.BO);
			}
		});
		btn2.setText("BO");
		btn1.setLayoutParams(params1);
		btn2.setLayoutParams(params2);
		ll.setLayoutParams(params);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.addView(btn1);
		ll.addView(btn2);
		builder.setView(ll);
		builder.setTitle("please choose a measure device");
		builder.create().show();
	}

	public void unInit(View view) {
		tv.setText("");
		tv2.setText("");
		BridgeProxy.getInstance().uninit();

	}

	public void init(View view) {
		try {
			BridgeProxy.getInstance().init(this, new IMCloudSdk() {

				@Override
				public void listenMeasureDataState(MCloudSdkMeasureDataArgs args) {
					if (args == null) {
						tv.append("ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½");
					}
					else {
						tv.append(args.get(MCloudSdkMeasureDataArgs.DEVPARAMS) + "\n");
						if (args.containsKey(MCloudSdkMeasureDataArgs.MEASURE_UID)) {
							tv2.append("MeasureUID:" + args.get(MCloudSdkMeasureDataArgs.MEASURE_UID) + "\n");
							tv2.append("MeasureTime:" + args.get(MCloudSdkMeasureDataArgs.MEASURE_TIME) + "\n");
							tv2.append("ÑªÑõ£º\n");
							tv2.append("Saturation:" + args.get(MCloudSdkMeasureDataArgs.VALUE_SATURATION) + "\n");
							tv2.append("HeartRate:" + args.get(MCloudSdkMeasureDataArgs.VALUE_HEART_RATE) + "\n\n");
							tv2.append("ÑªÑ¹:\n");
							tv2.append("Systolic:" + args.get(MCloudSdkMeasureDataArgs.VALUE_SYSTOLIC) + "\n");
							tv2.append("Diaslotic:" + args.get(MCloudSdkMeasureDataArgs.VALUE_DIASTOLIC) + "\n");
							tv2.append("HeartRate:" + args.get(MCloudSdkMeasureDataArgs.VALUE_HEART_RATE) + "\n");
						}
					}
				}

				@Override
				public void listenAutherizedState(MCloudSdkAuthorizedArgs args) {
					if (args == null) {
						tv.append("authorized error");
					}
					else {
						tv.append("authorized:" + args.getAuthorizedMessage() + ">>" + args.getAuthorizedstate());
					}
				}
			});
		}
		catch (MCloudInitException e) {
			e.printStackTrace();
		}
	}

	public void authorize(View view) {
		BridgeProxy.getInstance().doAutherized(new MCloudSdkAuthorizedArgs());
	}
}
