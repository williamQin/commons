package com.william.httpclient;

import java.io.File;
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
		UploadRequest request = new UploadRequest();
		request.setUrl("http://localhost:8089/user/upload");
		request.addFile("C:\\Users\\Administrator/Desktop/webapp");
		HttpResponse response = HttpClientHandler.getInstance().upload(request);
		System.out.println(response.getResultStr());
	}
	
	@Test
	public void testDownLoad() throws ClientProtocolException, IOException{
		DownLoadRequest request = new DownLoadRequest();
		request.setUrl("http://localhost:8089/user/download");
		request.setFileName("download.json");;
		request.setFilePath("C:\\Users\\Administrator/Desktop/upload/");
		HttpResponse response = HttpClientHandler.getInstance().download(request);
		System.out.println(response.getResultStr());
	}
	
	@Test
	public void test(){
		File file = new File("C:\\Users\\Administrator/Desktop\\upload/jquery-migrate-1.2.1.js");
		System.out.println(file.getName());
	}

}
