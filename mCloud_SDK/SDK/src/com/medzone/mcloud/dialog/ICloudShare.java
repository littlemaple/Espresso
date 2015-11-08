package com.medzone.mcloud.dialog;

import com.medzone.mcloud.network.task.TaskHost;

public interface ICloudShare {

	@Deprecated
	public void doShare();

	@Deprecated
	public void doShareWithUrl(TaskHost taskHost);
}
