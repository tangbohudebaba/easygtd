package com.nationsky.backstage.core.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 功能：异常处理
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class ExcptHandlerExceptionResolver extends
		SimpleMappingExceptionResolver {
	 protected ModelAndView doResolveException(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o, Exception e) {
	        httpServletRequest.setAttribute("ex", e);
	        return super.doResolveException(httpServletRequest, httpServletResponse, o, e);
	    }
}
