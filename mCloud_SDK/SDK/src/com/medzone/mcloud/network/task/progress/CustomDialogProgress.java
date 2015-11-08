package com.medzone.mcloud.network.task.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.medzone.mcloud.network.task.Progress;
import com.medzone.mcloud.utils.RefResourceUtil;

/**
 * @author Robert
 * 
 */
public class CustomDialogProgress implements Progress {

	private Context					context			= null;
	private CustomProgressDialog	customDialog	= null;
	private CharSequence			message			= "";

	private long					startTimeMillis, endTimeMillis;

	public CustomDialogProgress(Context context) {
		this.context = context;
	}

	public CustomDialogProgress(Context context, CharSequence message) {
		this.context = context;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 */

	public boolean isAvailable() {
		return customDialog != null;
	}

	/*
	 * (non-Javadoc)
	 */

	public void startProgress() {
		if (customDialog == null && context != null) {
			customDialog = CustomProgressDialog.createDialog(context);
			customDialog.setMessage(message);
			/**
			 * XXX 当Task执行后，当前Activity已经被销毁，此时呈现会报出：window application token
			 * null的异常。
			 * 解决这个问题更好的办法是，在Activity界面合理的取消一些异步事件来避免该情况的发生。
			 */
			try {
				customDialog.show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void finishProgress() {
		if (isAvailable()) {
			endTimeMillis = System.currentTimeMillis();
			long interval = endTimeMillis - startTimeMillis;
			if (interval < 500) {
				try {
					Thread.sleep(500 - interval);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			customDialog.dismiss();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.ishow.beycare.task.IProgress#updateMessage(java.lang.String)
	 */

	public void updateProgressMessage(CharSequence message) {
		if (isAvailable()) {
			customDialog.setMessage(message);
		}

	}

	public static class CustomProgressDialog extends Dialog {
		private Context						context					= null;
		private static CustomProgressDialog	customProgressDialog	= null;

		public CustomProgressDialog(Context context) {
			this(context, 0);
		}

		public CustomProgressDialog(Context context, int theme) {
			super(context, theme);
			this.context = context;
		}

		public static CustomProgressDialog createDialog(Context context) {

			final int CustomProgressDialog = RefResourceUtil.getStyleId(context, "CustomProgressDialog");
			final int progress_dialog_loading = RefResourceUtil.getLayoutId(context, "progress_dialog_loading");

			customProgressDialog = new CustomProgressDialog(context, CustomProgressDialog);
			customProgressDialog.setContentView(progress_dialog_loading);
			customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

			return customProgressDialog;
		}

		public void onWindowFocusChanged(boolean hasFocus) {

			if (customProgressDialog == null) {
				return;
			}

		}

		public CustomProgressDialog setTitile(String strTitle) {
			return customProgressDialog;
		}

		public void setMessage(CharSequence message) {
			if (this.context == null) {
				return;
			}
			final int tvMessageId = RefResourceUtil.getId(this.context, "progress_tv_message");
			TextView tvMsg = (TextView) customProgressDialog.findViewById(tvMessageId);

			if (tvMsg != null) {
				tvMsg.setText(message);
			}
		}
	}

	@Override
	public void updateProgress(Integer value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setIndeterminate(boolean indeterminate) {
		// TODO Auto-generated method stub
	}

}
