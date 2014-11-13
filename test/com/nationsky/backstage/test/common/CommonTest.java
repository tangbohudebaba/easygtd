package com.nationsky.backstage.test.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.util.HttpUtil;


public class CommonTest {
	static final Logger logger = LoggerFactory.getLogger(CommonTest.class);
	public static String urlStr = "http://127.0.0.1:8080/easygtd/v1/";
	public static void main(String[] args) {
		login();
//		getAuthCode();
//		submitAuthCode();
//		setPwd();
//		System.out.println(System.currentTimeMillis());
	}
	
	//登录
	public static void login(){
		String childUrl = "user/login.ac";
		String queryString = "phone=18611866642&password=1111";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
	
	//获取验证码
	public static void getAuthCode(){
		String childUrl = "user/getAuthCode.ac";
		String queryString = "phone=18701588105&type=1";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
	
	//提交验证码
	public static void submitAuthCode(){
		String childUrl = "user/submitAuthCode.ac";
		String queryString = "phone=18701588105&type=1&authCode=1";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}

	//设置密码
	public static void setPwd(){
		String childUrl = "user/setPwd.ac"; 
		String queryString = "phone=18611866642&password=1111";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
	
	
}
