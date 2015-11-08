package com.medzone.mcloud;

public enum MCloudDevice {
	/**
	 * ÑªÑ¹¼Æ
	 */
	BP("bp"),
	/**
	 * ÑªÑõ¼Æ
	 * */
	BO("bo");

	private String	type;

	private MCloudDevice(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}
