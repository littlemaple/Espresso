package com.medzone.mcloud.env;

/**
 * 第三方 APP 接入心云提供的开发包，实现连接心云设备进行数据检测，并将数据自动上传至心云平台。
 * 
 * 第三方 APP 根据心云提供的入口，通过 webview 形式查阅用户历史数据，曲线统计及建议分析。
 * 
 * 如有必要，心云平台为合作方部署简化版数据中心。检测数据将被自动同步至该中心，以提供统一的管理入口。
 * 
 * @author Robert
 * 
 */
public class MCloudSdkEnv {

	public static final MCloudSdkAPIEnv	sdkApiEnv	= MCloudSdkAPIEnv.ENV_TRADE_TESTING;

}
