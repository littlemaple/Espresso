package com.medzone.mcloud.dialog;

import android.content.Context;

public interface IDialogFactory<T extends DialogPage> {

	public T createDetailPage(Context context, Object... objects);
}
