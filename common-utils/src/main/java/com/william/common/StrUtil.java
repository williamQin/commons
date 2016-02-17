package com.william.common;

public class StrUtil {
	
	private StrUtil(){
	}
	
	public static boolean isNull(String str){
		return str == null || str.length() == 0;
	}
	
	public static boolean isNotNull(String str){
		return !isNull(str);
	}

}
