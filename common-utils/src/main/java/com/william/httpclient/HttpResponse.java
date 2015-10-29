package com.william.httpclient;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Date:     2015-10-19 ÉÏÎç10:21:15<br/>
 * @author   [William.Qin]
 * @version  2.0
 * @since    JDK 1.6
 */
public class HttpResponse {
	
	private Header[] headers;
	
	private StatusLine statusLine;
	
	private String resultStr;

	public Header[] getHeaders() {
	
		return headers;
	}

	public void setHeaders(Header[] headers) {
	
		this.headers = headers;
	}

	public StatusLine getStatusLine() {
	
		return statusLine;
	}

	public void setStatusLine(StatusLine statusLine) {
	
		this.statusLine = statusLine;
	}

	public String getResultStr() {
	
		return resultStr;
	}

	public void setResultStr(String resultStr) {
	
		this.resultStr = resultStr;
	}
	
	public Header getHeader(String key){
		if(headers == null || headers.length == 0){
			return null;
		}
		for(Header header : headers){
			if(key.equals(header.getName())){
				return header;
			}
		}
		return null;
	}
	
}

