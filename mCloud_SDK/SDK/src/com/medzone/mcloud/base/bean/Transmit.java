package com.medzone.mcloud.base.bean;

import java.io.Serializable;

/**
 * 用于传输的数据类
 * 
 * 错误码
 * 
 * @author lwm
 * 
 */
public class Transmit implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String				msg					= "";
	private int					what;
	private int					arg1;

	private short				status;

	public void setStatus(short status) {
		this.status = status;
	}

	public short getStatus() {
		return status;
	}

	// msg的含义
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	// what的含义
	public int getWhat() {
		return what;
	}

	public void setWhat(int what) {
		this.what = what;
	}

	@Deprecated
	// 使用错误码替代该方法
	public int getArg1() {
		return arg1;
	}

	@Deprecated
	// 使用错误码替代该方法
	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}

}
