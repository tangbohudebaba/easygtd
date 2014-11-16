package com.nationsky.backstage.business.v1.web.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
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
		String code = "7", msg = "获取任务列表失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			commonService.findList(TaskInfo.class, 0, Integer.MAX_VALUE, "beginTime:desc", Factor.create("userId", C.Eq, userId));
			//UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
//			if(userInfo==null){
//				userInfo = new UserInfo();
//				userInfo.setPhone(phone);
//				userInfo.setPassword(password);
//				commonService.create(userInfo);
//				code = "0";
//				responseWriter(response);
//			}else{
//				 code = "6";
//				 msg = "此手机号已经存在";
//				throw new Exception();
//			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		//logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	
	}
	
	
	
}
