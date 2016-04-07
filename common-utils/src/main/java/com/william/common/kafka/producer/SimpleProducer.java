package com.william.common.kafka.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SimpleProducer {
	
	public static void main(String[] args) {
		
		Properties properties = new Properties();
		properties.put("zk.connect", "10.40.100.27:2222");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		
		ProducerConfig config = new ProducerConfig(properties);
		
		Producer<String, String> producer = new Producer<String, String>(config);
		
		KeyedMessage<String, String> message = new KeyedMessage<String, String>("topic", "message");
		producer.send(message);
		producer.close();
	}
	
	

}
