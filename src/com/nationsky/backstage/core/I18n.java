/**
 * 
 */
package com.nationsky.backstage.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.nationsky.backstage.util.ExcptUtil;

/**
 * 功能：国际化语言调用类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class I18n {
	public static final String FILED_RPEAT = getMessage("core.field.rpeat");
	public static final String FILED_EXISTS = getMessage("core.field.exists");
	public static final String FILED_REQUIRED = getMessage("core.field.required");
	public static final String FILED_UNLAWFUL = getMessage("core.field.unlawful");
	
	private static final String MSG_BASE_NAME="i18n/messages";
	/**
	 * 获得信息
	 * @param key
	 * @param 信息参数
	 * @return
	 */
	public static String getMessage(String key,Object... params){
		if(params==null || params.length < 1){
			return ResourceBundle.getBundle(MSG_BASE_NAME,Locale.CHINA).getString(key);
		}
		else{
			return MessageFormat.format(ResourceBundle.getBundle(MSG_BASE_NAME,Locale.CHINA).getString(key), params);
		}
	}
	
	/**
	 * 获得信息
	 * @param key
	 * @return
	 */
	public static String getMessage(String key){
		String value = key;
		try{
			ResourceBundle resource = ResourceBundle.getBundle(MSG_BASE_NAME, Locale.CHINA);
			String temp = resource.getString(key);
			if(temp != null) value = temp;
		}catch(Exception e){
			ExcptUtil.unchecked(e);
		}
		return value;
	}

}
