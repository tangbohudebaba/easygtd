/**
 * 
 */
package com.nationsky.backstage.core.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.nationsky.backstage.business.common.ResponseMessage;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：所有ACTION都要继承这个类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class BaseAction {
	
	protected static final String MESSAGE_ATTRIBUTE = "message";
	protected static final String ERROR = "error";
	protected ResponseMessage responseMessage = new ResponseMessage();
	
	/**
	 * 前端报错信息处理Map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,String> getMessageMap(){
		Map<String,String> messages = (Map<String, String>) this.getRequest().getAttribute(MESSAGE_ATTRIBUTE);
		if(messages==null)messages = new HashMap<String,String>();
		if(getRequest().getAttribute(MESSAGE_ATTRIBUTE)==null)
			getRequest().setAttribute(MESSAGE_ATTRIBUTE,messages);
		return messages;
	}
	
	/**
	 * 仅获得一次如:验证码
	 * @param param
	 * @return
	 */
	protected String getAndRemoveSessionAttribute(String param){
		Assert.isTrue(ValidateUtil.isNotNull(param));
		String rand = (String)getRequest().getSession(false).getAttribute(param);
		getRequest().getSession(false).removeAttribute(param);
		return rand;
	}
	
	/**
	 * 获得参数,如果是多选框用connector衔接
	 * @param param
	 * @param connector
	 * @return
	 */
	protected String getParameters(String param,String connector){
		Assert.isTrue(ValidateUtil.isNotNull(param));
		String[] str = (String[])getRequest().getParameterValues(param);
		if(str != null){
			StringBuilder sb = new StringBuilder();
			for(int i=0,j=str.length;i<j;i++){
				if(i>0)sb.append(connector);
				sb.append(str[i]);
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * 获得request对象
	 * @return
	 */
	protected HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 获得ModelAndView对象
	 * @param string
	 * @return
	 */
	protected ModelAndView getModelAndView(String string) {
		return new ModelAndView(string);
	}
}
