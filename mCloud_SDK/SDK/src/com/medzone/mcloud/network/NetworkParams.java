package com.medzone.mcloud.network;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NetworkParams {

	private int			connectTimeOut;
	private int			socketTimeOut;
	private HttpMethod	httpMethod;
	private HttpEntity	httpEntity;
	private String		accessToken;
	private JSONObject	jsonObject;
	private String		appVersion;

	@Override
	public String toString() {
		String mid = ":";
		String end = "；";
		StringBuilder builder = new StringBuilder();
		builder.append("connectTimeOut");
		builder.append(mid);
		builder.append(connectTimeOut);
		builder.append(end);
		builder.append("socketTimeOut");
		builder.append(mid);
		builder.append(socketTimeOut);
		builder.append(end);
		builder.append("httpMethod");
		builder.append(mid);
		builder.append(httpMethod);
		builder.append(end);
		builder.append("accessToken");
		builder.append(mid);
		builder.append(accessToken);
		builder.append(end);
		builder.append("jsonObject");
		builder.append(mid);
		builder.append(jsonObject != null ? jsonObject.toString() : null);
		builder.append(end);
		builder.append("appVersion");
		builder.append(mid);
		builder.append(appVersion);
		builder.append(end);
		return builder.toString();
	}

	public static Builder obtainBuilder() {
		return new Builder();
	}

	private NetworkParams(Builder builder) {
		connectTimeOut = builder.connectTimeOut;
		socketTimeOut = builder.socketTimeOut;
		httpMethod = builder.httpMethod;
		httpEntity = builder.httpEntity;
		accessToken = builder.accessToken;
		jsonObject = builder.jsonObject;
		appVersion = builder.appVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public int getSocketTimeOut() {
		return socketTimeOut;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public static class Builder {

		private static final int	DEFAULT_CONNECTION_TIMEOUT	= 3 * 1000;
		private static final int	DEFAULT_SOCKET_TIMEOUT		= 8 * 1000;

		private int					connectTimeOut				= DEFAULT_CONNECTION_TIMEOUT;
		private int					socketTimeOut				= DEFAULT_SOCKET_TIMEOUT;
		private HttpMethod			httpMethod					= HttpMethod.POST;
		private HttpEntity			httpEntity;
		private String				accessToken;
		private JSONObject			jsonObject;
		private String				appVersion;

		public Builder setConnectTimeOut(int connectTimeOut) {
			this.connectTimeOut = connectTimeOut;
			return this;
		}

		public Builder setSocketTimeOut(int socketTimeOut) {
			this.socketTimeOut = socketTimeOut;
			return this;
		}

		public Builder setHttpMethod(HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		public Builder setHttpEntity(HttpEntity httpEntity) {
			this.httpEntity = httpEntity;
			return this;
		}

		public Builder setHttpEntity(JSONObject params) {
			this.jsonObject = params;
			setHttpEntity(createHttpEntity(params));
			return this;
		}

		public Builder setAppVersion(String appVersion) {
			this.appVersion = appVersion;
			return this;
		}

		HttpEntity createHttpEntity(JSONObject params) {
			if (params != null) {
				try {
					if (params.has("access_token") && !params.isNull("access_token")) {
						this.accessToken = params.getString("access_token");
					}
				}
				catch (JSONException e) {
					// TODO: handle exception
				}

				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(params.toString(), type);
				for (String key : map.keySet()) {
					list.add(new BasicNameValuePair(key, map.get(key)));
				}
				try {
					return new UrlEncodedFormEntity(list, HTTP.UTF_8);
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public NetworkParams build() {
			return new NetworkParams(this);
		}
	}
}
