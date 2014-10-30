package com.nationsky.backstage.test.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.nationsky.backstage.business.common.ResponseMessage;
import com.nationsky.backstage.business.v1.bsc.dao.po.UserInfo;
import com.nationsky.backstage.util.DateJsonValueProcessorUtil;
import com.nationsky.backstage.util.HttpUtil;
import com.nationsky.backstage.util.ValidateUtil;


public class CommonTest {
	public static String urlStr = "http://127.0.0.1:8080/easygtd/v1/";
	public static void main(String[] args) {
		login();
//		json();
	}
	
	//登录
	public static void login(){
		String childUrl = "user/login.ac";
		String queryString = "phone=18611866642&password=1111";
		String result = HttpUtil.getResult(urlStr+childUrl, queryString);
		System.out.println(result);
	}
	
	public static void json(){
		UserInfo u = new UserInfo();
		u.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		u.setHeadURL("sdfsf");
		u.setId(1);
		u.setName("");
		u.setPassword("sdff");
		u.setPhone("sdfsfd");
		u.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		
		UserInfo u2 = new UserInfo();
		u2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		u2.setHeadURL("sdfsf");
		u2.setId(2);
		u2.setName("");
		u2.setPassword("sdff");
		u2.setPhone("sdfsfd");
		u2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		
		List<UserInfo> uList = new ArrayList<UserInfo>();
		uList.add(u2);
		uList.add(u);
		
		
		ResponseMessage r = new ResponseMessage();
		r.setErrormsg("登录成功");
		String s = responseJsonMessage(r, "sdf","werwr");
		System.out.println(s);
	}
	
	/**
	 * 二级json组合
	 * @param parent 父节点实体类
	 * @param accumulateKey 子节点名称
	 * @param child 子节点实体类或者实体类集合
	 * @return
	 */
	public static String responseJsonMessage(Object parent,String accumulateKey , Object child){
		JsonConfig config=new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessorUtil(DateJsonValueProcessorUtil.Default_DATE_PATTERN));
		JSONObject ResponseMessageJsonObject = JSONObject.fromObject(parent,config);
		if(ValidateUtil.isNotNull(accumulateKey)&&child!=null){
			if(child instanceof Collection){
				ResponseMessageJsonObject.accumulate(accumulateKey, JSONArray.fromObject(child,config));
			}else{
				try {
					ResponseMessageJsonObject.accumulate(accumulateKey, JSONObject.fromObject(child,config));
				} catch (JSONException e) {
					ResponseMessageJsonObject.accumulate(accumulateKey,child);
				}
			}
		}
		return ResponseMessageJsonObject.toString();
	}

}
