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
 * 
 * @author [William.Qin]
 * @version 2.0
 * @since JDK 1.6
 */
public class HttpClientHelper {
	
	private static String DEFAULT_CHARSET = "UTF-8";
	
	//连接超时时间
	private int defaultConnectionTimeout = 60000;

	//请求超时时间
	private int defaultRequestTimeout = 30000;

	private int defaultMaxConnPerHost = 30;

	private int defaultMaxTotalConn = 200;
	
	private PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();

	private static HttpClientHelper httpClientHandler;

	//Get the instance by default config
	public static HttpClientHelper getInstance() {
		if(httpClientHandler == null)
			httpClientHandler = new HttpClientHelper();
		return httpClientHandler;
	}
	//Get the instance by config
	public static HttpClientHelper getInstance(Integer defaultMaxConnPerHost, Integer defaultMaxTotalConn) {
		if(httpClientHandler == null){
			httpClientHandler = new HttpClientHelper(defaultMaxConnPerHost, defaultMaxTotalConn);
		}
		return httpClientHandler;
	}
	
	private HttpClientHelper() {
		this.setConnectionManager();
	}
	
	private HttpClientHelper(Integer defaultMaxConnPerHost, Integer defaultMaxTotalConn){
		if(defaultMaxConnPerHost != null)
			this.defaultMaxConnPerHost = defaultMaxConnPerHost;
		if(defaultMaxTotalConn != null)
			this.defaultMaxTotalConn = defaultMaxTotalConn;
		this.setConnectionManager();
	}
	
	/**
	 * 设置httpclient的并发连接数
	 */
	private void setConnectionManager() {
		// Set the max total connections
		clientConnectionManager.setMaxTotal(defaultMaxTotalConn);
		// Set default max connection per route
		clientConnectionManager.setDefaultMaxPerRoute(defaultMaxConnPerHost);
		// Set max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("locahost", 80);
		clientConnectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);
	}

	/**
	 * @Title: doPost
	 * @Description: TODO(使用httpclient发起简单的post请求)
	 * @param @param request
	 * @param @return
	 * @param @throws ParseException
	 * @param @throws UnsupportedEncodingException
	 * @param @throws IOException
	 * @param @throws URISyntaxException
	 * @return HttpResponse
	 * @throws
	 */
	public HttpResponse doPost(HttpRequest request) throws ParseException, UnsupportedEncodingException, IOException, URISyntaxException {
		HttpResponse response = null;
		
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		
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
	 * @Description: TODO(使用httpclient发起简单的get请求)
	 * @param @param request
	 * @param @return
	 * @param @throws ParseException
	 * @param @throws IOException
	 * @param @throws URISyntaxException
	 * @return HttpResponse
	 * @throws
	 */
	public HttpResponse doGet(HttpRequest request) throws ParseException, IOException, URISyntaxException {
		HttpResponse response = null;
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		
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
	 * @Description: TODO(使用httpClient上传文件) 
	 * @param @param request
	 * @param @return
	 * @param @throws ClientProtocolException
	 * @param @throws IOException
	 * @return HttpResponse
	 * @throws 
	 */
	public HttpResponse upload(UploadRequest request) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
		
		RequestConfig requestConfig = getRequestConfig(request);
		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.setCharset(Charset.forName(charset))//设置字符集
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
		List<File> fileList = request.getFileList();
		if(fileList != null && fileList.size() > 0){
			for(File file : fileList){
				String fileName = file.getName();
				builder.addPart(fileName, new FileBody(file, ContentType.MULTIPART_FORM_DATA, fileName));
			}
		}
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
	 * @Description: TODO(使用httpClient下载文件) 
	 * @param request
	 * @return 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws 
	 */
	public HttpResponse download(DownLoadRequest request) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		HttpClient httpclient = HttpClients.createMinimal(clientConnectionManager);
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
				byte[] buffer = new byte[2048];
				int bufferRead = 0;
				while((bufferRead = in.read(buffer, 0, buffer.length)) != -1){
					outputStream.write(buffer, 0, bufferRead);
				}
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
		// 连接超时时间
		int connectionTimeout = defaultConnectionTimeout;
		if (request.getConnectionTimeout() > 0) {
			connectionTimeout = request.getConnectionTimeout();
		}
		// 请求超时时间
		int requestTimeout = defaultRequestTimeout;
		if (request.getRequestTimeout() > 0) {
			requestTimeout = request.getRequestTimeout();
		}
		// set Timeout
		return RequestConfig.custom().setConnectionRequestTimeout(requestTimeout).setConnectTimeout(connectionTimeout).build();
	}

}

