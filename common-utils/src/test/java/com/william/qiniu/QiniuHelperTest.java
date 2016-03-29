package com.william.qiniu;

import org.junit.Test;

public class QiniuHelperTest {
	
	private static final String ACCESS_KEY = "iBarnstES53Sxmt4AujvCtvsKkv9UcyNNhbrWgTt";
	private static final String SECRET_KEY = "0AytuentJjZxdfycjqQfcYNbCRK5uSDvkewclDde";
	private static final String DOMAIN = "7xs9so.com1.z0.glb.clouddn.com";
	
	@Test
	public void uploadFileTest(){
		QiniuHelper helper = new QiniuHelper(ACCESS_KEY, SECRET_KEY);
		/*
		Response res = helper.uploadFile(new File("C:\\Users\\Administrator\\Desktop\\test.jpg"), "test-bucket", "test.jpg");
		System.out.println(JSON.toJSONString(res));
		*/
		try {
			String resourceUrl = helper.getResUrl(DOMAIN, "test.jpg", true);
			System.out.println(resourceUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
