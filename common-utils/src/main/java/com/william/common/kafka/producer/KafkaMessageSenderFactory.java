package com.william.common.kafka.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class KafkaMessageSenderFactory implements MessageSenderFactory{
	
	private String brokerList;
	
	//(默认10000)在异步模式下，producer端允许buffer的最大消息数量，如果producer无法尽快将消息发送给broker，从而导致消息在producer端大量沉积，如果消息的条数达到此配置值，将会导致producer端阻塞或者消息被抛弃
	private String queueBufferingMaxMessages;
	//(默认5000)当使用异步模式时，缓冲数据的最大时间。例如设为100的话，会每隔100毫秒把所有的消息批量发送。这会提高吞吐量，但是会增加消息的到达延时
	private String queueBufferingMaxMs;

	@Override
	public MessageSender createSender() {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", brokerList);
		properties.setProperty("producer.type", "async");
		properties.put("queue.buffering.max.messages", queueBufferingMaxMessages);
		properties.put("queue.buffering.max.ms", queueBufferingMaxMs);
		
		ProducerConfig config = new ProducerConfig(properties);
		
		Producer<String, byte[]> producer = new Producer<String, byte[]>(config);
		return new KafkaMessageSender(producer);
	}

}
