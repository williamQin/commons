package com.william.common.kafka.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public abstract class AbstractMessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class); 
	
	private ExecutorService executor;
	
	//处理的线程数
	private int threadNums;
	
	//topic
	private String topic;
	
	@Autowired
	private ConsumerConnectorFactory consumerConnectorFactory;
	
	public void init(){
		//获取连接
        ConsumerConnector consumerConnector = consumerConnectorFactory.getConnector();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(threadNums));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
     
        executor = Executors.newFixedThreadPool(threadNums);
        
        for (final KafkaStream<byte[], byte[]> stream : streams) {
        	executor.execute(new Runnable() {
				@Override
				public void run() {
					ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while(it.hasNext()){
						// 接收消息
						String message = new String(it.next().message());
						logger.debug("receive kafka message success! msg = {}", message);
						// 处理消息
						long startTime = System.currentTimeMillis();
						try{
							//捕获消息处理中产生的所有异常，防止线程因异常或错误意外中断，造成消费者无法读取消息的问题
							MessageHandler(message);
						}catch(Throwable e){
							logger.error("消息处理异常：ThreadName={}, topic={}, message={}", Thread.currentThread().getName(), topic, message, e);
						}
						logger.info("#########topic={} 消息处理耗时：" + (System.currentTimeMillis() - startTime) + "毫秒############", topic);
					}
				}
			});
        }
	}

	protected abstract void MessageHandler(String msg) ;

	public int getThreadNums() {
		return threadNums;
	}

	public void setThreadNums(int threadNums) {
		this.threadNums = threadNums;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	

}
