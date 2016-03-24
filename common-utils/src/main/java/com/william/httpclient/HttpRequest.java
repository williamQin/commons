package com.william.httpclient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author   [William.Qin]
 * @version  2.0
 * @since    JDK 1.6
 */
public class HttpRequest {
	
	/*
	 * 连接超时时间
	 */
	private int connectionTimeout = 60000;
	
	/*
	 * 请求超时时间
	 */
	private int requestTimeout = 30000;
	
	/**
	 * 默认字符集("UTF-8")
	 */
	private String charset = "UTF-8";
	
	/**
	 * 请求的url地址
	 */
	private String url = null;
	
	/**
	 * 请求参数
	 */
	private Map<String, String> parametersMap = null;
	
	private String clientIp;
	
	public HttpRequest(){
	}
	
	public HttpRequest(String url){
		this.url = url;
	}
	
	public HttpRequest(String url, Map<String, String> parametersMap){
		this.url = url;
		this.parametersMap = parametersMap;
	}
	
	public HttpRequest(String charset, String url, Map<String, String> parametersMap){
		this.charset = charset;
		this.url = url;
		this.parametersMap = parametersMap;
	}
	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}
	
	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	/**
	 * @param key 参数名
	 * @param value 参数值
	 * @return
	 */
	public HttpRequest setRequestParam(String key, String value){
		if(this.parametersMap == null){
			this.parametersMap = new HashMap<String, String>();
		}
		this.parametersMap.put(key, value);
		return this;
	}
}
