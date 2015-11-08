package com.medzone.mcloud.base;

import java.util.ArrayList;
import java.util.List;

import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.utils.RefResourceUtil;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShowDeviceAdapter extends BaseAdapter {

	public List<String>		_list;
	private LayoutInflater	inflater;
	private Context			context;
	private Dialog			mParentDialog;
	private String			mDefault;

	public ShowDeviceAdapter(Context context) {
		this.context = context;
		_list = new ArrayList<String>();
		inflater = LayoutInflater.from(context);
	}

	public void setParent(Dialog parent) {
		mParentDialog = parent;
	}

	public void setDefault(String def) {
		mDefault = def;
	}

	@Override
	public int getCount() {
		return _list == null ? 0 : _list.size();
	}

	@Override
	public Object getItem(int position) {
		return _list == null ? null : _list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return _list == null ? 0 : position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(RefResourceUtil.getLayoutId(context, "dialog_global_device_list"), null);
			holder.tv_device = (TextView) convertView.findViewById(RefResourceUtil.getId(context, "show_device_name"));
			holder.btn_link = (TextView) convertView.findViewById(RefResourceUtil.getId(context, "show_device_link"));
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		String splits[] = _list.get(position).split(":");
		StringBuffer devAddress = new StringBuffer(mDefault);
		if (splits.length > 2) {
			devAddress.append("_");
			devAddress.append(splits[splits.length - 2]);
			devAddress.append(splits[splits.length - 1]);
		}
		if (position == 0) {
			devAddress.append(context.getResources().getString(RefResourceUtil.getStringId(context, "default_device")));
			holder.btn_link.setTextColor(RefResourceUtil.getColorId(context, "gray"));
			holder.btn_link.setBackgroundColor(color.white);
			holder.btn_link.setText(context.getResources().getString(RefResourceUtil.getStringId(context, "not_found")));
			// 设置“未发现”不能被点击
			holder.btn_link.setEnabled(false);
		}
		else {
			devAddress.append("          ");
			holder.btn_link.setText(context.getResources().getString(RefResourceUtil.getStringId(context, "connection")));
			// 此处需将之前的不可点击更变为可点击否者搜索到的设备将不能被点击
			holder.btn_link.setEnabled(true);
			holder.btn_link.setBackgroundResource(RefResourceUtil.getDrawableId(context, "selector_button_group_orange"));
		}
		holder.tv_device.setText(devAddress);

		holder.btn_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MeasureActivity) context).open(_list.get(position));
				if (mParentDialog != null) {
					mParentDialog.dismiss();
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		private TextView	tv_device, btn_link;
	}
}
