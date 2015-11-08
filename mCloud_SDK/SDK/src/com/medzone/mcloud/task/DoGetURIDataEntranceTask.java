package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamBase;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataEntrance;

class DoGetURIDataEntranceTask extends MCloudBaseTask {

	public DoGetURIDataEntranceTask(int requestCode, OAuthParamBase param) {
		super(requestCode, param);
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doGetURIDataEntrance((OAuthParamGetURIDataEntrance) param);
	}

}
