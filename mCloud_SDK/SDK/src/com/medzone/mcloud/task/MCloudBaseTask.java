package com.medzone.mcloud.task;

import com.medzone.mcloud.data.errorcode.CodeProxy;
import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.network.task.BaseTask;
import com.medzone.mcloud.oauth.OAuthParamBase;

class MCloudBaseTask extends BaseTask {

	OAuthParamBase	param;

	public MCloudBaseTask(int requestCode, OAuthParamBase param) {
		super(requestCode);
		this.param = param;
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		BaseResult result = new BaseResult();
		result.setErrorCode(CodeProxy.CODE_REQUEST_FAILED);
		result.setErrorMessage("you must override doInBackground().");
		return result;
	}

}
