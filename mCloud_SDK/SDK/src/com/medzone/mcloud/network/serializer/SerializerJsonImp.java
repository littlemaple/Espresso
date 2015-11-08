package com.medzone.mcloud.network.serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.medzone.mcloud.logging.Log;

public class SerializerJsonImp implements ISerializer<Object> {

	public SerializerJsonImp() {
	}

	@Override
	public String serialize(Object obj) {
		if (obj == null) return new String("");
		if (obj instanceof JSONObject) return ((JSONObject) obj).toString();
		return ((JSONArray) obj).toString();
	}

	@Override
	public Object deserialize(String json) {
		try {
			return new JSONObject(json);
		}
		catch (JSONException e) {
			try {
				// 请求中去检查格式，不是好的选择
				// 检查如果存在类型为Array则一并转化为JSONObject
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("root", new JSONArray(json));
				return jsonObj;
			}
			catch (Exception e1) {

				Log.v(Log.CORE_FRAMEWORK, "deserialize$failed:" + json);
			}
		}
		return null;
	}

}
