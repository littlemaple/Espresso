package com.medzone.mcloud;

public final class Constants {

	private Constants() {

	}

	public static final String	APP_ID							= "wx335dd0edfa16ae15";
	public static final String	APP_SECRET						= "c2c5fdc92cf3258df1d6d4273135978f";

	/** 这个数值需要与IOS协条一致,该值的变更影响到用户补充资料对话框的呈现 */
	public static final int		APP_VERSION						= 1;
	public static final String	APP_VERSION_MAIN				= "2.4.1";
	public static final String	APP_VERSION_DEV_INCREASE		= ".13";
	public static final String	APP_VERSION_COMPLETE			= APP_VERSION_MAIN.concat(APP_VERSION_DEV_INCREASE);

	public static final int		SPLASH_TIME						= 1000;
	public static final int		DURING_PREGNANCY				= 280;

	public static final int		LOCAL_SERVER_RULE_HIGH_VERSION	= 1;

	/**
	 * 假的群成员，通常被作为占位符，用于定义其他事件
	 */
	public static final String	OWNER_MANAGER_TAG				= "fakeMember";

	public static final String	INDICATOR_HOME_TEST				= "home_test";
	public static final String	INDICATOR_HOME					= "home";
	public static final String	INDICATOR_MEASURE_DATA			= "measure_data";
	public static final String	INDICATOR_GROUP					= "group";
	public static final String	INDICATOR_MEASURE				= "measure";
	public static final String	INDICATOR_SERVICE				= "service";
	public static final String	INDICATOR_SETTING				= "setting";

	public final static String	DEVICE_DATA						= "device_data";

	public final static String	MEASURE							= "measure";
	public final static String	INPUT							= "input";

	// Default Blood Sugar
	public static final String	BLOOD_SUGAR						= "blood_sugar";
	public static final int		BLOOD_SUGAR_LOW_LEFT			= 0;
	public static final int		BLOOD_SUGAR_HIGH_LEFT			= 33;
	public static final int		BLOOD_SUGAR_LOW_RIGHT			= 0;
	public static final int		BLOOD_SUGAR_HIGH_RIGHT			= 9;
	// TODO--2014-12-30---
	public static final float	BLOOD_SUGAR_DEFAULT				= 6;

	// Default Ear Temperature
	public static final String	EAR_TEMPERATURE					= "ear_temperature";
	public static final float	EAR_TEMPERATURE_LOW				= 32f;
	public static final float	EAR_TEMPERATURE_HIGH			= 42.2f;
	public static final float	EAR_TEMPERATURE_DEFAULT			= 37f;

	// Default Oximeter
	public static final String	BLOOD_OXYGEN					= "blood_oxygen";
	public static final int		OXIMETRY_LOW					= 35;
	public static final int		OXIMETRY_HIGH					= 99;
	public static final int		OXIMETRY_DEFAULT_HIGH			= 95;
	public static final int		OXIMETRY_RATE					= 60;

	// Default Heart Rate
	public static final String	HEART_RATE						= "heart_rate";
	public static final int		HEART_RATE_LOW					= 30;
	public static final int		HEART_RATE_HIGH					= 200;

	// Default blood pressure rate
	public static final int		BLOODPRESSURE_DEFAULT_RATE		= 60;
	// Default Systolic Pressure
	public static final String	SYSTOLIC_PRESSURE				= "systolic_pressure";
	public static final int		SYSTOLIC_PRESSURE_LOW			= 30;
	public static final int		SYSTOLIC_PRESSURE_HIGH			= 300;
	public static final int		SYSTOLIC_PRESSURE_DEFAULT_HIGH	= 120;

	// Default Diastolic Pressure
	public static final String	DIASTOLIC_PRESSURE				= "diastolic_pressure";
	public static final int		DIASTOLIC_PRESSURE_LOW			= 30;
	public static final int		DIASTOLIC_PRESSURE_HIGH			= 300;
	public static final int		DIASTOLIC_PRESSURE_DEFAULT_LOW	= 80;

	// Error Type
	public static final int		ERR_POPUP_BT_OPEN				= 101;
	public static final int		ERR_POPUP_BT_DISCOVER			= 102;
	public static final int		ERR_POPUP_BT_CONNECT			= 103;
	public static final int		ERR_POPUP_BT_SOCKET				= 104;
	public static final int		ERR_POPUP_BP_CMDOUTTIME_SHORT	= 105;
	public static final int		ERR_POPUP_BP_CMDOUTTIME_LONG	= 106;
	public static final int		ERR_POPUP_BP_TEST_ERROR			= 107;
	public static final int		ERR_POPUP_BP_BAT_LOW			= 108;
	public static final int		ERR_POPUP_BP_GAS_CHARGE			= 109;
	public static final int		ERR_POPUP_OX_CMDOUTTIME_SHORT	= 110;
	public static final int		ERR_POPUP_OX_CMDOUTTIME_LONG	= 111;
	public static final int		ERR_POPUP_OX_FINGER_OUT			= 112;
	public static final int		ERR_POPUP_OX_BAT_LOW			= 113;
	public static final int		ERR_POPUP_OX_FINGER_BAT			= 114;
	public static final int		ERR_POPUP_ET_CONNECT			= 115;
	public static final int		ERR_POPUP_ET_SENSOR				= 116;
	public static final int		ERR_POPUP_ET_EEPROM				= 117;
	public static final int		ERR_POPUP_ET_RESULT_HIGH		= 118;
	public static final int		ERR_POPUP_ET_RESULT_LOW			= 119;
	public static final int		ERR_POPUP_ET_ENVIRONMENT_HIGH	= 120;
	public static final int		ERR_POPUP_ET_ENVIRONMENT_LOW	= 121;
	public static final int		ERR_POPUP_ET_BAT_LOW			= 122;

	public static final boolean	DEBUG							= true;

	public static final int		CODE_DELAY_COUNT				= 60;
	public static final int		DIALOG_DISMISS_TIME				= 2000;

	public static final int		HEIGHT_DEFAULT					= 160;
	public static final int		HEIGHT_MIN						= 10;
	public static final int		HEIGHT_MAX						= 240;

	public static final int		AGE_DEFAULT						= 18;
	public static final int		AGE_MIN							= 0;
	public static final int		AGE_MAX							= 160;

	public static final int		WEIGHT_DEFAULT					= 60;
	public static final int		WEIGHT_MIN						= 2;
	public static final int		WEIGHT_MAX						= 150;

	public static final long	millisecondOfDay				= 86400000;
	public static final int		PRE_PRODUCTION_PERIOD			= 280;

	// pressure 、oygen、et ：key
	public static final int		VALUE_RECENT					= 0;
	public static final int		VALUE_MONTH						= 1;

	public static final String	KEY_ALL_COUNT					= "all_count";
	public static final String	KEY_EXCEPTION_COUNT				= "exception_count";
	public static final String	KEY_COUNT						= "count";
	public static final String	KEY_MONTH						= "month";
	public static final String	KEY_ABNORMAL					= "abnormal";

	// measure
	public static final String	DEVICE_ID						= "device_id";
	public static final String	WHETHER_FOR_A_LONG_TIME			= "long";
	public static final String	OXYGEN							= "oxygen";
	public static final String	RATE							= "rate";
	public static final String	HIGH_PRESSURE					= "high";
	public static final String	LOW_PRESSURE					= "low";
	public static final String	TEMPERATURE						= "temperatue";

	public final static int		CONNECT_FLAG_START				= 0;
	public final static int		CONNECT_FLAG_IN					= 1;
	public final static int		CONNECT_FLAG_END				= 2;
	public final static int		SLIDING_FLAG_INIT				= 3;
	public final static int		OPEN_DEVICE_FLAG_INIT			= 5;
	public static final int		MEASURE_PENDING					= 0;
	public static final int		MEASURE_COMPLETE				= 1;
	public static final int		MEASURE_TIMEOUT					= 2;
	public static final int		MEASURE_STATE					= 3;

	// measure animation
	public static final int		MEASURE_TIME_ONE				= 1600;
	public static final int		MEASURE_TIME_TWO				= 800;

	// the long-term bloodoxygen sampling interval
	public static final long	BLOOD_OXYGEN_SAMPLING_INTERVAL	= 30 * 1000;

	// setting
	public static final int		MAX_EMS_IDCARD					= 18;
	public static final int		MAX_EMS_NAME					= 15;
	public static final int		MAX_EMS_EMAIL					= 64;
	public static final int		MAX_EMS_ADDRESS					= 110;
	public static final int		MAX_EMS_PASSWORD				= 16;
	public static final int		MAX_EMS_PHONE					= 11;

	// id card
	public static final String	ID_CARD_SCANNER					= "CVR-SCANNER";

	// 胎心
	public static final String	FETAL_HEART_MEASURE_DETECTION	= "measure_detection";
	public static final String	FETAL_HEART_START_MEASURE_TIME	= "start_measure_time";
	public static final String	FETAL_HEART_END_MEASURE_TIME	= "end_measure_time";
	public static final String	FETAL_HEART_RATE_LIST			= "fetal_heart_list";
	public static final String	FETAL_HEART_TIME				= "fetal_heart_time_list";
	public static final String	FETAL_HEART_WAV_ADDR			= "wav_addr";

	// 规则库常量字段
	public static final String	RULE_VERSION					= "rule_version";
	public static final String	RULE_LIST						= "rule_list";

	public static final String	HEALTH_SICK						= "health_sick";
	public static final String	SICK_DIABETES					= "sick_diabetes";
}
