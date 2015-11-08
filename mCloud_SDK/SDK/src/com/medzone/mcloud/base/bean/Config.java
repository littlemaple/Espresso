package com.medzone.mcloud.base.bean;

/**
 * 
 * @author Robert.
 * 
 *         </br>This class used to debug framework,make some config here.
 */
public class Config {

	private Config() {
	}

	/**
	 * 是否是设计者模式，允许各自做一些实验性的设计工作，在主干中作展示使用。
	 */
	public static boolean	isProductDesignerMode	= false;
	/**
	 * 是否是开发者模式，允许做一些便于开发者的调试开发工作。
	 */
	public static boolean	isDeveloperMode			= false;
	/**
	 * 是否是测试人员模式，允许为测试人员提供一些常用的功能，以便于测试快速开展工作。
	 */
	public static boolean	isTesterMode			= false;
	/**
	 * 是否是生产校验模式，允许质检员可以降低门槛进行快速测试。
	 */
	public static boolean	isFactoryMode			= false;
	/**
	 * 试点版本
	 */
	public static boolean	isExperimentalMode		= true;

}
