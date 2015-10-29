package com.william.httpclient;

import java.util.HashMap;
import java.util.Map;

/**
 * Date:     2015-10-19 上午10:07:10<br/>
 * @author   [William.Qin]
 * @version  2.0
 * @since    JDK 1.6
 */
public class HttpRequest {
	
	/**
	 * 默认的请求编码方式("UTF-8")
	 */
	private String charset = "UTF-8";
	
	/**
	 * 访问超时时间
	 */
	private int timeout = 0;
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 0;
	
	/**
	 * 待请求的URL
	 */
	private String url = null;
	
	/**
	 * 请求的文本参数
	 */
	private Map<String, String> parametersMap = null;
	
	/**
	 * 上传或下载的文件路径
	 */
	private String filePath;
	
	/**
	 * 上传或下载文件的文件名
	 */
	private String fileName;
	
	/**
	 * 请求发起方的Ip地址
	 */
	private String clientIp;

	public String getUrl() {
	
		return url;
	}

	public void setUrl(String url) {
	
		this.url = url;
	}

	public int getTimeout() {
	
		return timeout;
	}

	public void setTimeout(int timeout) {
	
		this.timeout = timeout;
	}

	public int getConnectTimeout() {
	
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
	
		this.connectTimeout = connectTimeout;
	}

	public Map<String, String> getParametersMap() {
	
		return parametersMap;
	}

	public void setParametersMap(Map<String, String> parametersMap) {
	
		this.parametersMap = parametersMap;
	}

	public String getCharset() {
	
		return charset;
	}

	public void setCharset(String charset) {
	
		this.charset = charset;
	}

	public String getClientIp() {
	
		return clientIp;
	}

	public void setClientIp(String clientIp) {
	
		this.clientIp = clientIp;
	}

	public String getFilePath() {
	
		return filePath;
	}

	public void setFilePath(String filePath) {
	
		this.filePath = filePath;
	}

	public String getFileName() {
	
		return fileName;
	}

	public void setFileName(String fileName) {
	
		this.fileName = fileName;
	}
	
	/**
	 * @param key 參數名
	 * @param value 參數值
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
