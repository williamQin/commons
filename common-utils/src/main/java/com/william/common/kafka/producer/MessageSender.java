package com.william.common.kafka.producer;

public interface MessageSender {
	
	/**
	 * @Desc 发送消息
	 * @param topic 消息主题
	 * @param msg 消息内容
	 */
	public void send(String topic, Object msg);

}
