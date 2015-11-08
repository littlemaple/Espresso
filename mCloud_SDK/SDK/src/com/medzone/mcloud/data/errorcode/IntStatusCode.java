package com.medzone.mcloud.data.errorcode;

/**
 * 
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
public abstract class IntStatusCode extends StatusCode<Integer> {

	protected void initCodeCollect() {
		// 提供Jar给外部使用，必须使用反射的方式来获取资源，否则会无法正确找到资源
		// errorCodeMap.put(CodeProxy.CODE_UNDEFINED, string.CODE_UNDEFINED);
		// errorCodeMap.put(CodeProxy.CODE_REQUEST_FAILED,
		// string.CODE_REQUEST_FAILED);
		// errorCodeMap.put(CodeProxy.CODE_10000, string.CODE_10000);
		// errorCodeMap.put(CodeProxy.CODE_10001, string.CODE_10001);
		// errorCodeMap.put(CodeProxy.CODE_10002, string.CODE_10002);
		// errorCodeMap.put(CodeProxy.CODE_10003, string.CODE_10003);
		// errorCodeMap.put(CodeProxy.CODE_10004, string.CODE_10004);
		// errorCodeMap.put(CodeProxy.CODE_10005, string.CODE_10005);
	}

}
