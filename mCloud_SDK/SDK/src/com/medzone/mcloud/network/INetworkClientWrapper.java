package com.medzone.mcloud.network;

import org.apache.http.client.HttpClient;

import com.medzone.mcloud.network.exception.RestException;
import com.medzone.mcloud.network.serializer.ISerializer;

/**
 * @author Robert
 * @category 网络客户端基础属性
 */
public interface INetworkClientWrapper<I, O> {

	/**
	 * 创建一个用于网络请求的客户端。
	 * 
	 * @return 这里使用Apache的HttpClient组件完成对HTTP请求的封装。
	 */
	public HttpClient createClient();

	/**
	 * 执行一次网络请求，返回结果将同步返回。通常，我们会使用{@link ISerializer}完成对结果的解析以及包装。
	 * 
	 * @param uri
	 *            资源连接，诸如：“/api/contacts/”
	 * @param params
	 *            封装包括请求实体等。
	 * @return 通常，我们会使用{@link ISerializer}完成对结果的解析以及包装。
	 * @throws RestException
	 *             当请求过程中遇到异常，会使用RestException封装异常结果。
	 */
	public O call(String uri, NetworkParams.Builder params) throws RestException;

	/**
	 * 销毁一个网络请求的客户端，通常他会关闭Client的连接池。
	 */
	public void distoryClient();

}
