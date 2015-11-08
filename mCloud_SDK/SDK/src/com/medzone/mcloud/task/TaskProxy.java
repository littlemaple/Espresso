package com.medzone.mcloud.task;

import android.content.Context;

import com.medzone.mcloud.exception.MCloudApiRequestException;
import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.network.task.ITaskCallback;
import com.medzone.mcloud.network.task.TaskHost;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataEntrance;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataRule;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataSuggest;
import com.medzone.mcloud.oauth.OAuthParamLogin;
import com.medzone.mcloud.oauth.OAuthParamUploadAttachment;
import com.medzone.mcloud.oauth.OAuthParamUserCreate;
import com.medzone.mcloud.oauth.OAuthParamUserRecordUpload;

public final class TaskProxy {

	public static void init(Context context, String appid) {
		MCloudNetworkClient.init(context, appid);
	}

	public static void uninit() {
		MCloudNetworkClient.uninit();
	}

	public static void doLogin(OAuthParamLogin param, final ITaskCallback callback) throws MCloudApiRequestException {
		if (param.isReady()) {

			DoLoginTask loginTask = new DoLoginTask(0, param);
			loginTask.setTaskHost(new TaskHost() {
				@Override
				public void onPostExecute(int requestCode, BaseResult result) {
					super.onPostExecute(requestCode, result);
					if (callback != null) {
						callback.onComplete(result);
					}
				}
			});
			loginTask.execute();

		}
		else {
			throw new MCloudApiRequestException(String.format("%s :param is not ready.", "doLogin"));
		}
	}

	public static void doDownLoadRules(final ITaskCallback callback) {
		DoGetAllRulesTask task = new DoGetAllRulesTask(0, null);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) {
					callback.onComplete(result);
				}

			}
		});
		task.execute();
	}

	public static void doDownLoadRulesInType(OAuthParamGetURIDataRule param, final ITaskCallback callback) {

		DoGetRulesInTypeTask task = new DoGetRulesInTypeTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();

	}

	public static void doGetURIDataEntranceTask(OAuthParamGetURIDataEntrance param, final ITaskCallback callback) {

		DoGetURIDataEntranceTask task = new DoGetURIDataEntranceTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();
	}

	public static void doGetURIDataSuggestTask(OAuthParamGetURIDataSuggest param, final ITaskCallback callback) {
		DoGetURIDataSuggestTask task = new DoGetURIDataSuggestTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();
	}

	public static void doUploadAttachmentTask(OAuthParamUploadAttachment param, final ITaskCallback callback) {
		DoUploadAttachmentTask task = new DoUploadAttachmentTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();
	}

	public static void doUserCreateTask(OAuthParamUserCreate param, final ITaskCallback callback) {
		DoUserCreateTask task = new DoUserCreateTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();
	}

	public static void doUserRecordUploadTask(OAuthParamUserRecordUpload param, final ITaskCallback callback) {
		DoUserRecordUploadTask task = new DoUserRecordUploadTask(0, param);
		task.setTaskHost(new TaskHost() {
			@Override
			public void onPostExecute(int requestCode, BaseResult result) {
				super.onPostExecute(requestCode, result);
				if (callback != null) callback.onComplete(result);
			}
		});
		task.execute();
	}

}
