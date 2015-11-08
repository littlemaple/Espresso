package com.medzone.mcloud.data.errorcode;

import android.util.SparseArray;

/**
 * TODO status code contains attribute<code>type</code> .
 * <p>
 * 1) .处理错误码以及错误提示信息的映射关系
 * </p>
 * <p>
 * 2) .需要注意这边同一错误码可能对应多份错误信息。（目前场景是分模块去呈现）
 * </p>
 * <p>
 * 3) .另外错误码应该允许被客户定制，即客户端可以选择不使用我们推荐的错误信息去呈现。（兼容同一模块内，也出现不同的错误提示信息极少数情况）
 * </p>
 * 
 * @author Robert
 * 
 */
abstract class StatusCode<T> implements ICode<T> {

	protected SparseArray<T>	errorCodeMap	= new SparseArray<T>();

	public StatusCode() {
		initCodeCollect();
	}

	protected abstract void initCodeCollect();

	/**
	 * @param type
	 *            指定错误集合类别
	 * @param code
	 *            错误码
	 * @return 如果存在指定集合，并且集合内错误码也存在，则返回对应的友好错误信息。如果不存在，则返回
	 *         {@link#UNDEFINED_CODE}对应的内容.
	 * 
	 * @deprecated type
	 */
	@Override
	public T getStatusCodeMessage(int type, int code) {

		if (isContainsKey(type)) {
			if (errorCodeMap.get(code) != null) {
				return errorCodeMap.get(code);
			}
		}
		return errorCodeMap.get(CODE_UNDEFINED);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	protected abstract boolean isContainsKey(int type);
}
