package com.medzone.mcloud;

public enum MCloudDevice {
	/**
	 * Ѫѹ��
	 */
	BP("bp"),
	/**
	 * Ѫ����
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
