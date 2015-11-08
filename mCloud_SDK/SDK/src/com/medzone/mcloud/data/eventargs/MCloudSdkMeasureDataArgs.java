package com.medzone.mcloud.data.eventargs;

/**
 * 约定为存放单条测量数据
 * 
 * @author Robert
 * 
 */
public class MCloudSdkMeasureDataArgs extends MCloudSdkEventArgs {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7293215312802968047L;

	public static final String	DEVPARAMS			= "devParams";

	public static final String	VALUE_DIASTOLIC		= "diastolic";
	public static final String	VALUE_SYSTOLIC		= "systolic";
	public static final String	VALUE_HEART_RATE	= "heart_rate";

	public static final String	VALUE_SATURATION	= "value_saturation";
	public static final String	MEASURE_TIME		= "measure_time";
	public static final String	MEASURE_UID			= "measureUID";

	public MCloudSdkMeasureDataArgs() {
		super();
	}

}
