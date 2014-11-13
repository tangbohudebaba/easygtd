/**
 * 
 */
package com.nationsky.backstage.business.common;

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
	private String code = "0";
	private String msg = "";
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
