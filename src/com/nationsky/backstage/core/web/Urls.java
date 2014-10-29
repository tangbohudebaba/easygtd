/**
 * 
 */
package com.nationsky.backstage.core.web;

/**
 * 功能：页面对应地址,命名重点在于说明用处
 * 属性命名规则：模块名_子模块名_地址
 * 示例：USER_LOGIN_FORGOT_PASSWORD
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface Urls {
	
	/*
	 * 302重定向
	 */
	String REDIRECT = "/redirect";
	/*
	 * 301重定向
	 */
	String REDIRECT_301 = "/301redirect";
	/*
	 * 转向
	 */
	String FORWARD = "/forward";
	/*
	 * 页面METE标签转向
	 */
	String REFRESH = "/refresh";
	/*
	 * 异常页面
	 */
	String EXCEPTION = "/excpt";
	/*
	 * 程序处理文本页面比如Ajax返回信息或JSON
	 */
	String TEXT = "/text";
	
}
