package com.nationsky.backstage.business.common.job;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.v1.bsc.dao.po.AuthCode;
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
public class AuthCodeJob implements Job {
	static final Logger logger = LoggerFactory.getLogger(AuthCodeJob.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("--clear expiry authcode start--");
		//清理数据库中失效的验证码
		List<AuthCode> authCodeList = BusinessCommonService.commonService.findList(AuthCode.class, 0, Integer.MAX_VALUE, null, Factor.create("createdAtMillis", C.Le, System.currentTimeMillis()-20*1000*60));
		logger.info("clear expiry authcode count:"+authCodeList.size());
		for (AuthCode authCode : authCodeList) {
			BusinessCommonService.commonService.remove(authCode);
		}
		logger.info("--clear expiry authcode finashed--");
	}
	
}
