package com.william.radius;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;

/**
 * @Desc 对接radius服务器的工具类
 * @author Administrator
 *
 */
public class RadiusUtil {

	//radius服务器地址
	private String radiusHost;

	//radius服务的共享密钥
	private String sharedSecret;

	//radius服务开放端口
	private int authPort = 1812;

	public RadiusUtil(String radiusHost, String sharedSecret) {
		this.radiusHost = radiusHost;
		this.sharedSecret = sharedSecret;
	}

	public RadiusUtil(String radiusHost, int authPort, String sharedSecret) {
		this.radiusHost = radiusHost;
		this.authPort = authPort;
		this.sharedSecret = sharedSecret;
	}

	/**
	 * @Desc  radius服务认证接口(发送仅包含用户名和密码的数据包)
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean authenticate(String username, String password) throws Exception {
		RadiusClient rc = new RadiusClient(radiusHost, sharedSecret);
		rc.setAuthPort(authPort);
		return rc.authenticate(username, password);
	}

	/**
	 * @Desc radius服务认证接口(发送除用户名密码外包含有其他信息头的数据包)
	 * @param username
	 * @param password
	 * @param attributeMap
	 * @return
	 * @throws Exception
	 */
	public RadiusPacket authenticate(String username, String password, Map<String, String> attributeMap) throws Exception {
		// 1. Create a RadiusClient object with the host name and shared secret
		// of the Radius server you wish to contact.
		// You may set additional details (port numbers, for example) using
		// methods of this object.
		RadiusClient rc = new RadiusClient(radiusHost, sharedSecret);
		rc.setAuthPort(authPort);
		// 2.Create the Access-Request Radius packet. Pass the user name and
		// password in the constructor.
		// The User-Name attribute will be added on construction of the object,
		// while the User-Password attribute (PAP) or the CHAP-Password and
		// CHAP-Challenge attributes (CHAP) will be generated when encoding the
		// packet because the request authenticator of the packet is required to
		// encrypt the password.
		AccessRequest ar = new AccessRequest(username, password);
		// ar.setAuthProtocol(AccessRequest.AUTH_CHAP);
		// 3. Set further attributes.
		// Note that TinyRadius resolves the attribute type from the given type
		// name and that it converts the IP address and the name of the constant
		// (Login-User) to the right values.
		// Please also note how the Vendor-Specific (WISPr) sub-attribute
		// "WISPr-Location-ID" is set.
		// This call results in the creation of a Vendor-Specific attribute with
		// the proper vendor ID and the addition of a sub-attribute to this
		// attribute.
		
		//ar.addAttribute("NAS-Identifier", "visionchina");
		//ar.addAttribute("NAS-IP-Address", "127.0.0.1");
		//ar.addAttribute("Service-Type", "Login-User");
		
		// 4. Send the packet and receive the response.
		if(attributeMap != null){
			Set<String> keySet = attributeMap.keySet();
			if(keySet != null){
				Iterator<String> iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					ar.addAttribute(key, attributeMap.get(key));
				}
			}
		}
		
		RadiusPacket packet = rc.authenticate(ar);
		return packet;
	}
	
	/**
	 * @Desc 计费请求（向radius服务器发送计费请求数据包）
	 * @param username
	 * @param attributeMap
	 * @return
	 * @throws Exception
	 */
	public RadiusPacket accountStart(String username, Map<String, String> attributeMap) throws Exception {
		RadiusClient rc = new RadiusClient(radiusHost, sharedSecret);
		rc.setAuthPort(authPort);
		AccountingRequest request = new AccountingRequest(username, AccountingRequest.ACCT_STATUS_TYPE_START);
		if(attributeMap != null){
			Set<String> keySet = attributeMap.keySet();
			if(keySet != null){
				Iterator<String> iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					request.addAttribute(key, attributeMap.get(key));
				}
			}
		}
		return rc.account(request);
	}

	/**
	 * @Desc 停止计费(向radius服务器发送停止计费请求数据包)
	 * @param username
	 * @param attributeMap
	 * @return
	 * @throws Exception
	 */
	public RadiusPacket accountStop(String username, Map<String, String> attributeMap) throws Exception {
		RadiusClient rc = new RadiusClient(radiusHost, sharedSecret);
		rc.setAuthPort(authPort);
		AccountingRequest request = new AccountingRequest(username, AccountingRequest.ACCT_STATUS_TYPE_STOP);
		if(attributeMap != null){
			Set<String> keySet = attributeMap.keySet();
			if(keySet != null){
				Iterator<String> iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					request.addAttribute(key, attributeMap.get(key));
				}
			}
		}
		return rc.account(request);
	}
	
}
