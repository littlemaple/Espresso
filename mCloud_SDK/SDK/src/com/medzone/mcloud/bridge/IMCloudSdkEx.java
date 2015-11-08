package com.medzone.mcloud.bridge;

import android.content.Context;

import com.medzone.mcloud.MCloudDevice;
import com.medzone.mcloud.data.eventargs.MCloudSdkAuthorizedArgs;
import com.medzone.mcloud.exception.MCloudInitException;

interface IMCloudSdkEx extends IMCloudSdk {

	/**
	 * 初始化SDK，并设置好SDK的事件监听器
	 * 
	 * @param appContext
	 *            应用程序ApplicationContext
	 * @param sdkProxy
	 *            实现了{@link IMCloudSdkEx}按约定实现接口的实例
	 */
	void init(Context appContext, IMCloudSdk sdkProxy) throws MCloudInitException;

	/**
	 * 返回是否已经正确初始化SDK
	 * 
	 * @return boolean
	 */
	boolean isInit();

	/**
	 * 检查是否已经授权成功
	 * 
	 * @return
	 */
	boolean isOAuthorized();

	/**
	 * 初始化模块，若当前已经授权成功，则不作处理。
	 * 
	 * @param context
	 */
	void doAutherized(MCloudSdkAuthorizedArgs eventArgs);

	/**
	 * 下载规则库
	 */
	void downloadRules();

	/**
	 * 依据给定的测量模块下载规则库
	 * 
	 * @param device
	 */
	void downloadRuleInType(MCloudDevice device);

	/**
	 * 启动指定模块的测量，需要授权成功。
	 * 
	 * @param device
	 */
	void measure(MCloudDevice device);

	/**
	 * 启动查看指定某个测量模块，需要授权成功。
	 * 
	 * @param device
	 */
	void view(MCloudDevice device);

	/**
	 * 获取查看历史数据网址，失败返回 null，需要授权成功。
	 * 
	 * @param device
	 */
	String obtainViewHistoryUrl(MCloudDevice device);

	/**
	 * 释放SDK内部对外部的引用：context。
	 */
	void uninit();
}
