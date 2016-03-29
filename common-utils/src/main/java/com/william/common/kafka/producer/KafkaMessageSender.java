package com.william.common.kafka.producer;

import com.alibaba.fastjson.JSON;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

public class KafkaMessageSender implements MessageSender{
	
	private Producer<String, byte[]> producer;
	
	public KafkaMessageSender(Producer<String, byte[]> producer){
		this.producer = producer;
	}
	
	public Producer<String, byte[]> getProducer() {
		return producer;
	}

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}

	@Override
	public void send(String topic, Object msg) {
		String msgStr = JSON.toJSONString(msg);
		KeyedMessage<String, byte[]> message = new KeyedMessage<String, byte[]>(topic, msgStr.getBytes());
		producer.send(message);
	}

}
