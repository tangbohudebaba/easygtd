package com.nationsky.backstage.business.v1.Handler;

import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfoAndUserInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.TimestampMorpher;
import com.nationsky.backstage.util.ValidateUtil;

public class TaskInfoHandler {

	public static TaskInfo getUserTaskInfo(int userId, int taskId){
		try {
			TaskInfo taskInfo = BusinessCommonService.commonService.getUnique(TaskInfo.class,Factor.create("id", C.Eq, taskId));
			if(taskInfo == null){
				throw new Exception();
			}
			TaskInfoAndUserInfo taskInfoAndUserInfo = BusinessCommonService.commonService.getUnique(TaskInfoAndUserInfo.class, Factor.create("userId", C.Eq, userId),  Factor.create("taskId", C.Eq, taskId), Factor.create("isAgree", C.Eq, 1));
			taskInfo.setIsDone(taskInfoAndUserInfo.getIsDone());
			taskInfo.setIsFlag(taskInfoAndUserInfo.getIsFlag());
			List<UserInfo> userInfoList = null;
			if(taskInfo.getIsHasMembers()==1){
				List<TaskInfoAndUserInfo> taskInfoAndUserInfoList1 = BusinessCommonService.commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, taskInfo.getId()), Factor.create("isAgree", C.Eq, 1));
				Integer[] memberUserIds= new Integer[taskInfoAndUserInfoList1.size()];
				for (int i = 0; i < taskInfoAndUserInfoList1.size(); i++) {
					TaskInfoAndUserInfo taskInfoAndUserInfo1 = taskInfoAndUserInfoList1.get(i);
					memberUserIds[i] = taskInfoAndUserInfo1.getUserId();
				}
				userInfoList = BusinessCommonService.commonService.findList(UserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("id", C.In, memberUserIds));
				if(ValidateUtil.isNullCollection(userInfoList)){
					taskInfo.setMemberUserIds("[]");
				}else{
					taskInfo.setMemberUserIds(DateJsonValueProcessorUtil.ObjectToJson(userInfoList));
				}
			}else{
				taskInfo.setMemberUserIds("[]");
			}
			return taskInfo;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int createTaskInfo(String userId, String taskInfoJsonStr){
		TaskInfo taskInfo = null;
		int taskId = -1;
		try {
			if(ValidateUtil.isNull(userId)||ValidateUtil.isNull(taskInfoJsonStr)){
				throw new Exception();
			}
			String[] formats={"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
			taskInfo= (TaskInfo)JSONObject.toBean(JSONObject.fromObject(taskInfoJsonStr), TaskInfo.class);
			
			if(ValidateUtil.isNotNull(taskInfo.getMemberUserIds())){
				taskInfo.setIsHasMembers(1);
			}
			taskInfo.setUserId(Integer.parseInt(userId));
			taskInfo.setCreaterUserId(Integer.parseInt(userId));
			BusinessCommonService.commonService.create(taskInfo);
			taskId = taskInfo.getId();
			TaskInfoAndUserInfo taskInfoAndUserInfo = new TaskInfoAndUserInfo();
			taskInfoAndUserInfo.setTaskId(taskId);
			taskInfoAndUserInfo.setIsAgree(1);
			taskInfoAndUserInfo.setIsFlag(taskInfo.getIsFlag());
			taskInfoAndUserInfo.setUserId(taskInfo.getUserId());
			BusinessCommonService.commonService.create(taskInfoAndUserInfo);
			
			if(taskInfo.getIsHasMembers() == 1){
				String[] memberUserIdStrArray = taskInfo.getMemberUserIds().split(",");
				for (int i = 0; i < memberUserIdStrArray.length; i++) {
					taskInfoAndUserInfo = new TaskInfoAndUserInfo();
					taskInfoAndUserInfo.setTaskId(taskId);
					taskInfoAndUserInfo.setUserId(Integer.parseInt(memberUserIdStrArray[i]));
					BusinessCommonService.commonService.create(taskInfoAndUserInfo);
					//生成通知
					Notify notify = new Notify();
					notify.setFromUserId(Integer.parseInt(userId));
//					notify.setFromUserName(fromUserName);
					notify.setTaskId(taskId);
					notify.setType(1);
					notify.setUserId(Integer.parseInt(memberUserIdStrArray[i]));
					BusinessCommonService.commonService.create(notify);
				}
			}
			return taskId;
		} catch (Exception e) {
			return taskId;
		}
	}
	
	public static List<TaskInfoAndUserInfo> getTaskUserId(int taskId){
		return BusinessCommonService.commonService.findList(TaskInfoAndUserInfo.class, 0, Integer.MAX_VALUE, null, Factor.create("taskId", C.Eq, taskId));
	}
}
