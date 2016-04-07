package com.william.common.kafka.consumer;

import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerConnectorFactory {
	
	private String zkConnect;
	
	private String zkConnectTimeout;
	
	private String groupId;
	
	public ConsumerConnector getConnector() {
		Properties props = new Properties();
		props.put("zookeeper.connect", zkConnect);
		props.put("group.id", groupId);
		props.put("zookeeper.connection.timeout.ms", zkConnectTimeout);
		ConsumerConfig consumerConfig = new ConsumerConfig(props);
		ConsumerConnector connector = Consumer.createJavaConsumerConnector(consumerConfig);
		return connector;
	}
	
	public String getZkConnect() {
		return zkConnect;
	}

	public void setZkConnect(String zkConnect) {
		this.zkConnect = zkConnect;
	}

	public String getZkConnectTimeout() {
		return zkConnectTimeout;
	}

	public void setZkConnectTimeout(String zkConnectTimeout) {
		this.zkConnectTimeout = zkConnectTimeout;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
