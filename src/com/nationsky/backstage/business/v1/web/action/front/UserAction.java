package com.nationsky.backstage.business.v1.web.action.front;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.v1.bsc.ISecurityService;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.core.web.action.BaseAction;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "v1/user/")
public class UserAction extends BaseAction {
	static final Logger logger = LoggerFactory.getLogger(UserAction.class);
	@Autowired
	private ISecurityService securityService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request,HttpServletResponse response) {
		try {
			String phone = request.getParameter("phone");
			String password = request.getParameter("password");
			
			logger.info("login:"+ phone+"\t"+password);
			
			UserInfo userInfo = securityService.getUnique(UserInfo.class, Factor.create("phone", C.Eq, phone));
			if(userInfo!=null&&ValidateUtil.isEquals(userInfo.getPassword(), password)){
				logger.info("login success");
				response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, "userInfo", userInfo));
				return;
			}
			logger.info("login fail");
			responseMessage.setCode("1");
			responseMessage.setErrormsg("登录失败");
			response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage,null,null));
		} catch (IOException e) {
			try {
				logger.info("login fail");
				responseMessage.setCode("1");
				responseMessage.setErrormsg("登录失败");
				response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, null,null));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
}
