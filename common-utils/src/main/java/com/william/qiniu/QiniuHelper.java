package com.william.qiniu;

import java.io.File;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * @Desc 七牛辅助工具类(文档参照http://developer.qiniu.com/code/v7/sdk/java.html)
 * @author Administrator
 *
 */
public class QiniuHelper {
	
	/**
	 * Qiniu开发者账号access_key
	 */
	private String accessKey;
	
	/**
	 * Qiniu开发者账号secretKey
	 */
	private String secretKey;
	
	private Auth auth;
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public QiniuHelper(String accessKey, String secretKey){
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		auth = Auth.create(accessKey, secretKey);
	}
	
	/**
	 * 
	 * @param file 上传的文件
	 * @param bucketname 上传的空间名
	 * @param key 上传到七牛的文件名 
	 * @param coverdEnable 如果文件存在，是否覆盖
	 * @return
	 */
	public Response uploadFile(File file, String bucketname, String key, boolean coverdEnable){
		UploadManager manager = new UploadManager();
		try {
			//调用put方法上传文件
			Response response = manager.put(file, key, getUpToken(bucketname));
			return response;
		} catch (QiniuException e) {
			e.printStackTrace();
			return e.response;
		}
	}
	
	/**
	 * @Desc 获取指定文件名的上传token
	 * @param bucket 上传空间名
	 * @param key 存储在七牛的文件名
	 * @return
	 */
	public String getUpToken(String bucket, String key){
			return auth.uploadToken(bucket, key);
	}
	
	/**
	 * @Desc 获取默认的上传token
	 * @param bucket 上传空间名
	 * @return
	 */
	public String getUpToken(String bucket){
		return auth.uploadToken(bucket);
	}
	
	/**
	 * @Desc 生成上传文件的token
	 * @param bucket 空间名
	 * @param key 存储在七牛的文件名
	 * @param expires token的过期时间
	 * @param insertOnly 是否仅允许上传不允许修改
	 * @return
	 */
	public String getUpToken(String bucket, String key, Long expires, boolean insertOnly){
		//<bucket>:<key>，表示只允许用户上传指定key的文件。在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
	    //如果希望只能上传指定key的文件，并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1。
	    //第三个参数是token的过期时间
		StringMap strict = new StringMap();
		if(insertOnly) 
			strict.put("insertOnly", "1");
		else
			strict.put("insertOnly", "0");
		return auth.uploadToken(bucket, key, expires, strict);
	}
	
	/**
	 * @Desc 获取公有空间资源的下载路径
	 * @param domain 七牛域名
	 * @param key 文件名称
	 * @return
	 */
	public String getPublicResUrl(String domain, String key){
		return "http://" + domain + "/" + key;
	}
	
	/**
	 * @Desc 获取资源访问路径
	 * @param domain 七牛域名
	 * @param key 文件名
	 * @param isPublic 是否是共有空间
	 * @return
	 */
	public String getResUrl(String domain, String key, boolean isPublic){
		String baseUrl = getPublicResUrl(domain, key);
		if(isPublic)
			return baseUrl;
		else{
			return auth.privateDownloadUrl(baseUrl);
		}
	}
	
	/**
	 * @Desc 获取资源访问路径，如果是私有空间，指定url有效期，默认为一年
	 * @param domain 七牛域名
	 * @param key 文件名
	 * @param isPublic 是否公有空间
	 * @param expires 有效期
	 * @return
	 */
	public String getResUrl(String domain, String key, boolean isPublic, Long expires){
		String baseUrl = getPublicResUrl(domain, key);
		if(isPublic)
			return baseUrl;
		else{
			return auth.privateDownloadUrl(baseUrl, expires != null ? expires : 3600*24*365);
		}
	}
}
