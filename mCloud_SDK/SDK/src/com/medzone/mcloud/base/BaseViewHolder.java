/**
 * 
 */
package com.medzone.mcloud.base;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * @author Robert.
 * 
 */
public abstract class BaseViewHolder {

	public BaseViewHolder(View rootView) {
		init(rootView);
	}

	public void recycle() {

	}

	public void fillFromItem(Object item) {

	}

	public void fillFromItem(Object item, BaseAdapter adapter) {

	}

	// 这里主要是对同一个ListView或ExpandableListView中有不同的布局
	public void fillFromItem(Object item, Object type) {

	}

	public abstract void init(View view);

	@Deprecated
	public static Uri parseResourceToUri(Context context, int resID) {
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getResources().getResourcePackageName(resID) + '/' + context.getResources().getResourceTypeName(resID) + '/'
				+ context.getResources().getResourceEntryName(resID));
	}
}