package com.william.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtil {
	
	private static Logger logger = LoggerFactory.getLogger(NetUtil.class);
	private static final int BUFFER_SIZE = 1024; // 缓冲区最大字节数
	private static final String LOG_MSG = "cmd = method=NetUtil.";
	
	/**
     * @Desc 获取请求来源的IP地址
     * 获取Ip地址，先取X-Forwarded-For地址，如果为空， <br>
     * 取Proxy-Client-IP，如果为空，再取<br>
     * WL-Proxy-Client-IP，如果为空，再取request.getRemoteAddr() <br>
     * 注意：如果以上任何一种方式获取到的ip中有, 逗号分隔出的多个IP地址，则取第一个合法的IP地址（非unknown的地址)
     * @param request
     * @return
     * String
     */
    public static String getIpAddr(HttpServletRequest request) {
    	
        String ip = request.getHeader("X-Real-IP");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Forwarded-For");  
        }  
        if (ip == null || "".equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || "".equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || "".equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && !"".equals(ip)) {
            String[] ips = ip.split(",");
            for (int i = 0; i < ips.length; i++) {
                if (ips[i] != null && !"".equals(ips[i])
                        && !"unknown".equalsIgnoreCase(ips[i])) {
                    ip = ips[i];
                    break;
                }
            }
        }
        return ip;
    }
    /**
     * @Desc http request 代理访问
     * @param requestUrl
     * @param req
     * @param resp
     * void
     */
    public static void httpProxyRequest(String requestUrl, HttpServletRequest req,HttpServletResponse resp){
        httpProxyRequest(requestUrl, req, resp,null);
    }
    
    /**
     * @Desc http request 代理访问
     * @param requestUrl 要访问的url
     * @param req HttpServletRequest对象
     * @param resp
     * @param ros rout post数据流
     * void
     */
    public static void httpProxyRequest(String requestUrl, HttpServletRequest req,HttpServletResponse resp, OutputStream ros) {
        HttpURLConnection httpconn = null;
        OutputStream outPs = null;
        OutputStream os = null;
        InputStream inPs = null;
        int responseCode = -1;
        String ip = getIpAddr(req);
        try {
            URL url = new URL(requestUrl);
            httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(60000);
            httpconn.setReadTimeout(60000);
            httpconn.setUseCaches(false);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            
			Enumeration<String> e =   req.getHeaderNames();
            httpconn.setRequestMethod(req.getMethod());

            while(e.hasMoreElements()){
                String key = e.nextElement();
                String value = req.getHeader(key);
                httpconn.setRequestProperty(key, value);
            }
            
            httpconn.setRequestProperty("X-Forwarded-For", ip);

            outPs = httpconn.getOutputStream();
            if(ros != null){
                write(outPs, req.getInputStream(),ros);
            }else{
                write(outPs, req.getInputStream());
            }
            outPs.flush();
           
            inPs = httpconn.getInputStream();
            responseCode = httpconn.getResponseCode();
            int i = 1;
            String header;
            resp.reset();
            while ((header = httpconn.getHeaderField(i)) != null) {
                String key = httpconn.getHeaderFieldKey(i);
                resp.addHeader(key, header);
                i++;
            }
            os = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(inPs);
            int n = -1;
            while ((n = bis.read(buffer,0,buffer.length)) != -1) {
                os.write(buffer, 0, n);
                os.flush();
            }
        } catch (Exception e) {
        	logger.error(String.format("%s%s | reqUrl=%s | ip=%s | resCode=%s", LOG_MSG, "httpRequest", requestUrl, ip, responseCode), e);
        } finally {
            try {
                if (inPs != null) {
                    inPs.close();
                    inPs = null;
                }
            } catch (IOException e) {
            	logger.error(String.format("%s%s | reqUrl=%s | ip=%s | resCode=%s", LOG_MSG, "httpRequest", requestUrl, ip, responseCode), e);
            }

            try {
                if (outPs != null) {
                    outPs.close();
                    outPs = null;
                }
            } catch (IOException e) {
            	logger.error(String.format("%s%s | reqUrl=%s | ip=%s | resCode=%s", LOG_MSG, "httpRequest", requestUrl, ip, responseCode), e);
            }
            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
            } catch (IOException e) {
            	logger.error(String.format("%s%s | reqUrl=%s | ip=%s | resCode=%s", LOG_MSG, "httpRequest", requestUrl, ip, responseCode), e);
            }

            //断开http连接
            httpconn.disconnect();
            //标记该对象为可回收
            httpconn = null;
            logger.info(String.format("%s%s | reqUrl=%s | ip=%s | resCode=%s", LOG_MSG, "httpRequest", requestUrl, ip, responseCode));
        }
    }
    
    private static void write(OutputStream out, InputStream is){
        BufferedInputStream bis = null;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is);
            int n = -1;
            while ((n = bis.read(buffer,0,buffer.length)) != -1) {
                out.write(buffer, 0, n);
                out.flush();
            }
        } catch (Exception e) {
        	logger.error(String.format("%s%s", LOG_MSG, "pipe"), e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }
    
    private static void write(OutputStream out, InputStream is, OutputStream rout){
        BufferedInputStream bis = null;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is);
            int n = -1;
            while ((n = bis.read(buffer,0,buffer.length)) != -1) {
                out.write(buffer, 0, n);
                out.flush();
                rout.write(buffer,0,n);
            }
        } catch (Exception e) {
        	logger.error(String.format("%s%s", LOG_MSG, "pipe"), e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }

    /**
     * @Desc 获取网站主域名
     * @param req
     * @return
     * String
     */
    public static String getDomainUrl(HttpServletRequest req){
    	StringBuffer url = req.getRequestURL();
		String contextUrl = url.delete(url.length() - req.getRequestURI().length(), url.length())
                            .append(req.getContextPath()).toString();
		return contextUrl;
    }
}
