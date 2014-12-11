package com.nationsky.backstage.business.common.job;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.TaskInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;


/**
 * 
 * @title : 商户类型索引类型任务调度类
 * @description : 定时同步商户类型索引类型
 * @projectname : commcan_search
 * @classname : CommunityCentersJob
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年2月13日 上午11:15:28
 */
public class DelayTaskNotifyJob implements Job {
	static final Logger logger = LoggerFactory.getLogger(DelayTaskNotifyJob.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("--send delay task notify start--");
		//发送延期任务通知
		String hqltmp = "select new list(t.id, tu.userId) from TaskInfo as t, TaskInfoAndUserInfo tu where t.id = tu.taskId and tu.isAgree = 1 and tu.isDone = 0 and tu.delayNotify = 0 and t.isDelete = 0 and t.endTime < %s";
		String hql = String.format(hqltmp, System.currentTimeMillis()/1000);
		List<List<Integer>> taskIdAnduserIdList = (List<List<Integer>>)BusinessCommonService.commonService.findList(hql, 0, Integer.MAX_VALUE);
		for (List<Integer> taskIdAnduserId : taskIdAnduserIdList) {
			Integer taskId = taskIdAnduserId.get(0);
			Integer userId = taskIdAnduserId.get(1);
			//生成通知
			if(taskId != null && userId != null){
				Notify notify = new Notify();
				notify.setFromUserId(null);//来源人员姓名
				notify.setTaskId(taskId);
				notify.setType(6);
				notify.setUserId(userId);//被通知用户ID
				BusinessCommonService.commonService.create(notify);
			}
		}
		logger.info("send delay task notify count:"+taskIdAnduserIdList.size());
		logger.info("--send delay task notify finashed--");
	}
	
}
