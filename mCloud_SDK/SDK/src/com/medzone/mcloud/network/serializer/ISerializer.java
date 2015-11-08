package com.medzone.mcloud.network.serializer;

public interface ISerializer<T> {

	String serialize(T t);

	T deserialize(String s);
}
