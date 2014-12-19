package com.nationsky.backstage.business.v1.web.action.front;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.common.JavaSmsApi;
import com.nationsky.backstage.business.v1.V1Constants;
import com.nationsky.backstage.business.v1.Handler.NotifyHandler;
import com.nationsky.backstage.business.v1.Handler.TaskInfoHandler;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.Suggest;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.core.Identities;
import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;
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
		String pushToken = request.getParameter("pushToken");
		try {
			if(ValidateUtil.isNull(phone) || ValidateUtil.isNull(pushToken) || ValidateUtil.isNull(password)){
				msg = "phone,pushToken,password can't null";
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("phone", C.Eq, phone));
			if(userInfo == null){
				msg = "手机号未注册 ";
				throw new Exception();
			}
			if(ValidateUtil.isEquals(userInfo.getPassword(), password)){
				List<UserInfo> userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE,null, Factor.create("pushToken", C.Like, "%"+pushToken+"%"));
				for (UserInfo userInfo2 : userInfoList) {
					userInfo2.setPushToken(userInfo2.getPushToken().replace(pushToken, "").trim());
					commonService.update(userInfo2);
				}
				if(ValidateUtil.isNotNull(userInfo.getPushToken())){
					if(!userInfo.getPushToken().contains(pushToken)){
						userInfo.setPushToken((userInfo.getPushToken()+" "+pushToken).trim());
					}
				}else{
					userInfo.setPushToken(pushToken);
				}
				commonService.update(userInfo);
				code = "0";
				msg = "login success";
				responseWriter(msg, response, "userInfo", userInfo);
			}else {
				msg = "密码错误";
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},password:{},code:{},msg:{}",phone,password,code,msg);
	}
	
	/**
	 * 33用户登出
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest request,HttpServletResponse response) { 
		String code = "8", msg = "logout fail";//错误默认值
		String userId = request.getParameter("userId");
		String pushToken = request.getParameter("pushToken");
		try {
			if(ValidateUtil.isNull(userId) || ValidateUtil.isNull(pushToken)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				userInfo.setPushToken(userInfo.getPushToken().replaceAll(pushToken, "").replace("  ", " ").trim());
				commonService.update(userInfo);
				code = "0";
				msg = "logout success";
				responseWriter(msg, response, "userInfo", userInfo);
			}else {
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
	/**
	 * 34上传推送token
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/uploadPushToken", method = RequestMethod.POST)
	public void uploadPushToken(HttpServletRequest request,HttpServletResponse response) { 
		String code = "8", msg = "";//错误默认值
		String userId = request.getParameter("userId");
		String oldPushToken = request.getParameter("oldPushToken");
		String newPushToken = request.getParameter("newPushToken");
		try {
			if(ValidateUtil.isNull(userId) || ValidateUtil.isNull(oldPushToken) || ValidateUtil.isNull(newPushToken)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				List<UserInfo> userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE,null, Factor.create("pushToken", C.Like, "%"+newPushToken+"%"));
				for (UserInfo userInfo2 : userInfoList) {
					userInfo2.setPushToken(userInfo2.getPushToken().replace(newPushToken, "").trim());
					commonService.update(userInfo2);
				}
				if(ValidateUtil.isNotNull(userInfo.getPushToken())){
					userInfo.setPushToken((userInfo.getPushToken().replace(oldPushToken, newPushToken)).trim());
				}else{
					userInfo.setPushToken(newPushToken);
				}
				commonService.update(userInfo);
				code = "0";
				msg = "";
				responseWriter(msg, response, "userInfo", userInfo);
			}else {
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("oldPushToken:{},newPushToken:{},code:{},msg:{}",oldPushToken,newPushToken,code,msg);
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
		String type = request.getParameter("type");//
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(type)){
				code = "2";
				msg = "手机号和验证码类型不能为空";
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
			if(userInfo!=null&&ValidateUtil.isEquals("1", type)){
				 code = "6";
				 msg = "此手机号已经存在";
				throw new Exception();
			}else if(userInfo == null && ValidateUtil.isEquals("3", type)){
				 code = "9";
				 msg = "此手机号未注册";
				throw new Exception();
			}
			
			AuthCode authCodePO1 = commonService.getUnique(AuthCode.class, Factor.create("type", C.Eq, Integer.parseInt(type)),Factor.create("phone", C.Eq, phone));
			if(authCodePO1!=null){
				commonService.remove(authCodePO1);
			}
			int authCode = (int) (Math.random()*9000+1000);
//			int authCode = 1234;
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
			msg = "";
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
				msg = "";
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
		String code = "5", msg = "设置初始密码错误";//错误默认值
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(password)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
			if(ValidateUtil.isEquals("1", type)){//注册
				if(userInfo==null){
					userInfo = new UserInfo();
					userInfo.setPhone(phone);
					userInfo.setName(name);
					userInfo.setPassword(password);
					commonService.create(userInfo);
					code = "0";
					msg = "";
					responseWriter(response);
				}else{
					 code = "6";
					 msg = "此手机号已经存在";
					throw new Exception();
				}
			}else if(ValidateUtil.isEquals("3", type)){
				if(userInfo!=null){
					userInfo.setPassword(password);
					commonService.update(userInfo);
					code = "0";
					msg = "";
					responseWriter(response);
				}else{
					 code = "9";
					 msg = "";
					throw new Exception();
				}
			}
			
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
	}
	
	/**
	 * 28.	修改密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updatePwd", method = RequestMethod.POST)
	public void updatePwd(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "";//错误默认值
		String userId = request.getParameter("userId");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(oldPassword)||ValidateUtil.isNull(newPassword)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				if(userInfo.getPassword() != oldPassword){
					throw new Exception();
				}
				userInfo.setPassword(newPassword);
				commonService.update(userInfo);
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{}, oldPassword:{}, newPassword:{}, code:{}, msg:{}",userId, oldPassword, newPassword, code, msg);
	}
	
	/**
	 * 25.	获取设置信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getSetInfo", method = RequestMethod.POST)
	public void getSetInfo(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo != null){
				code = "0";
				msg = "";
				responseWriter(response,"privateSetting",userInfo.getPrivateType());
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}",code,msg);
	}
	
	/**
	 * 29.	获取用户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
	public void getUserInfo(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo != null){
				code = "0";
				msg = "";
				responseWriter(response,"userInfo",userInfo);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}",code,msg);
	}
	
	/**
	 * 17获取好友列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getBuddyList", method = RequestMethod.POST)
	public void getBuddyList(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			
			if(userInfo != null){
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();
				String buddyUserIds = userInfo.getBuddyUserIds();
				if(ValidateUtil.isNotNull(buddyUserIds)){
					buddyUserIds = buddyUserIds.trim();
					String[] buddyUserIdStrArray = buddyUserIds.split(" ");
					Integer[] buddyUserIdArray = new Integer[buddyUserIdStrArray.length];
					for (int i = 0; i < buddyUserIdStrArray.length; i++) {
						buddyUserIdArray[i] =Integer.parseInt(buddyUserIdStrArray[i]);
					}
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("id", C.Ne, userInfo.getId()), Factor.create("id", C.In, buddyUserIdArray));
				}
				code = "0";
				msg = "";
				responseWriter(response,"members",userInfoList);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}",code,msg);
	}
	
	/**
	 * 26.	设置隐私设置 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "setPrivate", method = RequestMethod.POST)
	public void setPrivate(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "";//错误默认值
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		String members = request.getParameter("members");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(type)){
				throw new Exception();
			}
			if(ValidateUtil.isEquals("1", type) && ValidateUtil.isNull(members)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				
				userInfo.setPrivateType(Integer.parseInt(type));
				userInfo.setPrivateUserIds(members);
				commonService.update(userInfo);
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{}, privateType:{}, privateUserIds:{}, code:{}, msg:{}",userId, type, members, code, msg);
	}
	
	/**
	 * 18.	搜索人员
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "searchPerson", method = RequestMethod.POST)
	public void searchPerson(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String keyword = request.getParameter("keyword");
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(keyword)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			List<UserInfo> userInfoList = null;
			if(userInfo != null){
				if(keyword.matches("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")){
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("phone", C.Eq, keyword), Factor.create("phone", C.Ne, userInfo.getPhone()));
				}else{
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("name", C.Like, "%"+keyword+"%"), Factor.create("name", C.Ne, userInfo.getName()));
				}
				
				for (UserInfo userInfo2 : userInfoList) {
					if(ValidateUtil.isNotNull(userInfo2.getBuddyUserIds()) && userInfo2.getBuddyUserIds().contains(userId)){
						userInfo2.setUserStatus("1");
					}else{
						userInfo2.setUserStatus("2");
					}
				}
				code = "0";
				msg = "";
				responseWriter(response,"members",userInfoList);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}",code,msg);
	}
	
	/**
	 * 32获取人员的注册状态
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getUserStatus", method = RequestMethod.POST)
	public void getUserStatus(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");//当前用户id
		String phones = request.getParameter("phones");//要获取状态的用户手机号,空格分开
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(phones)){
				throw new Exception();
			}
			phones = phones.replaceAll("\\+86|-", "").replaceAll(" {2,}", " ");
			
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			List<UserInfo> userInfoList = null;
			if(userInfo != null){
				Map<String,String> userStatusMap = new HashMap<String, String>();
				StringBuffer sbUserStatus1 = new StringBuffer();
				StringBuffer sbUserStatus2 = new StringBuffer();
				StringBuffer sbUserStatus3 = new StringBuffer();
				
				
				List<String> regPhoneList = new ArrayList<String>();
				userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("phone", C.In, phones.split(" ")), Factor.create("phone", C.Ne, userInfo.getPhone()));
				for (UserInfo userInfo2 : userInfoList) {
					regPhoneList.add(userInfo2.getPhone());
					if(ValidateUtil.isNotNull(userInfo2.getBuddyUserIds()) && userInfo2.getBuddyUserIds().contains(userId)){
						sbUserStatus1.append(userInfo2.getPhone()+" ");
					}else{
						sbUserStatus2.append(userInfo2.getPhone()+" ");
					}
				}
				
				List<String> phoneList = Arrays.asList(phones.split(" "));
				for (String phone : phoneList) {
					if(!regPhoneList.contains(phone)){
						sbUserStatus3.append(phone+" ");
					}
				}
				
				userStatusMap.put("userStatus1", sbUserStatus1.toString().trim().replaceAll(" {2,}", " "));
				userStatusMap.put("userStatus2", sbUserStatus2.toString().trim().replaceAll(" {2,}", " "));
				userStatusMap.put("userStatus3", sbUserStatus3.toString().trim().replaceAll(" {2,}", " "));
				
				code = "0";
				msg = "";
				responseWriter(response,"members",userStatusMap);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}",code,msg);
	}
	/**
	 * 19.	添加好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "addBuddy", method = RequestMethod.POST)
	public void addBuddy(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String buddyUserId = request.getParameter("buddyUserId");
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			if(ValidateUtil.isEquals(userId, buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
				if(ValidateUtil.isNotNull(userInfo.getBuddyUserIds()) && userInfo.getBuddyUserIds().contains(buddyuserInfo.getId().toString())){
					code = "0";
					msg = "";
					responseWriter(response,"userStatus","1");
					return;
				}
			List<Notify> notifyList = commonService.findList(Notify.class, 0, Integer.MAX_VALUE, null, Factor.create("userId", C.Eq, Integer.parseInt(buddyUserId)), Factor.create("fromUserId", C.Eq, Integer.parseInt(userId)), Factor.create("type", C.Eq, 7));
			if(ValidateUtil.isNullCollection(notifyList) || notifyList.size() == 0){
					//生成通知
				NotifyHandler.createNotify(Integer.parseInt(buddyUserId), Integer.parseInt(userId), -1, 7);
//					Notify notify = new Notify();
//					notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//					notify.setType(7);
//					notify.setUserId(Integer.parseInt(buddyUserId));//被通知用户ID
//					commonService.create(notify);
				}
				
				code = "0";
				msg = "";
				responseWriter(response,"userStatus","2");
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},buddyUserId:{}", code, msg, userId, buddyUserId);
	}
	
	/**
	 *20.	邀请好友(未完成)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "inviteBuddy", method = RequestMethod.POST)
	public void inviteBuddy(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String phone = request.getParameter("phone");
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(phone)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
			
			if(userInfo != null && buddyuserInfo == null){//如果好友没注册过,发短信
				if(ValidateUtil.isEquals(userInfo.getPhone(), phone)){
					throw new Exception();
				}
				//走短信..(待开发)
				code = "0";
				msg = "";
				responseWriter(response,"userStatus","3");
			}else if (userInfo != null && buddyuserInfo != null){//如果好友已经注册过,走添加好友流程
				if(ValidateUtil.isEquals(userInfo.getPhone(), phone)){
					throw new Exception();
				}
				request.getRequestDispatcher("addBuddy.ac?buddyUserId="+buddyuserInfo.getId()).forward((ServletRequest)request,(ServletResponse)response);
				return;
				//request.setAttribute("buddyUserId", buddyuserInfo.getId());
				//addBuddy(request, response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},phone:{}", code, msg, userId, phone);
	}
	
	/**
	 *21.	同意拒绝添加好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "agreeOrRejectBuddy", method = RequestMethod.POST)
	public void agreeOrRejectBuddy(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String buddyUserId = request.getParameter("buddyUserId");
		String isAgree = request.getParameter("isAgree");//是否同意，1为同意
		String notifyId = request.getParameter("notifyId");//通知Id
		
		try {
			if(ValidateUtil.isNull(userId) || ValidateUtil.isNull(buddyUserId) || ValidateUtil.isNull(notifyId)){
				throw new Exception();
			}
			if(ValidateUtil.isEquals(userId, buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			
			if(userInfo != null && buddyuserInfo != null){
				if(ValidateUtil.isNotNull(userInfo.getBuddyUserIds()) && userInfo.getBuddyUserIds().contains(buddyuserInfo.getId().toString())){
					msg = "已经是好友";
					code = "0";
					msg = "";
					responseWriter(response);
					return;
				}
				if(ValidateUtil.isEquals("1", isAgree)){
					if(ValidateUtil.isNull(userInfo.getBuddyUserIds())){
						userInfo.setBuddyUserIds(buddyuserInfo.getId().toString());
					}else{
						userInfo.setBuddyUserIds((userInfo.getBuddyUserIds()+" "+buddyuserInfo.getId()).trim());
					}
					if(ValidateUtil.isNull(buddyuserInfo.getBuddyUserIds())){
						buddyuserInfo.setBuddyUserIds(userInfo.getId().toString());
					}else{
						buddyuserInfo.setBuddyUserIds((buddyuserInfo.getBuddyUserIds()+" "+userInfo.getId()).trim());
					}
					commonService.update(userInfo);
					commonService.update(buddyuserInfo);
				}
				//删除通知
				NotifyHandler.deleteNotifyById(Integer.parseInt(notifyId));
				//生成通知
				if(ValidateUtil.isEquals("1", isAgree)){
					NotifyHandler.createNotify(Integer.parseInt(buddyUserId), Integer.parseInt(userId), -1, 10);
				}else{
					NotifyHandler.createNotify(Integer.parseInt(buddyUserId), Integer.parseInt(userId), -1, 11);
				}
//				
//				Notify notify = new Notify();
//				notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//				if(ValidateUtil.isEquals("1", isAgree)){
//					notify.setType(10);
//				}else{
//					notify.setType(11);
//				}
//				notify.setUserId(Integer.parseInt(buddyUserId));//被通知用户ID
//				commonService.create(notify);
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},buddyUserId:{},isAgree:{}", code, msg, userId, buddyUserId, isAgree);
	}
	
	/**
	 *22.	获取邀请列表(别人请求加我为好友的通知列表)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getInviteList", method = RequestMethod.POST)
	public void getInviteList(HttpServletRequest request,HttpServletResponse response) {
		String code = "7", msg = "获取任务列表失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			DateUtil.getDate(new Date());
			List<Notify> notifyList = commonService.findList(Notify.class, 0, Integer.MAX_VALUE, "updatedAt:desc", Factor.create("userId", C.Eq, Integer.parseInt(userId)), Factor.create("type", C.In, new Integer[]{7,10,11}));
			for (int i = 0; i < notifyList.size(); i++) {
					UserInfo fromuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, notifyList.get(i).getFromUserId()));
					notifyList.get(i).setFromUserName(fromuserInfo.getName());
			}
			code = "0";
			msg = "";
			responseWriter(response, "notifications", notifyList);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
	/**
	 *23.	查询人员某个时间段内是否有任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "findTimeBucketTasks", method = RequestMethod.POST)
	public void findTimeBucketTasks(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String buddyUserId = request.getParameter("buddyUserId");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		try {
			if(ValidateUtil.isNull(userId) || ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
				String hqlTmp = "select new list(t.beginTime) from TaskInfo as t , TaskInfoAndUserInfo as tu where tu.taskId = t.id and tu.userId = %s and tu.isDone = 0 and tu.isAgree = 1 and t.beginTime >= %s and t.endTime <= %s";//
				String hql =String.format(hqlTmp, buddyUserId, beginTime,  endTime);
				List<List<Long>> taskInfoList = (List<List<Long>>)commonService.findList(hql, 0, Integer.MAX_VALUE);
				Long[] beginTimeArray = new Long[taskInfoList.size()];
				for (int i = 0; i < taskInfoList.size(); i++) {
					beginTimeArray[i] = taskInfoList.get(i).get(0);
				}
				code = "0";
				msg = "";
				responseWriter(response,"times",beginTimeArray);
			}else{
				code = "8";
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},msg:{}",userId,buddyUserId);
	}
	
	/**
	 *24.	获取人员某天任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getDayTasks", method = RequestMethod.POST)
	public void getDayTasks(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String buddyUserId = request.getParameter("buddyUserId");
		String date = request.getParameter("date");//当天0点时间戳GTM(秒数)
		
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
			
				
				
				String hqlTmp = "select new list(t.id) from TaskInfo as t , TaskInfoAndUserInfo as tu where tu.taskId = t.id and tu.userId = %s and tu.isAgree = 1 and t.beginTime >= %s and t.beginTime <= %s";
				String hql =String.format(hqlTmp, buddyUserId, date,  Integer.parseInt(date)+24*3600);
				List<List<Integer>> taskIdList = (List<List<Integer>>)commonService.findList(hql, 0, Integer.MAX_VALUE);
				List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
				for (int i = 0; i < taskIdList.size(); i++) {
					int taskId = taskIdList.get(i).get(0);
					TaskInfo taskInfo = TaskInfoHandler.getUserTaskInfo(Integer.parseInt(buddyUserId), taskId);
					if(taskInfo != null){
						boolean isLook = true;//是否可以看这个人的任务详情
						//1：指定人可见、2：只有任务相关人员可见[默认]、3：所有人可见；4：所有人不可见
						if(buddyuserInfo.getPrivateType() == 1){
							if(ValidateUtil.isNull(buddyuserInfo.getPrivateUserIds()) || !buddyuserInfo.getPrivateUserIds().contains(userId)){
								isLook = false;
							}
						}else if(buddyuserInfo.getPrivateType() == 2){
							if(!taskInfo.getMemberUserIds().contains("\"id\":"+userId)){//如果这个人不在此任务的成员里
								isLook = false;
							}
							//在下面的for循环里面判断
						}else if(buddyuserInfo.getPrivateType() == 4){
							isLook = false;
						}else if(buddyuserInfo.getPrivateType() == 3){
							isLook = true;
						}
						if(!isLook){
							taskInfo.setTitle("已有安排");
						}
						taskInfoList.add(taskInfo);
					}
				}
				code = "0";
				msg = "";
				responseWriter(response,"tasks",taskInfoList);
			}else{
				code = "8";
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},msg:{}",userId,buddyUserId);
	}
	
	/**
	 * 32用户意见反馈
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/suggest", method = RequestMethod.POST)
	public void suggest(HttpServletRequest request,HttpServletResponse response) { 
		String code = "8", msg = "";//错误默认值
		String userId = request.getParameter("userId");
		String content = request.getParameter("content");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(content)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				Suggest suggest = new Suggest();
				suggest.setContent(content);
				commonService.create(suggest);
				code = "0";
				msg = "login success";
				responseWriter(response);
			}else {
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
	/**
	 * 27 修改头像
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "updateHeadPortraitImg", method = RequestMethod.POST)
	public void updateHeadPortraitImg(HttpServletRequest request,HttpServletResponse response) { 
		String code = "8", msg = "";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo!=null){
				logger.info("Configuration.ROOT:"+Configuration.ROOT);
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("head");
		        String uuIDs = Identities.uuid2();
		        String newHeadPortraitImg = uuIDs+".jpg";
		        if(!FileUtil.exists(V1Constants.headImgPath)){
		        	FileUtil.mkdirs(V1Constants.headImgPath);
		        }
		        String saveFileName = V1Constants.headImgPath+newHeadPortraitImg;
		        FileUtil.writeFile(saveFileName, file.getBytes());
				logger.info("headPortraitImgPath:"+saveFileName);
				String oldHeadPortraitImg = StringUtil.concat(V1Constants.headImgPath,userInfo.getHeadURL());
				FileUtil.delete(oldHeadPortraitImg);
				userInfo.setHeadURL(newHeadPortraitImg);
				commonService.update(userInfo);
				code = "0";
				msg = "img upload success";
				logger.info( "img upload success:{}",userInfo.getHttpHeadURL());
				responseWriter(response, "httpHeadURL", userInfo.getHttpHeadURL());
			}else {
				throw new IOException();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
}
