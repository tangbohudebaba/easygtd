package com.nationsky.backstage.business.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.nationsky.backstage.Configuration;

/** 
 * @title : 
 * @description : 
 * @projectname : easygtd
 * @classname : BaiduIosPush
 * @version 1.0
 * @company : nationsky
 * @email : liuchang_liu_chang@163.com
 * @author : liuchang
 * @createtime : 2014年12月14日 下午5:44:37 
 */
public class BaiduIosPush {
	static final Logger logger = LoggerFactory.getLogger(BaiduIosPush.class);
	static final String apiKey = Configuration.get("push.baiduApiKey");
	static final String secretKey = Configuration.get("push.baiduSecretKey");
	
	/**
	 * 百度推送,ios单条消息推送
	 * @param deployStatus 部署状态,1: Developer 2:Production
	 * @param userId 百度userId
	 * @param channelId 百度频道Id
	 * @param content 推送内容
	 * @return
	 */
	public static boolean IosPush(int deployStatus, String userId, long channelId, String content){
        /*
         * @brief 推送单播通知(IOS APNS) message_type = 1 (默认为0)
         */
        // 1. 设置developer平台的ApiKey/SecretKey
        
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
            	logger.info(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(4); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp
            request.setDeployStatus(deployStatus); // DeployStatus => 1: Developer 2:
                                        // Production
            request.setChannelId(channelId);
            request.setUserId(userId);

            request.setMessageType(1);
            request.setMessage("{\"aps\":{\"alert\":\""+content+"\"}}");

            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient
                    .pushUnicastMessage(request);

            // 6. 认证推送成功
            logger.info("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
            return false;
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
        	logger.error(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            return false;
        }
        return true;
        
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean a = IosPush(1, "666283820700770889", 5102311317247673482L, "推送测试啦啦啦");

	}

}
