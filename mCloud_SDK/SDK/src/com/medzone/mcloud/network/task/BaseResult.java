package com.medzone.mcloud.network.task;

public class BaseResult {

	// server response code(0)
	protected int		errorCode;
	protected String	errorMessage;

	public BaseResult() {
		super();
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}