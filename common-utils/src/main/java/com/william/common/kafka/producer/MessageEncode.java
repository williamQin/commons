package com.william.common.kafka.producer;

public interface MessageEncode<T> {
	
	public byte[] encode(T msg); 

}
