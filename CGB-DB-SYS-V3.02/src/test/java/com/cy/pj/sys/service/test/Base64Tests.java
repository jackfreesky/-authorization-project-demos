package com.cy.pj.sys.service.test;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Base64Tests {

	 @Test
	 void testBase64() {//Base64是可逆的加密算法
		 String pwd="123456";
		 //获取加密对象
		 Encoder encoder=Base64.getEncoder();
		 //加密
		 String hashedPwd=new String(encoder.encode(pwd.getBytes()));
		 System.out.println(hashedPwd);//MTIzNDU2
		 
		 //获取解密对象
		 Decoder decoder=Base64.getDecoder();
		 //解密
		 pwd=new String(decoder.decode(hashedPwd.getBytes()));
		 System.out.println(pwd);
		 
		 
		 
	 }
}
