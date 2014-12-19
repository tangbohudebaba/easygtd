package com.nationsky.backstage.business.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.util.ValidateUtil;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

/** 
 * @title : 
 * @description : 
 * @projectname : iospush
 * @classname : Server2
 * @version 1.0
 * @company : nationsky
 * @email : liuchang_liu_chang@163.com
 * @author : liuchang
 * @createtime : 2014年12月14日 下午4:54:04 
 */
public class IosPushApns {
	static final Logger logger = LoggerFactory.getLogger(IosPushApns.class);
	

	/************************************************
	 * 测试推送服务器地址：gateway.sandbox.push.apple.com /2195
	 * 产品推送服务器地址：gateway.push.apple.com / 2195
	 * 
	 * 需要javaPNS_2.2.jar包
	 ***************************************************/
	
	/**
	 * IOS群推送
	 * @param tokens 空格分开的token
	 * @param content 推送内容
	 * @param count iphone应用图标上小红圈上的数值
	 */
	public static boolean sendpush(String tokens,String content, Integer count) {
		if(ValidateUtil.isNull(tokens) || ValidateUtil.isNull(content)){
			return true;
		}
		String messageTmp = "{'aps':{'alert':'%s'}}";
		String path = Configuration.get("push.p12Patch");
		String password = Configuration.get("push.p12Password");
		List<String> tokenList = new ArrayList<String>();
		for (String token : tokens.split(" ")) {
			if(ValidateUtil.isNotNull(token) && token.length() == 64){
				tokenList.add(token);
			}
		}
		boolean production = false;
		String deployStatus = Configuration.get("push.deployStatus");
		if(ValidateUtil.isEquals("2", deployStatus)){
			production = true;
			path = Configuration.get("push.p12PatchProduction");
		}
		return sendpush(tokenList, path, password, String.format(messageTmp, content+""), count, false,production);
	}
	/**
	 * 
	 * 这是一个比较简单的推送方法，
	 * 
	 * apple的推送方法
	 * 
	 * @param tokens
	 *            iphone手机获取的token
	 * 
	 * @param path
	 *            这里是一个.p12格式的文件路径，需要去apple官网申请一个
	 * 
	 * @param password
	 *            p12的密码 此处注意导出的证书密码不能为空因为空密码会报错
	 * 
	 * @param message
	 *            推送消息的内容
	 * 
	 * @param count
	 *            应用图标上小红圈上的数值
	 * 
	 * @param sendCount
	 *            单发还是群发 true：单发 false：群发
	 */

	public static boolean sendpush(List<String> tokens, String path, String password,
			String message, Integer count, boolean sendCount,boolean production) {

		try {
			// message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}

			PushNotificationPayload payLoad = PushNotificationPayload
					.fromJSON(message);

//			payLoad.addAlert("iphone推送测试   www.guligei.com"); // 消息内容

			payLoad.addBadge(count); // iphone应用图标上小红圈上的数值

			payLoad.addSound("default"); // 铃音 默认

			PushNotificationManager pushManager = new PushNotificationManager();

			// true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
			// pushManager.initializeConnection(new
			// AppleNotificationServerBasicImpl(path, password, null,
			// "gateway.sandbox.push.apple.com", 2195));
			
			pushManager.initializeConnection(new AppleNotificationServerBasicImpl(path, password, production));

			List<PushedNotification> notifications = new ArrayList<PushedNotification>();

			// 发送push消息

			if (sendCount) {
				Device device = new BasicDevice();

				device.setToken(tokens.get(0));

				PushedNotification notification = pushManager.sendNotification(
						device, payLoad, true);

				notifications.add(notification);

			} else {
				List<Device> device = new ArrayList<Device>();

				for (String token : tokens) {

					device.add(new BasicDevice(token));

				}

				notifications = pushManager.sendNotifications(payLoad, device);

			}

			List<PushedNotification> failedNotifications = PushedNotification
					.findFailedNotifications(notifications);

			List<PushedNotification> successfulNotifications = PushedNotification
					.findSuccessfulNotifications(notifications);

			int failed = failedNotifications.size();

			int successful = successfulNotifications.size();

//			if (successful > 0 && failed == 0) {
			if (successful > 0) {
				logger.info("推送成功" + successfulNotifications.toString());
				return true;

			} else if (successful == 0 && failed > 0) {
				logger.info("推送失败" + failedNotifications.toString());
				return false;
			} else if (successful == 0 && failed == 0) {
				logger.info("推送失败" + failedNotifications.toString());
				logger.info("No notifications could be sent, probably because of a critical error");
				return false;
			} else {
				logger.info("推送失败" + failedNotifications.toString());
				return false;
			}
			// pushManager.stopConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		sendpush("12b28568566223e44ac61da8c31cfbb27b6088ac9338a76182ba5aee3a2dcbb0", "你猜3", 100);
		
		String token = "44408f2078d6a32f96b8ed4038ebb8e7f6ab16cd4283f45617174c31209afeed";
		sendpush(token, "你猜3", 100);
//		IosPushApns send = new IosPushApns();
//		List<String> tokens = new ArrayList<String>();
//		tokens.add("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157");
////		tokens.add("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157");
////		tokens.add("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157");
////		tokens.add("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157");
////		tokens.add("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157");
//		
//		String path = "Certificates.p12";
//		String password = "123456";
//		String message = "{'aps':{'alert':'%s'}}";
//		Integer count = 1;
//		boolean sendCount = true;
//		for (int i = 0; i < 2; i++) {
//			
//			sendpush(tokens, path, password, String.format(message, i+""), i+1, sendCount);
//		}
		
	//	System.out.println("6de030f4dd42a0896c683e3a27dd9d9e96becd10f053d6fef9391c2c3fc5e157".length());

		// String deviceToken =
		// "10f7 4593 df81 8db7 74b8 2fa7 bf75 551d 7659 eae0 4969 983a e2fd c3a2 1d4a e92f";//iphone手机获取的token
		// deviceToken = deviceToken.replaceAll(" ", "");
		// PayLoad payLoad = new PayLoad();
		// payLoad.addAlert("my push test 5");//push的内容
		// payLoad.addBadge(1);//图标小红圈的数值
		// payLoad.addSound("default");//铃音
		//
		// PushNotificationManager pushManager =
		// PushNotificationManager.getInstance();
		// pushManager.addDevice("iPhone", deviceToken);
		//
		// //Connect to APNs
		// /************************************************
		// 测试的服务器地址：gateway.sandbox.push.apple.com /端口2195
		// 产品推送服务器地址：gateway.push.apple.com / 2195
		// ***************************************************/
		// String host= "gateway.sandbox.push.apple.com";
		// int port = 2195;
		// String certificatePath= "/Users/aps_development_identity.p12";//导出的证书
		// String certificatePassword= "123456";//此处注意导出的证书密码不能为空因为空密码会报错
		// pushManager.initializeConnection(host,port,
		// certificatePath,certificatePassword,
		// SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
		//
		// // Send Push
		// Device client = pushManager.getDevice("iPhone");
		//
		// System.out.println("推送消息: " +
		// client.getToken()+"\n"+payLoad.toString() +" ");
		// pushManager.sendNotification(client, payLoad);
		// pushManager.stopConnection();
		// pushManager.removeDevice("iPhone");
		// System.out.println("推送消息成功!");

	}


}
