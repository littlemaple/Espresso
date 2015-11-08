package com.medzone.mcloud.dialog.error;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.medzone.mcloud.Constants;
import com.medzone.mcloud.dialog.CloudErrorDialogFactory;
import com.medzone.mcloud.dialog.IDialogFactory;
import com.medzone.mcloud.dialog.ProxyFactory;
import com.medzone.mcloud.utils.Args;
import com.medzone.mcloud.utils.RefResourceUtil;

/**
 * 
 * 作用类似于组装者，将获取的错误信息，错误View等组件进行组装
 * 
 * @see 不稳定的类，导致了问题#4940 #5020
 * 
 */
@SuppressLint("NewApi")
public final class ErrorDialogUtil {

	private static final int		TITLE_SUFFIX		= 0;
	private static final int		CONTENT_SUFFIX		= 1;
	private static final int		POSITIVE_SUFFIX		= 2;
	private static final int		NEGATIVE_SUFFIX		= 3;
	private static final boolean	FLAG_DIALOG_PART	= true;

	private static WindowManager	windowManager;

	// release it ,if not need.
	private static View				lastContentView;
	// release it ,if not need.
	private static Dialog			dialog;

	public static View getContentView() {
		return lastContentView;
	}

	private static void removeLastViewIfExist(Context context) {
		if (lastContentView != null) {
			try {
				getWindowManager(context).removeViewImmediate(lastContentView);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				lastContentView = null;
			}
		}
	}

	private static WindowManager getWindowManager(Context context) {
		if (windowManager == null) {
			windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return windowManager;
	}

	private static OnTouchListener	onTouchListener	= new OnTouchListener() {

														@SuppressLint("ClickableViewAccessibility")
														@Override
														public boolean onTouch(View v, MotionEvent event) {
															switch (event.getAction()) {
															case MotionEvent.ACTION_UP:
																removeView();
																break;

															default:
																break;
															}
															return false;
														}
													};

	public static void showErrorDialog(Context context, String message, boolean isAutoCancel) throws NullPointerException {

		if (context == null) return;
		removeLastViewIfExist(context);
		if (TextUtils.isEmpty(message)) return;
		// 获取指定错误类别的信息
		// 经理类根据工厂类别，获取指定的工厂对象
		IDialogFactory<?> factory = ProxyFactory.getFactory(ProxyFactory.TYPE_CLOUD_ERROR);
		CloudErrorDialogPage page = (CloudErrorDialogPage) factory.createDetailPage(context, CloudErrorDialogFactory.ERROR_TYPE_NORMAL);
		CloudErrorDialogPage.Builder builder = new CloudErrorDialogPage.Builder(page);
		page = builder.addContent(message).addLoadingIcon(null).build();
		lastContentView = page.getView();
		Args.notNull(lastContentView, "lastContentView");
		lastContentView.setOnTouchListener(onTouchListener);

		if (FLAG_DIALOG_PART) {
			useDialog(context, lastContentView);
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, Constants.DIALOG_DISMISS_TIME);
		}
		else {
			// 小米4 是将WindowManager上的悬浮窗体禁用掉了
			getWindowManager(context).addView(lastContentView, getLayoutParams());
			handler.removeMessages(1);
			if (isAutoCancel) {
				handler.sendEmptyMessageDelayed(1, Constants.DIALOG_DISMISS_TIME);
			}
		}
	}

	public static void showErrorDialog(Context context, String message, int leftDrawable) throws NullPointerException {

		removeLastViewIfExist(context);
		if (TextUtils.isEmpty(message)) return;
		IDialogFactory<?> factory = ProxyFactory.getFactory(ProxyFactory.TYPE_CLOUD_ERROR);
		CloudErrorDialogPage page = (CloudErrorDialogPage) factory.createDetailPage(context, CloudErrorDialogFactory.ERROR_TYPE_NORMAL);

		CloudErrorDialogPage.Builder builder = new CloudErrorDialogPage.Builder(page);
		Drawable drawable = context.getResources().getDrawable(leftDrawable);
		page = builder.addContent(message).addLoadingIcon(drawable).build();
		lastContentView = page.getView();
		Args.notNull(lastContentView, "lastContentView");
		lastContentView.setOnTouchListener(onTouchListener);

		if (FLAG_DIALOG_PART) {
			useDialog(context, lastContentView);
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, Constants.DIALOG_DISMISS_TIME);
		}
		else {

			getWindowManager(context).addView(lastContentView, getLayoutParams());
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, Constants.DIALOG_DISMISS_TIME);
		}
	}

	public static void removeView() {

		if (FLAG_DIALOG_PART) {

			if (dialog != null && dialog.isShowing()) {
				try {
					/**
					 * FIXME 当handler从queue中获取msg执行机会时，此时移除View时,
					 * 但是View所附加的ownerActivity已经被释放了。
					 */
					dialog.dismiss();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					dialog = null;
				}
			}
		}
		else {
			if (lastContentView != null && lastContentView.getParent() != null) {
				windowManager.removeViewImmediate(lastContentView);
				handler.removeMessages(1);
			}
		}
	}

	public static void useDialog(Context context, View view) {
		try {
			Args.notNull(view, "view");
			dialog = new Dialog(context, RefResourceUtil.getStyleId(context, "DialogStyle"));
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			LayoutParams params = dialog.getWindow().getAttributes();
			params.width = LayoutParams.WRAP_CONTENT;
			params.height = LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setAttributes(params);
			// LayoutParams params = dialog.getWindow().getAttributes();
			// params.gravity = Gravity.CENTER;
			// params.height = LayoutParams.WRAP_CONTENT;
			// params.width = LayoutParams.WRAP_CONTENT;
			// params.format = PixelFormat.RGBX_8888;
			// params.alpha = 80;
			// dialog.getWindow().setAttributes(params);
			dialog.setContentView(view);
			dialog.show();
		}
		catch (Exception e) {
			// FIXME 这里经常会抛出异常，使用错误对话框时。
			e.printStackTrace();
		}
	}

	public static void useToast(Context context, View view) {
		if (context == null) return;
		final Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		try {
			toast.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void useToast(Context context, int resId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(RefResourceUtil.getLayoutId(context, "error_tip_dialog"), null);
		view.setLayoutParams(getLayoutParams());
		TextView tv = (TextView) view.findViewById(RefResourceUtil.getId(context, "tip"));
		tv.setText(resId);
		useToast(context, view);
	}

	private static LayoutParams getLayoutParams() {
		LayoutParams params = new LayoutParams();
		params.gravity = Gravity.CENTER;
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.type = LayoutParams.TYPE_PHONE;
		params.format = PixelFormat.TRANSLUCENT;
		params.alpha = 0.8f;
		return params;
	}

	@Deprecated
	private static Handler	handler	= new Handler() {
										public void handleMessage(android.os.Message msg) {
											switch (msg.what) {
											case 1:
												removeView();
												break;
											}
										};
									};
}
