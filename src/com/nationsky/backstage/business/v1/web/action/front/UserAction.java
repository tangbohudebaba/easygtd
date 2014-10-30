package com.nationsky.backstage.business.v1.web.action.front;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.JavaSmsApi;
import com.nationsky.backstage.business.v1.bsc.ISecurityService;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
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
	
	
	@RequestMapping(value = "getAuthCode", method = RequestMethod.POST)
	public void getAuthCode(HttpServletRequest request,HttpServletResponse response) {
		try {
			String phone = request.getParameter("phone");
			String type = request.getParameter("type");
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(type)){
				logger.info("getAuthCode fail");
				responseMessage.setCode("2");
				responseMessage.setErrormsg("手机号和验证码类型不能为空");
				response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, null,null));
				return;
			}
			AuthCode authCodePO1 = securityService.getUnique(AuthCode.class, Factor.create("type", C.Eq, Integer.parseInt(type)),Factor.create("phone", C.Eq, phone));
			if(authCodePO1!=null){
				securityService.remove(authCodePO1);
			}
			int authCode = (int) (Math.random()*9000+1000);
			//设置模板ID，如使用1号模板:您的验证码是#code#【#company#】
			long tpl_id=1;
			//设置对应的模板变量值
			String tpl_value="#code#="+authCode+"&#company#=北京智想天成科技有限公司";
			//模板发送
			String sendSmsResultStr = JavaSmsApi.tplSendSms(JavaSmsApi.APIKEY, tpl_id, tpl_value, phone);
			int sendSmsResultCode = (int)JSONObject.fromObject(sendSmsResultStr).get("code");
			if(sendSmsResultCode!=0){
				logger.info("getAuthCode fail");
				responseMessage.setCode("3");
				responseMessage.setErrormsg("获取验证码错误");
				response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, null,null));
				return;
			}
			//验证码保存到数据库
			AuthCode authCodePO = new AuthCode();
			authCodePO.setCreatedAtMillis(System.currentTimeMillis());
			authCodePO.setPhone(phone);
			authCodePO.setType(Integer.parseInt(type));
			authCodePO.setAuthCode(authCode);
			securityService.create(authCodePO);
			response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, "authCode", authCode));
			return;
		} catch (IOException e) {
			try {
				logger.info("getAuthCode fail");
				responseMessage.setCode("3");
				responseMessage.setErrormsg("获取验证码错误");
				response.getWriter().write(DateJsonValueProcessorUtil.secondJsonJoint(responseMessage, null,null));
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
}
