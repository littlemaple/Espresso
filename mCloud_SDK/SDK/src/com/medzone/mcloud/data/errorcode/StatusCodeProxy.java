package com.medzone.mcloud.data.errorcode;

import com.medzone.mcloud.logging.Log;

/**
 * 
 * @author Robert
 */
public class StatusCodeProxy extends CodeProxy<Integer> {

	protected StatusCodeProxy() {
	}

	// ===================================================//
	// 异常码管理类的方法，异常码本身无法获取该事件 //
	// ===================================================//

	private boolean isTypeExist(int type) {
		return getCodes().indexOfKey(type) < 0 ? false : true;
	}

	/**
	 * 根据指定错误类别与错误码，获取一条错误信息
	 */
	@Override
	public Integer getStatusCodeMessage(int type, int errorCode) {
		if (!isTypeExist(type)) {
			createIfNotExist(type);
		}
		return getCodes().get(type).getStatusCodeMessage(type, errorCode);
	}

	/**
	 * 如果指定错误管理对象不存在，则进行创建
	 */
	protected boolean createIfNotExist(int type) {
		return false;
	}

	public void release(int type) {
		if (isTypeExist(type)) {
			getCodes().delete(type);
		}
		else {
			Log.v(Log.CORE_FRAMEWORK, "This type has been release!");
		}
	}

	public void release() {
		getCodes().clear();
	}

}
