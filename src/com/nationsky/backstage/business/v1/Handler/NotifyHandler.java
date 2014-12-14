package com.nationsky.backstage.business.v1.Handler;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;

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
			return notify.getId();
		} catch (Exception e) {
			return notifyId;
		}
	}
	
}
