package com.medzone.mcloud.data.errorcode;

import android.util.SparseArray;

/**
 * 错误场景码，需要保证不同的场景唯一对应一个Code，但是相同的场景，可以结合。
 * 
 * @author Robert
 * 
 */
public abstract class CodeProxy<T> implements ICode<T> {

	/**
	 * the collect of statusCode
	 */
	protected SparseArray<ICode<T>>	iCodes;

	public SparseArray<ICode<T>> getCodes() {
		if (iCodes == null) {
			iCodes = new SparseArray<ICode<T>>();
		}
		return iCodes;
	}

}
