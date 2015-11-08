package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamBase;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataSuggest;

class DoGetURIDataSuggestTask extends MCloudBaseTask {

	public DoGetURIDataSuggestTask(int requestCode, OAuthParamBase param) {
		super(requestCode, param);
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doGetURIDataSuggest((OAuthParamGetURIDataSuggest) param);
	}

}
