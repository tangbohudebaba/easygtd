package com.nationsky.backstage.business.v1.web.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "v1/task/")
public class TaskAction extends BusinessBaseAction {
	static final Logger logger = LoggerFactory.getLogger(TaskAction.class);
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	public void getList(HttpServletRequest request,HttpServletResponse response) {
//		String phone = request.getParameter("phone");
//		String password = request.getParameter("password");
//		logger.info("login:"+ phone+"\t"+password);
//		UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("phone", C.Eq, phone));
//		if(userInfo!=null&&ValidateUtil.isEquals(userInfo.getPassword(), password)){
//			msg = "login success";
//			responseWriter(msg, response, "userInfo", userInfo);
//			logger.info(msg);
//			return;
//		}
//		logger.info("login fail");
//		responseWriter("1", "登录失败", response);
	}
	
	
	
}
