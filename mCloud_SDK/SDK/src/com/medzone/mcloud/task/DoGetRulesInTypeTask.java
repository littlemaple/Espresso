package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.network.task.BaseTask;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataRule;

class DoGetRulesInTypeTask extends BaseTask {

	OAuthParamGetURIDataRule	param;

	public DoGetRulesInTypeTask(int requestCode, OAuthParamGetURIDataRule param) {
		super(requestCode);
		this.param = param;
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doGetRulesInType(param);
	}

}
