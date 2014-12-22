package com.nationsky.backstage.business.common.job;
import java.net.UnknownHostException;
import java.util.List;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.PushedNotification;

import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.business.common.BusinessCommonService;
import com.nationsky.backstage.business.common.IosPushApns;
import com.nationsky.backstage.business.v1.bsc.dao.po.IosPush;
import com.nationsky.backstage.business.v1.bsc.dao.po.Notify;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.util.ValidateUtil;


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
				try {
					UserInfo userInfo = BusinessCommonService.commonService.getUnique(UserInfo.class, Factor.create("id", C.Eq, iosPush.getUserId()));
					if(userInfo != null){
						String pushToken = userInfo.getPushToken();
						if(ValidateUtil.isNull(pushToken)){
							BusinessCommonService.commonService.remove(iosPush);
							break;
						}else{
							String[] pushTokenArray = pushToken.split(" ");
							for (String pushToken2 : pushTokenArray) {
								if(ValidateUtil.isNotNull(pushToken2) && pushToken2.length() != 64){
									userInfo.setPushToken(userInfo.getPushToken().replaceAll(pushToken2, "").replace("  ", " ").trim());;
								}
								
							}
						}
						BusinessCommonService.commonService.update(userInfo);
						if(ValidateUtil.isNull(userInfo.getPushToken())){
							BusinessCommonService.commonService.remove(iosPush);
							break;
						}
						int count = BusinessCommonService.commonService.getCount(Notify.class, Factor.create("userId", C.Eq, userInfo.getId()), Factor.create("status", C.Eq, 0));
						if(count == 0){
							count = 1;
						}
						boolean pushSuccess = false;
						try {
							List<PushedNotification> notifications = IosPushApns.sendpush2(userInfo.getPushToken(), iosPush.getContent(), count);
							List<PushedNotification> failedNotifications = PushedNotification
									.findFailedNotifications(notifications);

							List<PushedNotification> successfulNotifications = PushedNotification
									.findSuccessfulNotifications(notifications);

							int failed = failedNotifications.size();
							for (PushedNotification pushedNotification : failedNotifications) {
								String failtoken = pushedNotification.getDevice().getToken();
								userInfo.setPushToken(userInfo.getPushToken().replaceAll(failtoken, "").replace("  ", " ").trim());
							}
							BusinessCommonService.commonService.update(userInfo);
							int successful = successfulNotifications.size();

//							if (successful > 0 && failed == 0) {
							if (successful > 0) {
								pushSuccess = true;
								logger.info("推送成功" + successfulNotifications.toString());

							} else if (successful == 0 && failed > 0) {
								logger.error("推送失败" + failedNotifications.get(0).toString());
							} else if (successful == 0 && failed == 0) {
								logger.error("推送失败" + failedNotifications.toString());
								logger.error("No notifications could be sent, probably because of a critical error");
							} else {
								logger.error("推送失败" + failedNotifications.toString());
							}
							
							//pushSuccess = IosPushApns.sendpush(userInfo.getPushToken(), iosPush.getContent(), count);
						} catch (UnknownHostException | CommunicationException | KeystoreException e) {
							logger.error(e.toString());
							break;
						} catch (JSONException e) {
							BusinessCommonService.commonService.remove(iosPush);
							logger.error(e.toString());
						} catch (InvalidDeviceTokenFormatException e) {
//							
//							userInfo.setPushToken(userInfo.getPushToken().replaceAll(pushToken, "").replace("  ", " ").trim());
//							commonService.update(userInfo);
							// TODO: handle exception
						}
						
						if(pushSuccess){
							BusinessCommonService.commonService.remove(iosPush);
						}else{
							if(iosPush.getFailCount() != null && iosPush.getFailCount() >= 3){
								BusinessCommonService.commonService.remove(iosPush);
								logger.error("failCount >=3 removed:"+iosPush.getContent());
							}else{
								if(iosPush.getFailCount() == null){
									iosPush.setFailCount(1);
								}else{
									iosPush.setFailCount(iosPush.getFailCount()+1);
								}
								BusinessCommonService.commonService.update(iosPush);
							}
						}
					}else{
						BusinessCommonService.commonService.remove(iosPush);
					}
				
				} catch (Exception e) {
					BusinessCommonService.commonService.remove(iosPush);
					logger.error(e.toString());
				}
				}
			logger.info("--IosPushJob finashed--");
		} catch (Exception e) {
			
		}finally{
			pushing = false;
		}
		
	}
	
}
