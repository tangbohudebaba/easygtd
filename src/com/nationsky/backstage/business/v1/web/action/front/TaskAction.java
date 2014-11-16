package com.nationsky.backstage.business.v1.web.action.front;

import java.util.Date;
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
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.DateUtil;
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
	
	
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	public void getInfo(HttpServletRequest request,HttpServletResponse response) {
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
			code = "0";
			msg = "";
			responseWriter(response, "taskInfo", taskInfo);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		String taskInfoJsonStr = request.getParameter("taskInfo");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("id", C.Or, Integer.parseInt(taskId)));
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
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void create(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskInfoJsonStr = request.getParameter("taskInfo");
		TaskInfo taskInfo = null;
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskInfoJsonStr)){
				throw new Exception();
			}
			taskInfo = (TaskInfo)JSONObject.toBean(JSONObject.fromObject(taskInfoJsonStr), TaskInfo.class);
			code = "0";
			msg = "";
			commonService.create(taskInfo);
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskInfo.getId(),userId,code,msg);
	}
	
}
