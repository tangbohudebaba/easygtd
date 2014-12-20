package com.nationsky.backstage.business.common;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.core.web.action.BaseAction;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.ValidateUtil;

public abstract class BusinessBaseAction extends BaseAction{
	static final Logger logger = LoggerFactory.getLogger(BusinessBaseAction.class);
	/**
	 * 响应仅返回成功状态码
	 * @param response
	 * @return
	 */
	public static boolean responseWriter(HttpServletResponse response) {
		return responseWriter(null, null, response, null, null);
	}
	
	/**
	 * 响应返回成功状态码和内容
	 * @param response
	 * @param accumulateKey 子节点名称
	 * @param child 子节点(实体类或者实体类集合或者任意对象包括八大基本类型)
	 * @return
	 */
	public static boolean responseWriter(HttpServletResponse response, String accumulateKey, Object child) {
		return responseWriter(null, null, response, accumulateKey, child);
	}
	
	/**
	 * 响应返回成功状态码和内容
	 * @param response
	 * @param accumulateKey 子节点名称
	 * @param child 子节点(实体类或者实体类集合或者任意对象包括八大基本类型)
	 * @return
	 */
	public static boolean responseWriter(HttpServletResponse response, Map<String,Object> childMap) {
		return responseWriter(null, null, response, childMap);
	}
	
	/**
	 * 响应返回成功状态码0，消息，内容
	 * @param msg
	 * @param response
	 * @param accumulateKey 子节点名称
	 * @param child 子节点(实体类或者实体类集合或者任意对象包括八大基本类型)
	 * @return
	 */
	public static boolean responseWriter(String msg, HttpServletResponse response, String accumulateKey, Object child) {
		return responseWriter(null, msg, response, accumulateKey, child);
	}
	
	/**
	 * 响应返回状态码和信息
	 * @param code
	 * @param msg
	 * @param response
	 * @return
	 */
	public static boolean responseWriter(String code, String msg, HttpServletResponse response) {
		return responseWriter(code, msg, response, null, null);
	}
	
	/**
	 * 响应返回状态码，信息，内容
	 * @param code
	 * @param msg
	 * @param response
	 * @param accumulateKey 子节点名称
	 * @param child 子节点(实体类或者实体类集合或者任意对象包括八大基本类型)
	 * @return
	 */
	public static boolean responseWriter(String code, String msg, HttpServletResponse response, String accumulateKey, Object child) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			if(ValidateUtil.isNotNull(code)){
				responseMessage.setCode(code);
			}
			logger.info("response code : {}", responseMessage.getCode());
			if(ValidateUtil.isNotNull(msg)){
				responseMessage.setMsg(msg);
			}
			String responseJsonStr = DateJsonValueProcessorUtil.secondJsonJoint(responseMessage,accumulateKey,child);
			logger.debug(responseJsonStr);
			response.getWriter().write(responseJsonStr);
			return true;
		} catch (IOException e) {
			logger.error(e.toString());
			return false;
		}
	}
	
	public static boolean responseWriter(String code, String msg, HttpServletResponse response, Map<String,Object> childMap) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			if(ValidateUtil.isNotNull(code)){
				responseMessage.setCode(code);
			}
			logger.info("response code : {}", responseMessage.getCode());
			if(ValidateUtil.isNotNull(msg)){
				responseMessage.setMsg(msg);
			}
			String responseJsonStr = DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, childMap);
			logger.debug(responseJsonStr);
			response.getWriter().write(responseJsonStr);
			return true;
		} catch (IOException e) {
			logger.error(e.toString());
			return false;
		}
	}
	
}
