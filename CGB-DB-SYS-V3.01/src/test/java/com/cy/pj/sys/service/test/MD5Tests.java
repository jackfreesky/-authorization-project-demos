package com.cy.pj.sys.service.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
public class MD5Tests {
	@Test
	void testMD501() {//Spring tools
		String password="123456";
		byte[] hashedBytes=DigestUtils.md5Digest(password.getBytes());
		System.out.println(hashedBytes.length);//16byte==128bit
		String hashedPassword=DigestUtils.md5DigestAsHex((password).getBytes());
		System.out.println(hashedPassword);
	}
	@Test
	void testMd502() throws NoSuchAlgorithmException {
		//e10adc3949ba59abbe56e057f20f883e
		//e10adc3949ba59abbe56e057f20f883e
		String password="123456";
		//获取消息摘要对象
		MessageDigest md=MessageDigest.getInstance("MD5");
		//对内容进行加密
		byte[] hashedBytes=md.digest((password).getBytes());
		System.out.println(hashedBytes.length);//16byte==128bit
		//将字节数组中的内容转换为16进制的字符串
		StringBuilder sb=new StringBuilder();
		for(byte t:hashedBytes) {
			int a=t&0xFF;//位与，只要后8位，前面都设置为0
			String hexStr=Integer.toHexString(a);
			if(hexStr.length()==1) {
				hexStr='0'+hexStr;
			}
			sb.append(hexStr);
		}
		System.out.println(sb.toString());//e10adc3949ba59abbe56e057f20f883e
		
//		int a=011;//8进制
//		System.out.println(a);
//		int b=0x11;//16进制
//		System.out.println(b);
//		int c=11;//10进制
//		int d='A';//字符
//		int e=0b11;//二进制
//		System.out.println(e);
		
	}
	@Test
	void testMD503() {
		String salt=UUID.randomUUID().toString();
		System.out.println(salt);
		SimpleHash sh=new SimpleHash("MD5", "123456",salt,1);
		System.out.println(sh.toHex());//39f3c5cb02855df302c5335676c9bd34
		                               //39f3c5cb02855df302c5335676c9bd34
		                               //39f3c5cb02855df302c5335676c9bd34
	}
	
}
