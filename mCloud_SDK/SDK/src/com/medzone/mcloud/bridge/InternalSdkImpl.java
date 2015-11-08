package com.medzone.mcloud.bridge;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.data.errorcode.CodeProxy;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.data.eventargs.MCloudSdkMeasureDataArgs;
import com.medzone.mcloud.env.MCloudSdkAPIEnv;
import com.medzone.mcloud.env.MCloudSdkEnv;
import com.medzone.mcloud.exception.MCloudInitException;
import com.medzone.mcloud.exception.MCloudOAuthException;
import com.medzone.mcloud.measure.MeasureActivity;
import com.medzone.mcloud.measure.bloodoxygen.BloodOxygenProxy;
import com.medzone.mcloud.measure.bloodpressure.BloodPressureProxy;
import com.medzone.mcloud.network.NetworkClientResult;
import com.medzone.mcloud.network.task.BaseResult;
import com.medzone.mcloud.network.task.ITaskCallback;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataEntrance;
import com.medzone.mcloud.oauth.OAuthParamGetURIDataRule;
import com.medzone.mcloud.oauth.OAuthParamLogin;
import com.medzone.mcloud.oauth.OAuthParamLogin.CallBackParam;
import com.medzone.mcloud.task.TaskProxy;
import com.medzone.mcloud.utils.ToastUtils;

public class InternalSdkImpl implements IMCloudSdkEx {

	private InternalSdkImpl() {
	}

	public static synchronized InternalSdkImpl getInstance() {
		if (instance == null) {
			instance = new InternalSdkImpl();
		}
		return instance;
	}

	private static InternalSdkImpl	instance;
	private Context					mAppContext;
	private IMCloudSdk				mSdkProxy;
	private boolean					isInited	= false;

	/**
	 * ��Ȩ�ɹ����ڴ��б���Ĳ����б��������Ჹ��������������״̬
	 */
	private MCloudSdkAuthorizedArgs	mArgs;

	public MCloudSdkAuthorizedArgs getArgs() {
		return mArgs;
	}

	private void checkEventArgs(MCloudSdkAuthorizedArgs eventArgs) throws MCloudOAuthException, MCloudInitException {

		if (mSdkProxy == null) {
			throw new MCloudInitException(String.format("checkEventArgs's %s : cannot be empty.", "mSdkProxy"));
		}
		if (eventArgs == null || eventArgs.isEmpty()) {
			throw new MCloudOAuthException(String.format("checkEventArgs's %s : cannot be empty.", "eventArgs"));
		}
		if (TextUtils.isEmpty(eventArgs.getAppId())) {
			throw new MCloudOAuthException(String.format("checkEventArgs's %s : cannot be empty.", "ConsumerKey"));
		}
		if (TextUtils.isEmpty(eventArgs.getAppKey())) {
			throw new MCloudOAuthException(String.format("checkEventArgs's %s : cannot be empty.", "ConsumerSecret"));
		}
		if (TextUtils.isEmpty(eventArgs.getEndUserPrimaryId())) {
			throw new MCloudOAuthException(String.format("checkEventArgs's %s : cannot be empty.", "EndUserPrimaryId"));
		}
	}

	@Override
	public void listenAutherizedState(MCloudSdkAuthorizedArgs args) {
		if (mSdkProxy != null) mSdkProxy.listenAutherizedState(args);
	}

	@Override
	public void listenMeasureDataState(MCloudSdkMeasureDataArgs args) {
		if (mSdkProxy != null) mSdkProxy.listenMeasureDataState(args);
	}

	@Override
	public void init(Context appContext, IMCloudSdk sdkProxy) throws MCloudInitException {
		// ������ֵ
		if (appContext == null) {
			throw new MCloudInitException(String.format("init's %s : cannot be empty.", "context"));
		}
		if (sdkProxy == null) {
			throw new MCloudInitException(String.format("init's %s : cannot be empty.", "sdkProxy"));
		}
		this.mSdkProxy = sdkProxy;
		this.mAppContext = appContext.getApplicationContext();
		this.isInited = true;
	}

	@Override
	public boolean isInit() {
		if (!this.isInited) {
			Log.w(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, "Please call init() first before call doAuthorized()");
		}
		return this.isInited;
	}

	@Override
	public boolean isOAuthorized() {
		if (!isInit()) return false;
		if (mArgs == null || TextUtils.isEmpty(mArgs.getEndUserAccessToken())) {
			mockAuthSuccess(false, "Please doAuthorized() first before call measure().");
			return false;
		}
		return true;
	}

	/**
	 * ���õ��������ڿ����߽��е���
	 */
	public static final String	internal_appId				= "mcloud_appId_believe1n@us";
	public static final String	internal_appKey				= "mcloud_appKey_believe1n@us";
	public static final String	internal_EndUserPrimaryId	= "mcloud_EndUserPrimaryId_believe1n@us";
	public static final String	internal_EndUserAccessToken	= "mcloud_EndUserAccessToken_believe1n@us";

	private void withinPreAuthorization(MCloudSdkAuthorizedArgs args) {
		args.put(MCloudSdkAuthorizedArgs.Key_AppId, internal_appId);
		args.put(MCloudSdkAuthorizedArgs.Key_AppKey, internal_appKey);
		args.put(MCloudSdkAuthorizedArgs.Key_EndUserPrimaryId, internal_EndUserPrimaryId);
		args.put(MCloudSdkAuthorizedArgs.Key_EndUserAccessToken, internal_EndUserAccessToken);
	}

	/**
	 * ִ����Ȩ���룬��ɺ󷵻ظ������߿��õĵ�½����
	 */
	@Override
	public void doAutherized(MCloudSdkAuthorizedArgs args) {
		this.mArgs = args;
		try {
			if (MCloudSdkAPIEnv.ENV_TRADE_TESTING == MCloudSdkEnv.sdkApiEnv) {
				// ����ǿ�����ģʽ�£���ʹ������Ԥ��Ȩ�����������������Ȩ����
				withinPreAuthorization(mArgs);
			}
			// ��Ҫ�Ĳ�����Ч��У��
			checkEventArgs(args);
			// ��ʼ�������������
			TaskProxy.init(mAppContext, args.getAppId());
			// ִ��API����У���̻������Ƿ���Ч���Լ��̻����û����Ƿ񴴽����ߵ�½

			OAuthParamLogin loginParam = new OAuthParamLogin();
			loginParam.appId = args.getAppId();
			loginParam.appKey = args.getAppKey();
			loginParam.endUserId = args.getEndUserPrimaryId();

			if (loginParam.isReady()) {

				TaskProxy.doLogin(loginParam, new ITaskCallback() {

					@Override
					public void onComplete(BaseResult result) {
						String message = String.format("%s:%s", result.getErrorCode(), result.getErrorMessage());
						switch (result.getErrorCode()) {
						case CodeProxy.CODE_SUCCESS:
							NetworkClientResult res = (NetworkClientResult) result;
							JSONObject ret = res.getResponseResult();
							try {
								String access_token = (String) ret.get(CallBackParam.KEY_ACCESS_TOKEN);
								String expire_in = (String) ret.get(CallBackParam.KEY_EXPIRE_IN);
								mArgs.put(MCloudSdkAuthorizedArgs.Key_EndUserAccessToken, access_token);
								mArgs.put(MCloudSdkAuthorizedArgs.Key_ExpireIn, expire_in);
								mockAuthSuccess(true, message);
							}
							catch (JSONException e) {
								mockAuthSuccess(false, "��������Ӧ�ɹ��������ؽ����쳣��" + e.getMessage());
							}
							break;
						default:
							mockAuthSuccess(false, message);
							break;
						}
					}
				});
			}
			else {
				mockAuthSuccess(false, "����Ҫ�ṩ�����Ĳ��������������ƿ��������Ȩ��");
			}
		}
		catch (Exception e) {
			mockAuthSuccess(false, e.getMessage());
		}
	}

	@Override
	public void downloadRules() {
		if (!isOAuthorized()) {
			return;
		}
		TaskProxy.doDownLoadRules(new ITaskCallback() {

			@Override
			public void onComplete(BaseResult result) {
				switch (result.getErrorCode()) {
				case CodeProxy.CODE_SUCCESS:
					NetworkClientResult res = (NetworkClientResult) result;
					JSONObject json = res.getResponseResult();
					Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, "download rules:" + json.toString());
					mockAuthSuccess(true, String.format("%s:%s", result.getErrorCode(), result.getErrorMessage()));
					break;
				default:
					mockAuthSuccess(false, String.format("%s:%s", result.getErrorCode(), result.getErrorMessage()));
					break;
				}
			}
		});
	}

	@Override
	public void downloadRuleInType(MCloudDevice device) {
		if (!isOAuthorized()) {
			return;
		}
		if (device == null) {
			downloadRules();
		}
		else {
			final OAuthParamGetURIDataRule param = new OAuthParamGetURIDataRule();
			param.appId = mArgs.getAppId();
			param.appKey = mArgs.getAppKey();
			param.type = device.toString();
			if (param.isReady()) {
				TaskProxy.doDownLoadRulesInType(param, new ITaskCallback() {

					@Override
					public void onComplete(BaseResult result) {
						switch (result.getErrorCode()) {
						case CodeProxy.CODE_SUCCESS:
							NetworkClientResult res = (NetworkClientResult) result;
							JSONObject json = res.getResponseResult();
							Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, param.type + " download rules:" + json.toString());
							mockAuthSuccess(true, String.format("%s:%s", result.getErrorCode(), result.getErrorMessage()));
							break;
						default:
							mockAuthSuccess(false, String.format("%s:%s", result.getErrorCode(), result.getErrorMessage()));
							break;
						}
					}
				});
			}
			else {
				mockAuthSuccess(false, "����Ҫ�ṩ�����Ĳ��������������ƿ���������ع���⡣");
			}
		}
	}

	@Override
	public void measure(MCloudDevice device) {
		// if (!isOAuthorized()) {
		// return;
		// }
		if (device == null) {
			Log.w(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, "make sure device not empty. see more at MCloudDevice.class.");
			return;
		}
		if (device == MCloudDevice.BO) MeasureActivity.callMe(mAppContext, new BloodOxygenProxy(device));
		else
			MeasureActivity.callMe(mAppContext, new BloodPressureProxy(device));
	}

	@Override
	public void view(MCloudDevice device) {
		if (!isOAuthorized()) {
			return;
		}
		if (device == null) {
			Log.w(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, "make sure device not empty. see more at MCloudDevice.class.");
			return;
		}
		// ��ȡView������
		OAuthParamGetURIDataEntrance param = new OAuthParamGetURIDataEntrance();
		param.appId = mArgs.getAppId();
		param.appKey = mArgs.getAppKey();
		param.type = device.toString();
		if (param.isReady()) {
			TaskProxy.doGetURIDataEntranceTask(param, new ITaskCallback() {

				@Override
				public void onComplete(BaseResult result) {
					switch (result.getErrorCode()) {
					case CodeProxy.CODE_SUCCESS:
						NetworkClientResult res = (NetworkClientResult) result;
						JSONObject json = res.getResponseResult();
						try {
							String url = json.getString(OAuthParamGetURIDataEntrance.CallBackParam.KEY_URL);
							Log.d(com.medzone.mcloud.logging.Log.SDK_FRAMEWORK, String.format("DoGetURIDataEntranceTask get url:%s", url));
							// TODO ʹ�������ڲ���WebView���г���
							ToastUtils.show(mAppContext, "Dev:ʹ�������ڲ���WebView���г���:" + url);
						}
						catch (JSONException e) {
							mockAuthSuccess(false, e.getMessage());
						}
						break;
					default:
						mockAuthSuccess(false, String.format("%s:%s", result.getErrorCode(), result.getErrorMessage()));
						break;
					}
				}

			});
		}
		else {
			mockAuthSuccess(false, "����Ҫ�ṩ�����Ĳ��������������ƿ�����ɲ鿴�������ݡ�");
		}

	}

	@Override
	public String obtainViewHistoryUrl(MCloudDevice device) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uninit() {
		mSdkProxy = null;
		mAppContext = null;
		isInited = false;
	}

	// ==================================================
	// Utils for internal developer.
	// ==================================================

	private void mockAuthSuccess(boolean isSuccess, String message) {
		if (mArgs == null) mArgs = new MCloudSdkAuthorizedArgs();
		if (isSuccess) {
			mArgs.put(MCloudSdkAuthorizedArgs.Key_AuthorizedState, "Y");
			mArgs.put(MCloudSdkAuthorizedArgs.Key_AuthorizedMessage, message);
		}
		else {
			mArgs.put(MCloudSdkAuthorizedArgs.Key_AuthorizedState, "N");
			mArgs.put(MCloudSdkAuthorizedArgs.Key_AuthorizedMessage, message);
		}
		listenAutherizedState(mArgs);
	}

}
