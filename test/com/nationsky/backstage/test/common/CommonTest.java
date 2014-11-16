package com.nationsky.backstage.test.common;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.HttpUtil;


public class CommonTest {
	static final Logger logger = LoggerFactory.getLogger(CommonTest.class);
	public static String urlStr = "http://127.0.0.1:8080/easygtd/v1/";//127.0.0.1:8080
	public static void main(String[] args) {
//		login();
//		getAuthCode();
//		submitAuthCode();
//		setPwd();
//		new Date(System.currentTimeMillis());
//		System.out.println(System.currentTimeMillis());
		getList();
	}
	
	//登录
	public static void login(){
		String childUrl = "user/login.ac";
		String queryString = "phone=18611866642&password=e10adc3949ba59abbe56e057f20f883e";
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

	//用户注册时用户设置的初始密码
	public static void setPwd(){
		String childUrl = "user/setPwd.ac"; 
		String queryString = "phone=18611866641&password=1111";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
	
	//5.	获取任务列表
	public static void getList(){
		String childUrl = "task/getList.ac"; 
		String queryString = "userId=3";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
}
