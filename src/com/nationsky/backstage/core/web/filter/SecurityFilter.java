/**
 * 
 */
package com.nationsky.backstage.core.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.core.web.util.WebUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：处理页面访问的安全问题
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class SecurityFilter implements Filter {
	static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest)req;
		Map<String,String[]> requestParameterMap = request.getParameterMap();
		logger.info("-- request params --");
//		logger.info(requestParameterMap.toString());
		for (String parmName : requestParameterMap.keySet()) {
			String[] parmValue = requestParameterMap.get(parmName);
			logger.info(parmName+":"+parmValue[0]);
		}
		HttpServletResponse response = (HttpServletResponse)res;
		if(WebUtil.isFirstFilter(SecurityFilter.class,request)){
			//防止POST方法站外提交
			if(ValidateUtil.isEqualsIgnoreCase(WebUtil.POST_METHOD, request.getMethod()) && WebUtil.isInvalidRequests(request)){
				response.sendError(404);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
