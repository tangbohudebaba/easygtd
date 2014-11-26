package com.nationsky.backstage.business.v1.web.action.front;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.TimestampMorpher;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "v1/task/")
public class TaskAction extends BusinessBaseAction {
	static final Logger logger = LoggerFactory.getLogger(TaskAction.class);
	
	//获取任务列表
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	public void getList(HttpServletRequest request,HttpServletResponse response) {
		String code = "7", msg = "获取任务列表失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			DateUtil.getDate(new Date());
			Factor[] factorOrs = new Factor[]{Factor.create("endTime", C.Lt, System.currentTimeMillis()),Factor.create("beginTime", C.Ge, DateUtil.getDate(new Date()).getTime())};
			List<TaskInfo> taskInfoList = commonService.findList(TaskInfo.class, 0, Integer.MAX_VALUE, "beginTime:desc", Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create(null, C.Or, factorOrs),Factor.create("isDone", C.Ne, 0));
			code = "0";
			msg = "";
			responseWriter(response, "tasks", taskInfoList);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
	//做完任务
	@RequestMapping(value = "/done", method = RequestMethod.POST)
	public void done(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交做完任务失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			taskInfo.setIsDone(0);
			commonService.update(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//星标任务
	@RequestMapping(value = "/setTarget", method = RequestMethod.POST)
	public void setTarget(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		String isFlag = request.getParameter("isFlag");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)||ValidateUtil.isNull(isFlag)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			taskInfo.setIsFlag(Integer.parseInt(isFlag));;
			commonService.update(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//删除任务
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			commonService.remove(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	
	//获取任务详情
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public void getInfo(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			code = "0";
			msg = "";
			responseWriter(response, "taskInfo", taskInfo);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//修改任务
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		String taskInfoJsonStr = request.getParameter("taskInfo");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)||ValidateUtil.isNull(taskInfoJsonStr)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			TaskInfo newTaskInfo = new TaskInfo();
			String[] formats={"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
			newTaskInfo = (TaskInfo)JSONObject.toBean(JSONObject.fromObject(taskInfoJsonStr), TaskInfo.class);
			taskInfo.setBeginTime(newTaskInfo.getBeginTime());
			taskInfo.setEndTime(newTaskInfo.getEndTime());
			taskInfo.setIsDone(newTaskInfo.getIsDone());
			taskInfo.setIsFlag(newTaskInfo.getIsFlag());
			taskInfo.setIsHasMembers(newTaskInfo.getIsHasMembers());
			taskInfo.setLocation(newTaskInfo.getLocation());
			taskInfo.setMemberUserIds(newTaskInfo.getMemberUserIds());
			taskInfo.setRemark(newTaskInfo.getRemark());
			taskInfo.setReminderTime(newTaskInfo.getReminderTime());
			taskInfo.setTitle(newTaskInfo.getTitle());
			commonService.update(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//创建任务
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void create(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskInfoJsonStr = request.getParameter("taskInfo");
		TaskInfo taskInfo = null;
		int taskId = 0;
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskInfoJsonStr)){
				throw new Exception();
			}
			String[] formats={"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
			taskInfo= (TaskInfo)JSONObject.toBean(JSONObject.fromObject(taskInfoJsonStr), TaskInfo.class);
			code = "0";
			msg = "";
			commonService.create(taskInfo);
			taskId = taskInfo.getId();
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//推出任务
	@RequestMapping(value = "/exit", method = RequestMethod.POST)
	public void exit(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			String memberUserIds = taskInfo.getMemberUserIds();
			memberUserIds = memberUserIds.replace(userId, "").replace(",,", ",");
			if(memberUserIds.startsWith(",")){
				memberUserIds = memberUserIds.substring(1);
			}
			if(memberUserIds.endsWith(",")){
				memberUserIds = memberUserIds.substring(0, memberUserIds.length()-1);
			}
			taskInfo.setMemberUserIds(memberUserIds);
			commonService.update(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	
	//拒绝任务(没写呢)
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public void reject(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			String memberUserIds = taskInfo.getMemberUserIds();
			memberUserIds = memberUserIds.replace(userId, "").replace(",,", ",");
			if(memberUserIds.startsWith(",")){
				memberUserIds = memberUserIds.substring(1);
			}
			if(memberUserIds.endsWith(",")){
				memberUserIds = memberUserIds.substring(0, memberUserIds.length()-1);
			}
			taskInfo.setMemberUserIds(memberUserIds);
			commonService.update(taskInfo);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	
	//同意任务(没写)
	@RequestMapping(value = "/agree", method = RequestMethod.POST)
	public void agree(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Or, Integer.parseInt(taskId)));
			if(taskInfo == null){
				throw new Exception();
			}
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
}
