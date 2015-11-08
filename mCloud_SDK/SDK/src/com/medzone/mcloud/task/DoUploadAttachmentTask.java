package com.medzone.mcloud.task;

import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamBase;
import com.medzone.mcloud.oauth.OAuthParamUploadAttachment;

class DoUploadAttachmentTask extends MCloudBaseTask {

	public DoUploadAttachmentTask(int requestCode, OAuthParamBase param) {
		super(requestCode, param);
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		return MCloudNetworkClient.getInstance().doUploadAttachment((OAuthParamUploadAttachment) param);
	}

}
