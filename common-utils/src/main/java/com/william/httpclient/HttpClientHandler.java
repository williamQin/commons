package com.william.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Date: 2015-10-20 上午9:35:47<br/>
 * 
 * @author [William.Qin]
 * @version 2.0
 * @since JDK 1.6
 */
public class HttpClientHandler {
	private static String DEFAULT_CHARSET = "UTF-8";

	/** 连接超时时间，由bean factory设置，缺省为8秒钟 */
	private int defaultConnectionTimeout = 8000;

	/** 响应超时时间, 由bean factory设置，缺省为30秒钟 */
	private int defaultSoTimeout = 30000;

	private int defaultMaxConnPerHost = 30;

	private int defaultMaxTotalConn = 200;

	private PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();

	private static HttpClientHandler httpClientHandler = new HttpClientHandler();

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static HttpClientHandler getInstance() {
		return httpClientHandler;
	}

	/**
	 * 私有的构造方法(初始化连接管理器)
	 */
	private HttpClientHandler() {
		// Increase max total connection to 200
		clientConnectionManager.setMaxTotal(defaultMaxTotalConn);
		// Increase default max connection per route to 30
		clientConnectionManager.setDefaultMaxPerRoute(defaultMaxConnPerHost);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("locahost", 80);
		clientConnectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);
	}

	/**
	 * @Title: doPost
	 * @Description: TODO(使用post方式请求数据)
	 * @param @param request
	 * @param @return
	 * @param @throws ParseException
	 * @param @throws UnsupportedEncodingException
	 * @param @throws IOException
	 * @param @throws URISyntaxException 设定文件
	 * @return HttpResponse 返回类型
	 * @throws
	 * @date 2015-10-21 下午4:06:52
	 */
	public HttpResponse doPost(HttpRequest request) throws ParseException, UnsupportedEncodingException, IOException, URISyntaxException {
		HttpResponse response = null;
		// 获取http连接
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		// 请求体配置项
		RequestConfig requestConfig = getRequestConfig(request);
		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		List<NameValuePair> nameValuePairs = requestParamsHandler(request.getParametersMap());
		HttpPost httpPost = new HttpPost(request.getUrl());
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
		httpPost.setConfig(requestConfig);
		org.apache.http.HttpResponse httpResponse = httpclient.execute(httpPost);
		if (httpResponse != null) {
			response = new HttpResponse();
			response.setHeaders(httpResponse.getAllHeaders());
			response.setStatusLine(httpResponse.getStatusLine());
			if (httpResponse.getEntity() != null) {
				response.setResultStr(EntityUtils.toString(httpResponse.getEntity(), charset));
			}
		}
		return response;
	}

	/**
	 * @Title: doGet
	 * @Description: TODO(使用get方式请求数据)
	 * @param @param request
	 * @param @return
	 * @param @throws ParseException
	 * @param @throws IOException
	 * @param @throws URISyntaxException 设定文件
	 * @return HttpResponse 返回类型
	 * @throws
	 * @date 2015-10-21 下午4:07:18
	 */
	public HttpResponse doGet(HttpRequest request) throws ParseException, IOException, URISyntaxException {
		HttpResponse response = null;
		// 获取http连接
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		// 请求体配置项
		RequestConfig requestConfig = getRequestConfig(request);
		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		List<NameValuePair> nameValuePairs = requestParamsHandler(request.getParametersMap());
		HttpGet httpGet = new HttpGet();
		String param = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, charset), charset);
		httpGet.setURI(new URI(request.getUrl() + "?" + param));
		httpGet.setConfig(requestConfig);
		org.apache.http.HttpResponse httpResponse = httpclient.execute(httpGet);
		if (httpResponse != null) {
			response = new HttpResponse();
			response.setHeaders(httpResponse.getAllHeaders());
			response.setStatusLine(httpResponse.getStatusLine());
			if (httpResponse.getEntity() != null) {
				response.setResultStr(EntityUtils.toString(httpResponse.getEntity(), charset));
			}
		}
		return response;
	}

	/**
	 * @Title: upload 
	 * @Description: TODO(使用post方式上传文件) 
	 * @param @param request
	 * @param @return
	 * @param @throws ClientProtocolException
	 * @param @throws IOException 设定文件 
	 * @return HttpResponse 返回类型 
	 * @throws 
	 * @date 2015-10-21 下午4:52:05
	 */
	public HttpResponse upload(HttpRequest request) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		// 获取http连接
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		// 请求体配置项
		RequestConfig requestConfig = getRequestConfig(request);
		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setCharset(Charset.forName(charset)).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", new FileBody(new File(request.getFilePath()), ContentType.MULTIPART_FORM_DATA, request.getFileName()));
		if(request.getParametersMap() != null){
			Iterator<String> iterator = request.getParametersMap().keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				builder.addTextBody(key, request.getParametersMap().get(key));
			}
		}
		HttpPost httpPost = new HttpPost(request.getUrl());
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(builder.build());
		org.apache.http.HttpResponse httpResponse = httpclient.execute(httpPost);
		if (httpResponse != null) {
			response = new HttpResponse();
			response.setHeaders(httpResponse.getAllHeaders());
			response.setStatusLine(httpResponse.getStatusLine());
			if (httpResponse.getEntity() != null) {
				response.setResultStr(EntityUtils.toString(httpResponse.getEntity(), charset));
			}
		}
		return response;
	}

	/**
	 * @Title: download 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws 
	 * @date 2015-10-29 下午2:41:39
	 */
	public HttpResponse download(HttpRequest request) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		// 获取http连接
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		// 请求体配置项
		RequestConfig requestConfig = getRequestConfig(request);
		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		HttpGet httpGet = new HttpGet(request.getUrl());
		httpGet.setConfig(requestConfig);
		org.apache.http.HttpResponse httpResponse = httpclient.execute(httpGet);
		if(httpResponse != null){
			response = new HttpResponse();
			response.setHeaders(httpResponse.getAllHeaders());
			response.setStatusLine(httpResponse.getStatusLine());
			InputStream in = httpResponse.getEntity().getContent();
			try{
				File file = new File(request.getFilePath() + request.getFileName());
				FileOutputStream outputStream = new FileOutputStream(file);
				byte[] temp = new byte[1024];
				while(in.read(temp) != -1){
					outputStream.write(temp, 0, 1);
				}
				outputStream.flush();
				outputStream.close();
			}finally{
				in.close();
			}
		}
		return response;
	}

	private List<NameValuePair> requestParamsHandler(Map<String, String> parametersMap) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (parametersMap != null) {
			Iterator<String> iterator = parametersMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				nameValuePairs.add(new BasicNameValuePair(key, parametersMap.get(key)));
			}
		}
		return nameValuePairs;
	}

	private RequestConfig getRequestConfig(HttpRequest request) {
		// 设置连接超时
		int connectionTimeout = defaultConnectionTimeout;
		if (request.getConnectTimeout() > 0) {
			connectionTimeout = request.getConnectTimeout();
		}
		// 设置响应超时
		int soTimeout = defaultSoTimeout;
		if (request.getTimeout() > 0) {
			soTimeout = request.getTimeout();
		}
		// set Timeout
		return RequestConfig.custom().setConnectionRequestTimeout(soTimeout).setConnectTimeout(connectionTimeout).build();
	}

}

