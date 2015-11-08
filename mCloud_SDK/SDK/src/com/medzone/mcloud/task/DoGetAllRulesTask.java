package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamBase;

class DoGetAllRulesTask extends MCloudBaseTask {

	public DoGetAllRulesTask(int requestCode, OAuthParamBase param) {
		super(requestCode, param);
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doGetAllRules();
	}

}
