package com.nationsky.backstage.business.v1.web.action.front;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.nationsky.backstage.business.common.NotifyTypeEnum;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
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
		try {
			if(ValidateUtil.isNull(phone)||ValidateUtil.isNull(password)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("phone", C.Eq, phone));
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
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("phone:{},code:{},msg:{}",phone,code,msg);
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
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("id", C.In, buddyUserIdArray));
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
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("phone", C.Eq, keyword));
				}else{
					userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("name", C.Like, "%"+keyword+"%"));
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
	 * 19.	添加好友(待完善)
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
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
				//生成通知
				Notify notify = new Notify();
				notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
				notify.setType(7);
				notify.setUserId(Integer.parseInt(buddyUserId));//被通知用户ID
				commonService.create(notify);
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},buddyUserId:{}", code, msg, userId, buddyUserId);
	}
	
	/**
	 *20.	邀请好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "inviteBuddy", method = RequestMethod.POST)
	public void inviteBuddy(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String buddyUserId = request.getParameter("buddyUserId");
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo == null){
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},buddyUserId:{}", code, msg, userId, buddyUserId);
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
		String isAgree = request.getParameter("isAgree");//是否同意，ture为同意
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
				//生成通知
				Notify notify = new Notify();
				notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
				if(ValidateUtil.isEquals("true", isAgree)){
					notify.setType(10);
				}else{
					notify.setType(11);
				}
				notify.setUserId(Integer.parseInt(buddyUserId));//被通知用户ID
				commonService.create(notify);
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
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo != null){
				List<Notify> notifyList = commonService.findList(Notify.class, 0, Integer.MAX_VALUE, "updatedAt:desc", Factor.create("userId", C.Eq, Integer.parseInt(userId)), Factor.create("type", C.Eq, NotifyTypeEnum.getIndex(7)));
				code = "0";
				msg = "";
				responseWriter(response,"notifications",notifyList);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{}", code, msg, userId);
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
		String isAgree = request.getParameter("isAgree");//是否同意，ture为同意
		try {
			if(ValidateUtil.isNull(userId) && ValidateUtil.isNull(buddyUserId)){
				throw new Exception();
			}
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			UserInfo buddyuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(buddyUserId)));
			if(userInfo != null && buddyuserInfo != null){
				code = "0";
				msg = "";
				responseWriter(response);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("code:{},msg:{}, userId:{},msg:{}",userId,buddyUserId);
	}
	
	public static void main(String[] args) {
		
		boolean b = "18711866642".matches("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
				System.out.println(b);
	}
}
