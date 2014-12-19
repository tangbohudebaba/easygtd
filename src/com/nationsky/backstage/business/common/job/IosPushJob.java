package com.nationsky.backstage.business.common.job;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.common.IosPushApns;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
import com.nationsky.backstage.business.v1.bsc.dao.po.IosPush;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;


/**
 * 
 * @title : Ios推送任务调度类
 * @description : 
 * @projectname : commcan_search
 * @classname : IosPushJob
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年2月13日 上午11:15:28
 */
public class IosPushJob implements Job {
	static final Logger logger = LoggerFactory.getLogger(IosPushJob.class);
	static boolean pushing = false;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if(pushing){
			return;
		}
		try {
			
			pushing = true;
			logger.info("--IosPushJob start--");
			
			List<IosPush> iosPushList = BusinessCommonService.commonService.findList(IosPush.class, 0, Integer.MAX_VALUE, null);
			logger.info("IosPushJob count:"+iosPushList.size());
			for (IosPush iosPush : iosPushList) {
				UserInfo userInfo = BusinessCommonService.commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, iosPush.getUserId()));
				if(userInfo != null){
					int count = BusinessCommonService.commonService.getCount(Notify.class, Factor.create("userId", C.Eq, userInfo.getId()));
					if(count == 0){
						count = 1;
					}
					boolean pushSuccess = IosPushApns.sendpush(userInfo.getPushToken(), iosPush.getContent(), count);
					if(pushSuccess){
						BusinessCommonService.commonService.remove(iosPush);
					}
				}else{
					BusinessCommonService.commonService.remove(iosPush);
				}
			}
			logger.info("--IosPushJob finashed--");
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			pushing = false;
		}
		
	}
	
}
