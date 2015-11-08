package com.medzone.mcloud.network.client;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.json.JSONException;

import android.text.TextUtils;

import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.network.NetworkParams;
import com.medzone.mcloud.network.exception.RestException;
import com.medzone.mcloud.network.serializer.ISerializer;
import com.medzone.mcloud.utils.Args;

public abstract class NetworkCommon<T> {

	protected static final String		KEY_ACCESS_TOKEN	= "access_token";
	protected String					mHostURI;
	protected String					mAppVersion;
	protected HashMap<String, String>	mCookies;
	protected ISerializer<?>			iSerializer;
	protected String					credentials;

	public NetworkCommon(String hostURI) {
		this.mHostURI = hostURI;
		initISerializer();
	}

	public void setSerializer(ISerializer<T> serializer) {
		this.iSerializer = serializer;
	}

	abstract void initISerializer();

	// -----------------------------Cookies-------------------------------

	protected void setCookie(String paramString) {
		String[] arrayOfString = paramString.split("=");

		if (arrayOfString.length != 2) return;

		if (mCookies == null) {
			mCookies = new HashMap<String, String>();
		}
		if (mCookies.containsKey(arrayOfString[0])) {
			mCookies.remove(arrayOfString[0]);
		}
		mCookies.put(arrayOfString[0], arrayOfString[1]);
	}

	/**
	 * 
	 * @param request
	 *            涓簉equest闄勫姞Cookies
	 */
	protected void addCookiesForRequest(HttpRequestBase request) {
		final String cookiesString = getCookiesString();
		request.removeHeaders("Cookie");
		if (cookiesString != null && cookiesString.length() > 0) {
			request.addHeader("Cookie", cookiesString);
		}
	}

	protected String getCookiesString() {
		StringBuilder cookiesStringBuilder = new StringBuilder();

		Iterator<?> cookieIterator;
		if ((this.mCookies != null) && (this.mCookies.size() > 0)) {
			cookieIterator = this.mCookies.entrySet().iterator();
			while (cookieIterator.hasNext()) {

				@SuppressWarnings("unchecked")
				Map.Entry<Object, Object> cookieEntry = (Map.Entry<Object, Object>) cookieIterator.next();
				cookiesStringBuilder.append((String) cookieEntry.getKey());
				cookiesStringBuilder.append("=");
				cookiesStringBuilder.append((String) cookieEntry.getValue());
				cookiesStringBuilder.append(";");
			}

		}
		return cookiesStringBuilder.toString();
	}

	protected void clearCookies() {
		if (mCookies != null) mCookies.clear();
	}

	/**
	 * @param resourceURI
	 *            璧勬簮閾炬帴锛岄�氬父瀹冪被浼间簬锛氣��/api/contacts/鈥�
	 * @return 瀹屾暣鐨勮姹俇RI锛岄�氬父浠栫被浼间簬锛氣�渉ttp://www.domain.com/api/contacts/鈥�
	 */
	protected URI getURI(String resourceURI) {

		if (TextUtils.isEmpty(mHostURI)) {
			try {
				throw new RestException("Please specify the address of the api host.");
			}
			catch (RestException e) {
				e.printStackTrace();
			}
		}
		if (resourceURI.contains("http://") || resourceURI.contains("https://") || resourceURI.contains("ftp://")) {
			return URI.create(resourceURI);
		}
		else {
			String requestPath = mHostURI.concat(resourceURI);
			return URI.create(requestPath);
		}
	}

	/**
	 * 
	 * @param method
	 *            濡傛灉浼犲叆鐨刴ethod闈炴硶锛屽垯榛樿浣跨敤GET銆�
	 * @param uriString
	 *            璧勬簮閾炬帴锛岄�氬父瀹冪被浼间簬锛氣��/api/contacts/鈥�
	 * @return 杩斿洖鎸囧畾method锛屽搴旂殑request瀹炰緥銆�
	 */
	protected HttpRequestBase createHttpRequest(String resource, NetworkParams params) {

		Args.notNull(params, "params");

		HttpRequestBase request;
		URI uri = getURI(resource);
		switch (params.getHttpMethod()) {
		case OPTIONS:
			request = new HttpOptions(uri);
			break;
		case GET:
			request = new HttpGet(uri);
			break;
		case HEAD:
			request = new HttpHead(uri);
			break;
		case POST:
			request = new HttpPost(uri);
			break;
		case PUT:
			request = new HttpPut(uri);
			String host = uri.getHost();
			if (!TextUtils.isEmpty(host)) {
				request.addHeader("Host", host);
				// request.addHeader("Host", "db.mcloudlife.com");
			}
			break;
		case DELETE:
			request = new HttpDelete(uri);
			break;
		case TRACE:
			request = new HttpTrace(uri);
			break;
		default:
			request = new HttpPost(uri);
			break;
		}
		if (credentials != null) {
			request.addHeader("Authorization", "Bearer " + credentials);
			Log.w(Log.CORE_FRAMEWORK, "Authorization:Bearer " + credentials);
		}
		addCookiesForRequest(request);

		return request;
	}

	// -----------------------------HttpAction---------------------

	/**
	 * 杩斿洖HttpClient瀵硅薄锛岄�氬父杩欐槸鍏ㄥ眬鍗曚緥瀵硅薄銆�
	 * 
	 * @return
	 */
	abstract HttpClient getHttpClient();

	/**
	 * 鍙戣捣涓�娆TTP浜や簰锛屽苟杩斿洖浜や簰缁撴灉銆�
	 * 
	 * @param uri
	 *            璧勬簮閾炬帴锛岃濡傦細鈥�/api/contacts/鈥�
	 * @param params
	 *            璇锋眰鍙傛暟
	 * @return
	 * @throws RestException
	 */
	abstract Object httpConnect(String uri, NetworkParams params) throws RestException;

	/**
	 * 
	 * @param response
	 *            HttpClient鎵ц鍚庡搷搴旂粨鏋溿��
	 * @return 宸茬粡琚珄@link ISerializer}瑙ｆ瀽鍚庣殑缁撴灉銆�
	 * @throws ParseException
	 * @throws IOException
	 * @throws JSONException
	 */
	abstract Object processHttpResponse(HttpResponse response) throws ParseException, IOException, JSONException;

}
