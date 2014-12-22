/**
 * 
 */
package com.nationsky.backstage.business.common;

import java.util.HashMap;
import java.util.Map;

/** 
 * @title : 
 * @description : 
 * @projectname : easygtd
 * @classname : ResponseMessage
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年10月30日 下午2:07:03 
 */
public class ResponseMessage {
	public static Map<String,String> statusCode = new HashMap<String, String>();
	public static final String notifyExpiry = "10";
	private String code = "0";
	private String msg = "";
	
	static{
		statusCode.put(notifyExpiry, "此通知已失效");
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
