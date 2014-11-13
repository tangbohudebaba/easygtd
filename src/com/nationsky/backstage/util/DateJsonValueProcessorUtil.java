/**
 * 
 */
package com.nationsky.backstage.util;
/** 
 * @title : 
 * @description : 
 * @projectname : se_vertical_client
 * @classname : sfds
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年4月25日 下午5:48:20 
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/*** 将Bean中的Timestamp转换为json中的日期字符串*/
public class DateJsonValueProcessorUtil implements JsonValueProcessor {
	public static final String Default_DATE_PATTERN ="yyyy-MM-dd HH:mm:ss";
	private DateFormat dateFormat ;
	public DateJsonValueProcessorUtil(String datePattern){
		try{
			dateFormat  = new SimpleDateFormat(datePattern);}
		catch(Exception e ){
			dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);
		}
	}
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}
	public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
		return process(value);
	}
	private Object process(Object value){
		return dateFormat.format((Date)value);
	}
	
	public static String ObjectToJson(Object object){
		JsonConfig config=new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessorUtil(Default_DATE_PATTERN));
		if(object instanceof Collection){
			 JSONArray jsonArray = JSONArray.fromObject(object,config);
			 return jsonArray.toString();
		}else{
			 return JSONObject.fromObject(object,config).toString();
		}
	   
	}
	
	/**
	 * 二级json组合
	 * @param parent 父节点实体类
	 * @param accumulateKey 子节点名称
	 * @param child 子节点(实体类或者实体类集合或者任意对象包括八大基本类型)
	 * @return
	 */
	public static String secondJsonJoint(Object parent,String accumulateKey , Object child){
		JsonConfig config=new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessorUtil(DateJsonValueProcessorUtil.Default_DATE_PATTERN));
		JSONObject ResponseMessageJsonObject = JSONObject.fromObject(parent,config);
		if(ValidateUtil.isNotNull(accumulateKey)&&child!=null){
			if(child instanceof Collection){
				ResponseMessageJsonObject.accumulate(accumulateKey, JSONArray.fromObject(child,config));
			}else{
					if(ValidateUtil.isPrimitive(child)){
						ResponseMessageJsonObject.accumulate(accumulateKey,child);
					}else{
						ResponseMessageJsonObject.accumulate(accumulateKey, JSONObject.fromObject(child,config));
					}
			}
		}
		return ResponseMessageJsonObject.toString();
	}

	
	
	
}