package com.medzone.mcloud.network;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.utils.Args;

/**
 * 
 * @author Robert.
 * 
 *         <p>
 *         <b> Provide a template operation，(TODO)here define more templates.
 *         </p>
 *         <p>
 *         </b> Check the client installation and unloading behavior, business
 *         logic is reserved to the concrete operation in the API.
 *         </p>
 * */

public class NetworkClientManagerProxy {

	protected static String		mApiHost			= null;

	public static final String	RESOURCE_VERSION	= "/api/version";

	private static Context		mContext;
	private static String		mBaseUri;
	private static String		mAppVersion;
	protected static Handler	mApiHostHandler;

	public static void init(Context context, String apiHost, String appVersion, Handler handler) {
		mContext = context;
		mApiHost = apiHost;
		mAppVersion = appVersion;
		mApiHostHandler = handler;
		Log.i(Log.CORE_FRAMEWORK, "appVersion:" + appVersion);

	}

	protected static String getAppVersion() {
		return mAppVersion;
	}

	public static String getBaseUri() {
		return mBaseUri;
	}

	/**
	 * 
	 * @param uri
	 *            短链接的形式，比如：/app/services?access_token=%s
	 * @param params
	 *            %s对应参数化的值
	 * @return 完整的网址
	 */
	public static String getWebSitePath(String uri, Object... params) {

		StringBuilder builder = new StringBuilder();
		builder.append(NetworkClientManager.getInstance().viewURL);
		if (!TextUtils.isEmpty(uri)) {
			builder.append(uri);
		}
		return String.format(builder.toString(), params);
	}

	public static BaseResult call(String uri, NetworkParams.Builder params) {
		Args.notNull(mAppVersion, "mAppVersion");
		params.setAppVersion(mAppVersion);
		return NetworkClientManager.getInstance().call(uri, params, true);
	}

	protected static BaseResult callWithoutCheckState(String uri, NetworkParams.Builder params) {
		Args.notNull(mAppVersion, "mAppVersion");
		params.setAppVersion(mAppVersion);
		return NetworkClientManager.getInstance().call(uri, params, false);
	}

	public static boolean isServerReady() {
		return mBaseUri != null;
	}

	public static void readyClient(String uri) {
		Args.notNull(mContext, "context");

		if (uri != null) {
			mBaseUri = uri;
			JsonRestClientAdapter client = new JsonRestClientAdapter(uri);
			NetworkClientManager.getInstance().loadClient(client);
		}

		// TODO Check the response,choose a channel with good response.
	}

	public static void discardClient() {
		mBaseUri = null;
		NetworkClientManager.getInstance().loadClient(null);
		Log.w(Log.CORE_FRAMEWORK, "readyClient failed ,discard it .");
	}

}
