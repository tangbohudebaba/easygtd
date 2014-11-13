package com.nationsky.backstage.business.v1.web.action.front;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.common.JavaSmsApi;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "v1/user/")
public class UserAction extends BusinessBaseAction {
	static final Logger logger = LoggerFactory.getLogger(UserAction.class);
	
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request,HttpServletResponse response) {
		String code = "1", msg = "login fail";//错误默认值
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		try {
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("phone", C.Eq, phone));
			if(userInfo!=null&&ValidateUtil.isEquals(userInfo.getPassword(), password)){
				code = "0";
				msg = "login success";
				responseWriter(msg, response, "userInfo", userInfo);
			}else {
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	}
	
	
	/**
	 * 获取短信验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getAuthCode", method = RequestMethod.POST)
	public void getAuthCode(HttpServletRequest request,HttpServletResponse response) {
		String code = "3", msg = "手机号和验证码类型不能为空";//错误默认值
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(type)){
				code = "2";
				msg = "手机号和验证码类型不能为空";
				throw new Exception();
			}
			AuthCode authCodePO1 = commonService.getUnique(AuthCode.class, Factor.create("type", C.Eq, Integer.parseInt(type)),Factor.create("phone", C.Eq, phone));
			if(authCodePO1!=null){
				commonService.remove(authCodePO1);
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
				throw new Exception();
			}
			//验证码保存到数据库
			AuthCode authCodePO = new AuthCode();
			authCodePO.setCreatedAtMillis(System.currentTimeMillis());
			authCodePO.setPhone(phone);
			authCodePO.setType(Integer.parseInt(type));
			authCodePO.setAuthCode(authCode);
			commonService.create(authCodePO);
			code = "0";
			responseWriter(response, "authCode", authCode);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	}
	
	/**
	 * 提交验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "submitAuthCode", method = RequestMethod.POST)
	public void submitAuthCode(HttpServletRequest request,HttpServletResponse response) {
		String code = "4", msg = "验证码错误";//错误默认值
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
		String authCode = request.getParameter("authCode");
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(type)||ValidateUtil.isNull(authCode)){
				throw new Exception();
			}
			AuthCode authCodePO = commonService.getUnique(AuthCode.class, Factor.create("type", C.Eq, Integer.parseInt(type)),Factor.create("phone", C.Eq, phone), Factor.create("authCode", C.Eq, Integer.parseInt(authCode)));
			if(authCodePO!=null){
				commonService.remove(authCodePO);
				code = "0";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	}
	
	/**
	 * 用户注册时用户设置的初始密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "setPwd", method = RequestMethod.POST)
	public void setPwd(HttpServletRequest request,HttpServletResponse response) {
		String code = "5", msg = "设置密码错误";//错误默认值
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(password)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
			if(userInfo!=null){
				userInfo.setPassword(password);
				commonService.update(userInfo);
				code = "0";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	}
	
}
