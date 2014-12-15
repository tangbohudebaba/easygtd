package com.nationsky.backstage.business.v1.web.action.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.Handler.NotifyHandler;
import com.nationsky.backstage.business.v1.Handler.TaskInfoHandler;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfoAndUserInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
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
			List<TaskInfo> taskInfoList1 = new ArrayList<TaskInfo>();
			String hqltmp = "select distinct t from TaskInfo as t, TaskInfoAndUserInfo tu where ((t.id = tu.taskId and tu.isAgree = 1 and tu.isDone = 0 and t.isDelete = 0 and t.endTime < %s ) or (t.id = tu.taskId and tu.isAgree = 1 and t.isDelete = 0  and t.beginTime > %s ) or (t.id = tu.taskId and tu.isAgree = 1 and t.isDelete = 0  and t.endTime is null and t.beginTime is null )) and tu.userId = %s";
			String hql = String.format(hqltmp, System.currentTimeMillis()/1000, DateUtil.getDate(new Date()).getTime()/1000, userId);
			List<TaskInfo> taskInfoList = (List<TaskInfo>)commonService.findList(hql, 0, Integer.MAX_VALUE);
			for (TaskInfo taskInfo : taskInfoList) {
				TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),  Factor.create("taskId", C.Eq, taskInfo.getId()), Factor.create("isAgree", C.Eq, 1));
				taskInfo.setUserId(Integer.parseInt(userId));
				taskInfo.setIsDone(taskInfoAndUserInfo.getIsDone());
				taskInfo.setIsFlag(taskInfoAndUserInfo.getIsFlag());
				if(taskInfo.getIsHasMembers()==1){
					StringBuffer memberUserIds = new StringBuffer();
					List<TaskInfoAndUserInfo> taskInfoAndUserInfoList1 = commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, taskInfo.getId()), Factor.create("isAgree", C.Eq, 1));
					for (int i = 0; i < taskInfoAndUserInfoList1.size(); i++) {
						TaskInfoAndUserInfo taskInfoAndUserInfo1 = taskInfoAndUserInfoList1.get(i);
						if(i==taskInfoAndUserInfoList1.size()-1){
							memberUserIds.append(taskInfoAndUserInfo1.getUserId());
						}else{
							memberUserIds.append(taskInfoAndUserInfo1.getUserId()+",");
						}
					}
					taskInfo.setMemberUserIds(memberUserIds.toString());
				}
				taskInfoList1.add(taskInfo);
			}
			code = "0";
			msg = "";
			responseWriter(response, "tasks", taskInfoList);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
	//完成做完任务
	@RequestMapping(value = "/done", method = RequestMethod.POST)
	public void done(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交做完任务失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("taskId", C.Eq, Integer.parseInt(taskId)));
			if(taskInfoAndUserInfo == null){
				throw new Exception();
			}
			taskInfoAndUserInfo.setIsDone(1);
			commonService.update(taskInfoAndUserInfo);
			//生成通知
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			if(Integer.parseInt(userId) != taskInfo.getCreaterUserId()){
				NotifyHandler.createNotify(taskInfo.getCreaterUserId(), Integer.parseInt(userId), Integer.parseInt(taskId), 5);
//				Notify notify = new Notify();
//				notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//				notify.setTaskId(Integer.parseInt(taskId));
//				notify.setType(5);
//				notify.setUserId(taskInfo.getCreaterUserId());//被通知用户ID
//				commonService.create(notify);
				
			}
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
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),Factor.create("taskId", C.Eq, Integer.parseInt(taskId)));
			if(taskInfoAndUserInfo == null){
				throw new Exception();
			}
			taskInfoAndUserInfo.setIsFlag(Integer.parseInt(isFlag));;
			commonService.update(taskInfoAndUserInfo);
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
			List<TaskInfoAndUserInfo> taskInfoAndUserInfoList = commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, Integer.parseInt(taskId)));
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class,Factor.create("id", C.Eq, Integer.parseInt(taskId)),Factor.create("createrUserId", C.Eq, Integer.parseInt(userId)));
			if(taskInfoAndUserInfoList == null || taskInfo == null){
				throw new Exception();
			}
			taskInfo.setIsDelete(1);
			commonService.update(taskInfo);
			for (TaskInfoAndUserInfo taskInfoAndUserInfo : taskInfoAndUserInfoList) {
				commonService.remove(taskInfoAndUserInfo);
				//生成通知
				NotifyHandler.createNotify(taskInfoAndUserInfo.getUserId(), Integer.parseInt(userId), Integer.parseInt(taskId), 4);
//				Notify notify = new Notify();
//				notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//				notify.setTaskId(Integer.parseInt(taskId));
//				notify.setType(4);
//				notify.setUserId(taskInfoAndUserInfo.getUserId());//被通知用户ID
//				commonService.create(notify);
			}
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
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),  Factor.create("taskId", C.Eq, Integer.parseInt(taskId)));
			if(taskInfoAndUserInfo != null){
				taskInfo.setIsDone(taskInfoAndUserInfo.getIsDone());
				taskInfo.setIsFlag(taskInfoAndUserInfo.getIsFlag());
			}
			List<UserInfo> userInfoList = null;
			if(taskInfo.getIsHasMembers()==1){
				List<TaskInfoAndUserInfo> taskInfoAndUserInfoList1 = commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, taskInfo.getId()), Factor.create("isAgree", C.Eq, 1));
				Integer[] memberUserIds= new Integer[taskInfoAndUserInfoList1.size()];
				for (int i = 0; i < taskInfoAndUserInfoList1.size(); i++) {
					TaskInfoAndUserInfo taskInfoAndUserInfo1 = taskInfoAndUserInfoList1.get(i);
					memberUserIds[i] = taskInfoAndUserInfo1.getUserId();
				}
				userInfoList = commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("id", C.In, memberUserIds));
				if(ValidateUtil.isNullCollection(userInfoList)){
					taskInfo.setMemberUserIds("[]");
				}else{
					taskInfo.setMemberUserIds(DateJsonValueProcessorUtil.ObjectToJson(userInfoList));
				}
			}else{
				taskInfo.setMemberUserIds("[]");
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
			UserInfo userInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, Integer.parseInt(userId)));
			if(userInfo == null){
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
			if(ValidateUtil.isNotNull(taskInfo.getMemberUserIds())){
				taskInfo.setIsHasMembers(1);
			}else{
				taskInfo.setIsHasMembers(0);
			}
			taskInfo.setLocation(newTaskInfo.getLocation());
			taskInfo.setMemberUserIds(newTaskInfo.getMemberUserIds());
			taskInfo.setRemark(newTaskInfo.getRemark());
			taskInfo.setTitle(newTaskInfo.getTitle());
			commonService.update(taskInfo);
			
			if(ValidateUtil.isNotNull(taskInfo.getMemberUserIds())){
				//任务修改之前的成员
				List<Integer> beforeUserIdList = new ArrayList<Integer>(); 
				
				List<TaskInfoAndUserInfo> taskInfoAndUserInfoList = commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, Integer.parseInt(taskId)));
				for (TaskInfoAndUserInfo taskInfoAndUserInfo : taskInfoAndUserInfoList) {
					Integer oldUserId = taskInfoAndUserInfo.getUserId();
					beforeUserIdList.add(oldUserId);
				}
				
				List<String> afterUserIdStrList = Arrays.asList(taskInfo.getMemberUserIds().split(","));
				//任务修改之后的成员
				List<Integer> afterUserIdList = new ArrayList<Integer>(); 
				for (String userIdStr : afterUserIdStrList) {
					afterUserIdList.add(Integer.parseInt(userIdStr));
				}
				
				//不变的成员
				List<Integer> nochangeUserIdList = new ArrayList<Integer>(); 
				//删除的成员
				List<Integer> deletedUserIdList = new ArrayList<Integer>();
				//添加的成员
				List<Integer> newUserIdList = new ArrayList<Integer>();
				
				for (Integer afterUserId : afterUserIdList) {
					if(beforeUserIdList.contains(afterUserId)){
						nochangeUserIdList.add(afterUserId);
					}else{
						newUserIdList.add(afterUserId);
					}
				}
				
				for (Integer beforeUserId : beforeUserIdList) {
					if(!afterUserIdList.contains(beforeUserId)){
						deletedUserIdList.add(beforeUserId);
					}
				}
				
				for (Integer nochangeUserId : nochangeUserIdList) {
					NotifyHandler.createNotify(nochangeUserId, nochangeUserId, Integer.parseInt(taskId), 8);
				}
				
				for (Integer newUserId : newUserIdList) {
					TaskInfoAndUserInfo taskInfoAndUserInfo = new TaskInfoAndUserInfo();
					taskInfoAndUserInfo.setTaskId(Integer.parseInt(taskId));
					taskInfoAndUserInfo.setUserId(newUserId);
					BusinessCommonService.commonService.create(taskInfoAndUserInfo);
					//生成通知
					NotifyHandler.createNotify(newUserId, Integer.parseInt(userId), Integer.parseInt(taskId), 1);
				}
				
				for (Integer deletedUserId : deletedUserIdList) {
					List<TaskInfoAndUserInfo> taskInfoAndUserInfoList1 = commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, Integer.parseInt(taskId)), Factor.create("userId", C.Eq, deletedUserId));
					for (TaskInfoAndUserInfo taskInfoAndUserInfo : taskInfoAndUserInfoList1) {
						commonService.remove(taskInfoAndUserInfo);
						NotifyHandler.createNotify(deletedUserId, Integer.parseInt(userId), Integer.parseInt(taskId), 4);
					}
				}
			}
			
			code = "0";
			msg = "";
			responseWriter(response,"taskId",taskInfo.getId());
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//创建任务
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public void create(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskInfoJsonStr = request.getParameter("taskInfo");
		
		int taskId = -1;
		try {
			taskId = TaskInfoHandler.createTaskInfo(userId, taskInfoJsonStr);
			if(taskId != -1){
				code = "0";
				msg = "";
				responseWriter(response,"taskId",taskId);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	//31批量创建任务
	@RequestMapping(value = "batchCreate", method = RequestMethod.POST)
	public void batchCreate(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskInfosJsonStr = request.getParameter("taskInfos");//任务json数组
		
		StringBuffer sb = new StringBuffer();
		try {
			List<String> taskInfoJsonStrList = DateJsonValueProcessorUtil.jsonStrArrayToJsonItemStrList(taskInfosJsonStr);
			for (String taskInfoJsonStr : taskInfoJsonStrList) {
				int taskId = TaskInfoHandler.createTaskInfo(userId, taskInfoJsonStr);
				sb.append(taskId+" ");
			}
			code = "0";
			msg = "";
			responseWriter(response,"taskIds",sb.toString().trim());
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskIds:{} , userId:{},code:{},msg:{}",sb.toString(),userId,code,msg);
	}
	
	//退出任务
	@RequestMapping(value = "/exit", method = RequestMethod.POST)
	public void exit(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),  Factor.create("taskId", C.Eq, Integer.parseInt(taskId)), Factor.create("isAgree", C.Eq, 1));
			if(taskInfoAndUserInfo == null){
				throw new Exception();
			}
			commonService.remove(taskInfoAndUserInfo);
			//生成通知
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			NotifyHandler.createNotify(taskInfo.getCreaterUserId(), Integer.parseInt(userId), Integer.parseInt(taskId), 9);
//			Notify notify = new Notify();
//			notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//			notify.setTaskId(Integer.parseInt(taskId));
//			notify.setType(9);
////			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
//			notify.setUserId(taskInfo.getCreaterUserId());//被通知用户ID
//			commonService.create(notify);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	
	//拒绝任务
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public void reject(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),  Factor.create("taskId", C.Eq, Integer.parseInt(taskId)), Factor.create("isAgree", C.Eq, 0));
			if(taskInfoAndUserInfo == null){
				throw new Exception();
			}
			commonService.remove(taskInfoAndUserInfo);
			//生成通知
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			NotifyHandler.createNotify(taskInfo.getCreaterUserId(), Integer.parseInt(userId), Integer.parseInt(taskId), 2);
//			Notify notify = new Notify();
//			notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//			notify.setTaskId(Integer.parseInt(taskId));
//			notify.setType(2);
//			notify.setUserId(taskInfo.getCreaterUserId());//被通知用户ID
//			commonService.create(notify);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
	
	
	//同意任务
	@RequestMapping(value = "/agree", method = RequestMethod.POST)
	public void agree(HttpServletRequest request,HttpServletResponse response) {
		String code = "8", msg = "提交失败";//错误默认值
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskId)){
				throw new Exception();
			}
			TaskInfoAndUserInfo taskInfoAndUserInfo = commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, Integer.parseInt(userId)),  Factor.create("taskId", C.Eq, Integer.parseInt(taskId)), Factor.create("isAgree", C.Eq, 0));
			if(taskInfoAndUserInfo == null){
				throw new Exception();
			}
			taskInfoAndUserInfo.setIsAgree(1);
			commonService.update(taskInfoAndUserInfo);
			//生成通知
			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
			NotifyHandler.createNotify(taskInfo.getCreaterUserId(), Integer.parseInt(userId), Integer.parseInt(taskId), 3);
			
//			Notify notify = new Notify();
//			notify.setFromUserId(Integer.parseInt(userId));//来源人员姓名
//			notify.setTaskId(Integer.parseInt(taskId));
//			notify.setType(3);
//			TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, Integer.parseInt(taskId)));
//			notify.setUserId(taskInfo.getCreaterUserId());//被通知用户ID
//			commonService.create(notify);
			code = "0";
			msg = "";
			responseWriter(response);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("taskId:{} , userId:{},code:{},msg:{}",taskId,userId,code,msg);
	}
}
