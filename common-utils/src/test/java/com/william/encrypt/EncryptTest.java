package com.william.encrypt;

import org.junit.Test;

public class EncryptTest {
	
	@Test
	public void testMd5(){
		System.out.println(EncryptUtil.MD5("qinyuxi"));
	}
	
	@Test
	public void testSha1(){
		System.out.println(EncryptUtil.SHA1("qinyuxi"));
	}
	
	@Test
	public void testDES(){
		String key = "12365455555555";
		String str = "I'm a student!";
		try {
			String encrypt = DESUtil.encrypt(str, key);
			System.out.println(encrypt);
			String decrypt = DESUtil.decrypt(encrypt, key);
			System.out.println(decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
