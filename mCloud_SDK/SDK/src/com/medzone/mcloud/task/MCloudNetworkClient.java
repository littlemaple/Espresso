package com.medzone.mcloud.task;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.medzone.mcloud.data.errorcode.CodeProxy;
import com.medzone.mcloud.env.MCloudSdkAPIEnv;
import com.medzone.mcloud.env.MCloudSdkEnv;
import com.medzone.mcloud.logging.Log;
import com.medzone.mcloud.network.NetworkClientManagerProxy;
import com.medzone.mcloud.network.NetworkClientResult;
import com.medzone.mcloud.network.NetworkParams;
import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataEntrance;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataRule;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataSuggest;
import com.medzone.mcloud.oauth.OAuthParamLogin;
import com.medzone.mcloud.oauth.OAuthParamUploadAttachment;
import com.medzone.mcloud.oauth.OAuthParamUserCreate;
import com.medzone.mcloud.oauth.OAuthParamUserRecordUpload;
import com.medzone.mcloud.version.MCloudSdkVersion;

class MCloudNetworkClient {

	final class NetUrlCenter {
		/**
		 * 获取接口地址及版本信息
		 * 
		 * GET http://open.mcloudlife.com/version?appId={appId}
		 * // 响应结果
		 * {
		 * "apiversion": "1.0.0",
		 * "apihost": "http://open.mcloudlife.com",
		 * "ruleversion": "20150130",
		 * "message": {
		 * "title": "Tip title",
		 * "content": "Tip content",
		 * "buttons": []
		 * }
		 * }
		 * 其它 API 的地址均基于 apihost，适当比较版本号可以升级本地的
		 * 规则库，message 的用法参见原心云接口。
		 */
		protected static final String	API_HOST_FORMAT		= "http://open.mcloudlife.com/version?appId=%s";
		/**
		 * 登录认证，换取 access_token
		 * 
		 * POST /user/login
		 * // 除 timestamp, nonce, signature 外，其它参数：
		 * {
		 * "appId":"第三方AppID",
		 * "uid":"第三方用户ID"
		 * }
		 * // 返回结果
		 * {
		 * "access_token":"TOKEN内容，不超过64字节",
		 * "expire_in":"多少秒后过期",
		 * }
		 * // 出错代码
		 * 4000: 无效的 appId
		 * 4001: 校验码不正确
		 * 4100: 用户id 不存在
		 * 5000: 云端内部错误（通常是数据库）
		 */
		protected static final String	URI_LOGIN			= "/user/login";
		/**
		 * 创建帐号
		 * 
		 * POST /user/create
		 * // 除 timestamp, nonce, signature 外，其它参数：
		 * {
		 * "appId":"第三方AppID",
		 * "uid":"第三方用户ID，必须提供",
		 * "phone":"手机号码",
		 * "gender":"性别，男/女",
		 * "birthday":"生日，YYYY-MM-DD",
		 * "username":"用户姓名，不超过20字节",
		 * "idcode":"身份证号",
		 * "email":"电子邮箱",
		 * "location":"所在地区，不超过40字节"
		 * }
		 * // 响应结果
		 * {
		 * "access_token":"TOKEN内容，不超过64字节",
		 * "expire_in":"多少秒后过期",
		 * }
		 * // 出错代码
		 * 4000: 无效的 appId
		 * 4001: 校验码不正确
		 * 4200: 提交的数据有误（原因）
		 * 4300: 测量模块不存在
		 * 5000: 云端内部错误（通常是数据库）
		 */
		protected static final String	URI_USER_CREATE		= "/user/create";
		/**
		 * 上传数据，需要提供 Bearer 认证信息
		 * 
		 * POST /user/record
		 * // 无需提供校验码，其它参数：
		 * {
		 * "type":"测量类型，如bp,bs,et,oxy,...",
		 * "up_data":[
		 * {
		 * "measureuid":"测量UUID",
		 * "source":"设备ID",
		 * "readme":"备注",
		 * "voiceme":BASE64(voice_data),
		 * "x":102.3,
		 * "y":67.5,
		 * "z":731.1,
		 * "state":1,
		 * "value1":120,
		 * "value2":85,
		 * "value3":72,
		 * ...
		 * },
		 * ...
		 * ]
		 * }
		 * // 响应结果，列出成功存入的 UUID
		 * {
		 * "type":"bp",
		 * "up":[
		 * {"measureuid":"测量UUID-1", "recordid":"云端ID-1"},
		 * {"measureuid":"测量UUID-2", "recordid":"云端ID-2"},
		 * ...
		 * ]
		 * }
		 * // 出错代码
		 * 4001: 校验码不正确
		 * 4002: access_token 无效
		 * 4200: 提交的数据有误（附原因，仅在只上传一条数据时抛出）
		 * 5000: 云端内部错误（通常是数据库）
		 * note: 如果数据已存在，并不会产生错误；如果属主相同则更新数据，
		 * 若属主不同则静默忽略上传。
		 */
		protected static final String	URI_USER_RECORD		= "/user/record";

		/**
		 * 获取数据查看入口网址，需要提供认证信息
		 * 
		 * GET /data/entrance/{type}
		 * // 无需提供校验码，无其它参数。
		 * // 响应结果
		 * {
		 * "url":"http://...完整入口地址，不包含 access_token 参数"
		 * }
		 * // 出错代码
		 * 4002: access_token 无效
		 * 4300: 模块不存在
		 * 5000: 云端内部错误（通常是数据库）
		 * note: 若要将网址，供第三方 APP 直接使用，请在末尾
		 * 补充 access_token 参数。
		 */
		protected static final String	URI_DATA_ENTRANCE	= "/data/entrance/%s";

		/**
		 * 获取单条数据建议，需要提供认证信息
		 * 
		 * GET /data/suggest/{type}/{recordid|measureuid}
		 * // 无需提供校验码，没有其它参数。
		 * // 响应结果
		 * {
		 * "result":"结论",
		 * "depart":"科室",
		 * "drug":"用药",
		 * "sport":"运动",
		 * "food":"饮食",
		 * "psy":"精神",
		 * "state":"健康等级",
		 * "url":"http://...嵌入网址，不带 access_token"
		 * }
		 * // 出错代码
		 * 4002: access_token 无效
		 * 4300: 测量模块不存在
		 * 4301: 数据不存在
		 * 5000: 云端内部错误（通常是数据库）
		 */
		protected static final String	URI_DATA_SUGGEST	= "/data/suggest/%s/%s";

		/***
		 * 获取规则库，用于同步本地数据校验，需要提供认证信息
		 * 
		 * GET /rules
		 * GET /rules/{type}
		 * // 无需提供校验码，无其它参数。
		 * // 响应结果
		 * {
		 * "bp":[
		 * {
		 * "conds":[
		 * {"value1":">=90"},
		 * ...
		 * ],
		 * "state":1,
		 * "result":"结论",
		 * "description":"描述"
		 * },
		 * ...
		 * ],
		 * "bs":[
		 * ],
		 * ...
		 * }
		 * // 出错代码
		 * 4002: access_token 无效
		 * 4300: 测量模块不存在
		 * 5000: 云端内部错误（通常是数据库）
		 */
		protected static final String	URI_RULEST_ALL		= "/rules";
		protected static final String	URI_RULEST			= "/rules/%s";

		/**
		 * 请求上传数据附件，需认证
		 * 
		 * POST /data/attachment
		 * // 无需提供校验码，其它参数：
		 * {
		 * "filename":"文件名：{type}-{recordid|measureuid}-{sizeX}.{ext}",
		 * "size":1024
		 * }
		 * // 响应结果
		 * {
		 * "url":"http://... 上传地址",
		 * "method":"PUT",
		 * "off":0,
		 * "len":1024,
		 * "finished":"N"
		 * }
		 * // 出错代码
		 * 4002: access_token 无效
		 * 4300: 测量模块不存在
		 * 4301: 数据不存在
		 * 5000: 云端内部错误（通常是数据库）
		 */
		protected static final String	URI_DATA_ATTACHMENT	= "/data/attachment";

	}

	private static MCloudNetworkClient	instance;

	public static void init(Context context, String appid) {
		if (instance == null) {
			instance = new MCloudNetworkClient(context, appid);
		}
	}

	// ---------------------------helper + ------------------------------

	/**
	 * 
	 * @param uri
	 *            短链接的形式，比如：/app/services?access_token=%s
	 * @param params
	 *            %s对应参数化的值
	 * @return 完整的网址
	 */
	public String getWebSitePath(String uri, Object... params) {
		return NetworkClientManagerProxy.getWebSitePath(uri, params);
	}

	// ---------------------------helper - ------------------------------

	private String getAppVersion() {
		String appVersion = MCloudSdkVersion.getVersionName();
		if (MCloudSdkEnv.sdkApiEnv == MCloudSdkAPIEnv.ENV_TRADE_TESTING) {
			/**
			 * 需要在请求的 User-Agent 中,包括WEBVIEW加入 我们的APP签 名：mCloud/2.x.y.z 如果是开发版就
			 * mCloud/2.x.y.zD 最后加个大写的D
			 */
			appVersion = appVersion.concat("D");
		}
		return appVersion;
	}

	private static Handler	receivedApiHostHandler	= new Handler() {
														@Override
														public void handleMessage(Message msg) {
															super.handleMessage(msg);
															if (msg.obj != null && msg.obj instanceof JSONObject) {
																Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, ((JSONObject) msg.obj).toString());
															}
														}
													};

	private MCloudNetworkClient(final Context context, String appid) {

		if (!(Looper.getMainLooper().getThread() == Thread.currentThread())) {
			Looper.prepare();
		}
		NetworkClientManagerProxy.init(context, String.format(NetUrlCenter.API_HOST_FORMAT, appid), getAppVersion(), receivedApiHostHandler);
	}

	/**
	 * 当确实有需要彻底关闭程序时，调用该方法保证网络客户端资源得到正确的释放。
	 */
	public static void uninit() {
		NetworkClientManagerProxy.discardClient();
	}

	public static MCloudNetworkClient getInstance() {
		return instance;
	}

	public boolean testDNS() {
		try {
			DNSResolver dnsRes = new DNSResolver("http://open.mcloudlife.com");
			Thread t = new Thread(dnsRes);
			t.start();
			t.join(1000);
			InetAddress inetAddr = dnsRes.get();
			return inetAddr != null;
		}
		catch (Exception e) {
			return false;
		}
	}

	private static class DNSResolver implements Runnable {
		private String		domain;
		private InetAddress	inetAddr;

		public DNSResolver(String domain) {
			this.domain = domain;
		}

		public void run() {
			try {
				InetAddress addr = InetAddress.getByName(domain);
				set(addr);
			}
			catch (UnknownHostException e) {
			}
		}

		public synchronized void set(InetAddress inetAddr) {
			this.inetAddr = inetAddr;
		}

		public synchronized InetAddress get() {
			return inetAddr;
		}
	}

	public BaseResult detectApiHost() {
		return NetworkClientManagerProxy.call(null, NetworkParams.obtainBuilder());
	}

	public BaseResult doLogin(OAuthParamLogin t) {
		return NetworkClientManagerProxy.call(NetUrlCenter.URI_LOGIN, NetworkParams.obtainBuilder().setHttpEntity(t.toJSONObject()));
	}

	public BaseResult doUserCreate(OAuthParamUserCreate t) {
		return NetworkClientManagerProxy.call(NetUrlCenter.URI_USER_CREATE, NetworkParams.obtainBuilder().setHttpEntity(t.toJSONObject()));
	}

	public BaseResult doUserRecordUpload(OAuthParamUserRecordUpload t) {
		return NetworkClientManagerProxy.call(NetUrlCenter.URI_USER_RECORD, NetworkParams.obtainBuilder().setHttpEntity(t.toJSONObject(false)));
	}

	public BaseResult doGetURIDataEntrance(OAuthParamGetURIDataEntrance param) {
		final String URI = String.format(NetUrlCenter.URI_DATA_ENTRANCE, param.type);
		return NetworkClientManagerProxy.call(URI, NetworkParams.obtainBuilder());
	}

	public BaseResult doGetURIDataSuggest(OAuthParamGetURIDataSuggest param) {
		String URI = NetUrlCenter.URI_DATA_SUGGEST;
		if (!TextUtils.isEmpty(param.recordid)) {
			URI = String.format(NetUrlCenter.URI_DATA_SUGGEST, param.type, param.recordid);
		}
		else if (!TextUtils.isEmpty(param.measureuid)) {
			URI = String.format(NetUrlCenter.URI_DATA_SUGGEST, param.type, param.measureuid);
		}
		else {
			NetworkClientResult result = new NetworkClientResult();
			result.setErrorCode(CodeProxy.CODE_REQUEST_FAILED);
			result.setErrorMessage("the recordid or measureuid input empty,please check it.");
			return result;
		}
		return NetworkClientManagerProxy.call(URI, NetworkParams.obtainBuilder());
	}

	public BaseResult doGetAllRules() {
		return NetworkClientManagerProxy.call(NetUrlCenter.URI_RULEST_ALL, NetworkParams.obtainBuilder());
	}

	public BaseResult doGetRulesInType(OAuthParamGetURIDataRule param) {
		final String URI = String.format(NetUrlCenter.URI_RULEST, param.type);
		return NetworkClientManagerProxy.call(URI, NetworkParams.obtainBuilder());
	}

	public BaseResult doUploadAttachment(OAuthParamUploadAttachment param) {
		return NetworkClientManagerProxy.call(NetUrlCenter.URI_DATA_ATTACHMENT, NetworkParams.obtainBuilder().setHttpEntity(param.toJSONObject(false)));
	}
}
