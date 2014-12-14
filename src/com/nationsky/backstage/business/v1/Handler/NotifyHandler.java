package com.nationsky.backstage.business.v1.Handler;

import com.nationsky.backstage.business.common.BaiduIosPush;
import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.V1Constants;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.ValidateUtil;

public class NotifyHandler {
	
	/**
	 * 生成通知
	 * @param userId 被通知用户ID
	 * @param fromUserId 来源人员Id
	 * @param taskId 任务Id
	 * @param type 通知类型: 1收到任务邀请类型, 2任务被拒绝类型, 3任务被同意类型, 4任务被删除类型, 5任务已完成类型, 6任务延期类型(还没写), 7好友添加通知, 8任务修改类型(还没写), 9退出任务类型, 10同意好友添加通知, 11拒绝好友添加通知
	 * @return
	 */
	public static int createNotify(int userId, int fromUserId,int taskId, int type){
		int notifyId = -1;
		try{
			//生成通知
			Notify notify = new Notify();
			notify.setFromUserId(fromUserId);
			notify.setTaskId(taskId);
			notify.setType(type);
			notify.setUserId(userId);
			BusinessCommonService.commonService.create(notify);
				
			UserInfo userInfo = BusinessCommonService.commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, userId));
			TaskInfo taskInfo = new TaskInfo();
			if(taskId != -1){
				taskInfo = BusinessCommonService.commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, taskId));
			}
			UserInfo fromUserInfo = new UserInfo();
			if(fromUserId != -1){
				fromUserInfo = BusinessCommonService.commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, fromUserId));
			}
			String pushTokens = userInfo.getPushToken();
			if(ValidateUtil.isNotNull(pushTokens)){
				for (String pushToken : pushTokens.split(" ")) {
					String baiduUserId = pushToken.split(",")[0];
					Long baiduChannelId = Long.parseLong(pushToken.split(",")[1]);
					String content = "";
					if(type == 1){
						content = fromUserInfo.getName()+"任务邀请您加入:"+taskInfo.getTitle();
					}else if(type == 2){
						content =  fromUserInfo.getName()+"拒绝任务:"+taskInfo.getTitle();
					}else if(type == 3){
						content =  fromUserInfo.getName()+"同意任务:"+taskInfo.getTitle();
					}else if(type == 4){
						content =  fromUserInfo.getName()+"删除任务:"+taskInfo.getTitle();
					}else if(type == 5){
						content =  fromUserInfo.getName()+"完成任务:"+taskInfo.getTitle();
					}else if(type == 6){
						content =  "您有任务延期:"+taskInfo.getTitle();
					}else if(type == 7){
						content =  fromUserInfo.getName()+",邀请你加为好友";
					}else if(type == 8){
						content =  taskInfo.getTitle()+",任务已修改";
					}else if(type == 9){
						content =  fromUserInfo.getName()+"退出任务:"+taskInfo.getTitle();
					}else if(type == 10){
						content =  fromUserInfo.getName()+"同意添加您为好友";
					}else if(type == 11){
						content =  fromUserInfo.getName()+"拒绝添加您为好友";
					}
					BaiduIosPush.IosPush(V1Constants.baiduPushDeployStatus, baiduUserId, baiduChannelId, content);
				}
			}
			
			return notify.getId();
		} catch (Exception e) {
			return notifyId;
		}
	}
	
}
