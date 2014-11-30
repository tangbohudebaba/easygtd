package com.nationsky.backstage.business.common.Handler;

import java.util.List;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfoAndUserInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.ValidateUtil;

public class TaskInfoHandler {

	public static List<TaskInfo> getTaskInfoList(){
		return null;
	}
	
	
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
					taskInfo.setMemberUserIds(DateJsonValueProcessorUtil.ObjectToJson(userInfoList));
				}else{
					taskInfo.setMemberUserIds("[]");
				}
			}else{
				taskInfo.setMemberUserIds("[]");
			}
			return taskInfo;
		} catch (Exception e) {
			return null;
		}
	}
}
