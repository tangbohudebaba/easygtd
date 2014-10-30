package com.nationsky.backstage.test.common;

import com.nationsky.backstage.util.HttpUtil;


public class CommonTest {
	public static String urlStr = "http://127.0.0.1:8080/easygtd/v1/";
	public static void main(String[] args) {
//		login();
		getAuthCode();
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
		String queryString = "phone=18611866642&type=1";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}

}
