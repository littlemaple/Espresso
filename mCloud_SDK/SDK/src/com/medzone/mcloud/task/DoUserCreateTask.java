package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamBase;
import com.medzone.mcloud.oauth.OAuthParamUserCreate;

class DoUserCreateTask extends MCloudBaseTask {

	public DoUserCreateTask(int requestCode, OAuthParamBase param) {
		super(requestCode, param);
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doUserCreate((OAuthParamUserCreate) param);
	}

}
