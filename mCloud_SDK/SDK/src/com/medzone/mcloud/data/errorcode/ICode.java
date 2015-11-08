package com.medzone.mcloud.data.errorcode;

public interface ICode<T> {

	// =============================
	// 错误定义
	// =============================
	static final int	CODE_UNDEFINED		= -3;
	// =============================
	// 错误请求码-API
	// =============================
	/**
	 * If the error code is less than zero, then is the local definition of
	 * error code.
	 */
	static final int	CODE_REQUEST_FAILED	= -1;

	static final int	CODE_SUCCESS		= 0;

	/** 无 */
	static final int	CODE_10000			= 10000;
	/** 当前网络不可用,请检查网络设置 */
	static final int	CODE_10001			= 10001;
	/** 网络异常,请稍后再试 */
	static final int	CODE_10002			= 10002;
	/** 字数超过限制 */
	static final int	CODE_10003			= 10003;
	/** 没有找到储存卡 */
	static final int	CODE_10004			= 10004;
	/** 服务器异常，请稍后再试 */
	static final int	CODE_10005			= 10005;

	public T getStatusCodeMessage(int type, int code);

}
