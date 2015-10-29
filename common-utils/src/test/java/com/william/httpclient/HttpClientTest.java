package com.william.httpclient;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class HttpClientTest {
	
	@Test
	public void testGet() throws ParseException, IOException, URISyntaxException{
		HttpRequest request = new HttpRequest();
		request.setUrl("http://localhost:888/test/");
		request.setRequestParam("param1", "william").setRequestParam("param2", "qinxi");
		HttpResponse response = HttpClientHandler.getInstance().doGet(request);
		System.out.println(response.getResultStr());
	}
	
	@Test
	public void testPost() throws ParseException, IOException, URISyntaxException{
		HttpRequest request = new HttpRequest();
		request.setUrl("http://localhost:888/test/");
		request.setRequestParam("param1", "william").setRequestParam("param2", "qinxi");
		HttpResponse response = HttpClientHandler.getInstance().doPost(request);
		System.out.println(response.getResultStr());
	}
	
	@Test
	public void testUpload() throws ClientProtocolException, IOException{
		HttpRequest request = new HttpRequest();
		request.setUrl("http://localhost:888/upload");
		request.setFileName("test");
		request.setFilePath("C:\\Users\\Administrator\\Desktop\\test.txt");
		HttpResponse response = HttpClientHandler.getInstance().upload(request);
		System.out.println(response.getResultStr());
	}
	
	@Test
	public void testDownLoad() throws ClientProtocolException, IOException{
		HttpRequest request = new HttpRequest();
		request.setUrl("http://localhost:888/download");
		request.setFileName("test.txt");
		request.setFilePath("C:\\Users\\Administrator\\Desktop\\download\\");
		HttpClientHandler.getInstance().download(request);
	}

}
