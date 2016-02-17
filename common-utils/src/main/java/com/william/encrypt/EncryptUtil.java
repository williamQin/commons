package com.william.encrypt;

import java.security.MessageDigest;

import com.william.common.StrUtil;

public class EncryptUtil {
	
	private static String MD5 = "MD5";
	private static String SHA1 = "sha-1";
	
	private EncryptUtil(){}
	
	public static String MD5(String str){
		return encryptStr(str, EncryptUtil.MD5);
	}
	
	public static String SHA1(String str){
		return encryptStr(str, EncryptUtil.SHA1);
	}
	
	private static String encryptStr(String str, String type){
		if(StrUtil.isNull(str)){
			return "";
		}
		MessageDigest md = null;  
        try{  
        	md = MessageDigest.getInstance(type);  
        }catch (Exception e){  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] bytes = md.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < bytes.length; i++){  
            int val = ((int) bytes[i]) & 0xff;  
            if (val < 16){  
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));  
        } 
        
        return hexValue.toString();
	}

}
