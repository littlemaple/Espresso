package com.medzone.mcloud.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.medzone.mcloud.data.errorcode.CodeProxy;
import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.network.task.BaseResult;

/**
 * 
 * @author Robert.
 * 
 */
public class NetworkClientResult extends BaseResult {

	protected JSONObject	jsonObject;

	public NetworkClientResult() {
		super();
		setErrorCode(CodeProxy.CODE_10001);
	}

	public void setResponseResult(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		if (jsonObject != null) {
			Log.d(Log.CORE_FRAMEWORK, "response:" + jsonObject.toString());
		}
		else {
			Log.d(Log.CORE_FRAMEWORK, "response:null");
		}
		checkErrorMessage(jsonObject);
	}

	public JSONObject getResponseResult() {
		return jsonObject;
	}

	private boolean isResultMapValid() {
		return jsonObject != null;
	}

	private void checkErrorMessage(JSONObject jo) {
		if (isResultMapValid()) {

			try {
				setErrorCode(CodeProxy.CODE_SUCCESS);

				if (jo.has("errcode") && !jo.isNull("errcode")) {
					Double code = jo.getDouble("errcode");
					setErrorCode(code.intValue());
				}
				if (jo.has("errmsg") && !jo.isNull("errmsg")) {
					String errorMessage = jo.getString("errmsg");
					setErrorMessage(errorMessage);
				}

				/**
				 * (TODO) 字段解析存在问题，有可能除phone或者email之外的其他键字段。
				 */
				if (jo.has("errors") && !jo.isNull("errors")) {
					JSONObject o = jo.getJSONObject("errors");
					if (o.has("phone") && !o.isNull("phone")) {
						String errorMessage = o.getString("phone");
						setErrorMessage(errorMessage);
					}
					if (o.has("email") && !o.isNull("email")) {
						String errorMessage = o.getString("email");
						setErrorMessage(errorMessage);
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else {
			setErrorCode(CodeProxy.CODE_10001);
			setErrorMessage("The result map is invalid,please check request is success?");
		}
	}
}
