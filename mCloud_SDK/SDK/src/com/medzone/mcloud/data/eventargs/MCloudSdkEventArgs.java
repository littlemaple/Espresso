package com.medzone.mcloud.data.eventargs;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class MCloudSdkEventArgs implements Map<String, String>, Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -3633982993474303565L;

	private HashMap<String, String>	eventArgs;

	public MCloudSdkEventArgs() {
		eventArgs = new HashMap<String, String>();
	}

	public MCloudSdkEventArgs(Map<String, String> eventArgsMap) {
		eventArgs = new HashMap<String, String>();
		eventArgs.putAll(eventArgsMap);
	}

	@Override
	public void clear() {
		eventArgs.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return eventArgs.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return eventArgs.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return eventArgs.entrySet();
	}

	@Override
	public String get(Object key) {
		return eventArgs.get(key);
	}

	@Override
	public boolean isEmpty() {
		return eventArgs.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return eventArgs.keySet();
	}

	@Override
	public String put(String key, String value) {
		return eventArgs.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> arg0) {
		eventArgs.putAll(arg0);
	}

	@Override
	public String remove(Object key) {
		return eventArgs.remove(key);
	}

	@Override
	public int size() {
		return eventArgs.size();
	}

	@Override
	public Collection<String> values() {
		return eventArgs.values();
	}

	/**
	 * 根据Key获得Value
	 * 
	 * @param key
	 *            键
	 * @param defaultVal
	 *            当获取失败的返回
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, T defaultVal) {
		if (containsKey(key)) {
			return (T) get(key);
		}
		return defaultVal;
	}

	@Override
	public String toString() {
		return eventArgs.toString();
	}
}
