package com.medzone.mcloud.dialog;

import android.content.Context;

public abstract class DialogPage implements IDialogView {

	protected Context	mContext;

	public DialogPage(Context context) {
		this.mContext = context;
		prepareData();
	}

	public abstract void prepareData();
}
