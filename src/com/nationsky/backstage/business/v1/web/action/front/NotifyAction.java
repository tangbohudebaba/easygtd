package com.nationsky.backstage.business.v1.web.action.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nationsky.backstage.business.common.BusinessBaseAction;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "v1/notifications/")
public class NotifyAction extends BusinessBaseAction {
	static final Logger logger = LoggerFactory.getLogger(NotifyAction.class);
	static final List<Integer> tastNotifyTypeList = new ArrayList<Integer>();
	static final List<Integer> userNotifyTypeList = new ArrayList<Integer>();
	static{
		tastNotifyTypeList.add(1);
		tastNotifyTypeList.add(2);
		tastNotifyTypeList.add(3);
		tastNotifyTypeList.add(4);
		tastNotifyTypeList.add(5);
		tastNotifyTypeList.add(6);
		tastNotifyTypeList.add(8);
		tastNotifyTypeList.add(9);
		userNotifyTypeList.add(7);
		userNotifyTypeList.add(10);
		userNotifyTypeList.add(11);
		
	}
	//通知类型 
	//1收到任务邀请类型, 2任务被拒绝类型, 3任务被同意类型, 4任务被删除类型, 5任务已完成类型, 6任务延期类型(还没写), 7好友添加通知, 8任务修改类型(还没写), 9退出任务类型, 10同意好友添加通知, 11拒绝好友添加通知
	
	//获取通知列表
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	public void getList(HttpServletRequest request,HttpServletResponse response) {
		String code = "7", msg = "获取任务列表失败";//错误默认值
		String userId = request.getParameter("userId");
		try {
			if(ValidateUtil.isNull(userId)){
				throw new Exception();
			}
			DateUtil.getDate(new Date());
			List<Notify> notifyList = commonService.findList(Notify.class, 0, Integer.MAX_VALUE, "updatedAt:desc", Factor.create("userId", C.Eq, Integer.parseInt(userId)), Factor.create("type", C.In, new Integer[]{1,2,3,4,5,6,8,9}));
			for (int i = 0; i < notifyList.size(); i++) {
					TaskInfo taskInfo = commonService.getUnique(TaskInfo.class, Factor.create("id", C.Eq, notifyList.get(i).getTaskId()));
					UserInfo fromuserInfo = commonService.getUnique(UserInfo.class,Factor.create("id", C.Eq, notifyList.get(i).getFromUserId()));
					if(taskInfo != null){
						notifyList.get(i).setBeginTime(taskInfo.getBeginTime());
						notifyList.get(i).setEndTime(taskInfo.getEndTime());
						notifyList.get(i).setTitle(taskInfo.getTitle());
					}
					if(fromuserInfo != null){
						notifyList.get(i).setFromUserName(fromuserInfo.getName());
					}
			}
			code = "0";
			msg = "";
			responseWriter(response, "notifications", notifyList);
		} catch (Exception e) {
			responseWriter(code, msg, response);
		}
		logger.info("userId:{},code:{},msg:{}",userId,code,msg);
	}
	
}
