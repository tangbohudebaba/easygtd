package com.nationsky.backstage.core.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.util.ExcptUtil;
import com.nationsky.backstage.util.HttpUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：处理cookie工具类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class CookieUtil {
	
	public static final int AGE_SESSION = -1;
	public static final int AGE_DELETE = 0;
	public static final String DEF_COOKIE_DOMAIN = Configuration.get("cookie.domain");
	public static final String DEF_COOKIE_PATH = StringUtil.get(Configuration.get("cookie.path"), "/");
	
	/**
	 * 设置Cookie对象
	 * @param key
	 * @param value
	 * @param expiry
	 * @param response
	 */
	public static void set(String key,String value,int expiry,HttpServletResponse response){
		set(key,value,expiry,DEF_COOKIE_DOMAIN,DEF_COOKIE_PATH,response);
	}
	/**
	 * 设置Cookie对象
	 * @param key
	 * @param value
	 * @param expiry
	 * @param domain
	 * @param path
	 * @param response
	 */
	public static void set(String key,String value,int expiry,String domain,String path,HttpServletResponse response){
		if(value==null)return ;
		Cookie cookie = new Cookie(key,HttpUtil.getURLEncode(value, Configuration.getEncoding()));
		cookie.setMaxAge(expiry);
		cookie.setDomain(StringUtil.get(domain, DEF_COOKIE_DOMAIN));
		cookie.setPath(StringUtil.get(path, DEF_COOKIE_PATH));
		try{
			response.addCookie(cookie);
		}catch(Exception e){
			ExcptUtil.unchecked(e);
		}
	}
	
	/**
	 * 获得Cookie对象值
	 * @param key
	 * @param request
	 * @return
	 */
	public static String get(String key,HttpServletRequest request){
		return get(key,DEF_COOKIE_DOMAIN,DEF_COOKIE_PATH,request);
	}
	
	/**
	 * 获得Cookie对象值
	 * @param key
	 * @param domain
	 * @param path
	 * @param request
	 * @return
	 */
	public static String get(String key , String domain ,String path ,HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(!ValidateUtil.isNullArray(cookies)){
			for(Cookie cookie : cookies){				
				if(cookie.getName().equals(key)){
					return HttpUtil.getURLDecode(cookie.getValue(), "utf-8");
				}
			}
		}
		return null;
	}
	
	/**
	 * 删除Cookie对象
	 * @param key
	 * @param response
	 */
	public static void remove(String key,HttpServletResponse response){ 
		remove(key,DEF_COOKIE_DOMAIN,DEF_COOKIE_PATH,response);
	}
	
	/**
	 * 删除Cookie对象
	 * @param key
	 * @param domain
	 * @param path
	 * @param response
	 */
	public static void remove(String key,String domain,String path,HttpServletResponse response){
		if(key==null)return ;
		Cookie cookie = new Cookie(key,null);
		cookie.setDomain(StringUtil.get(domain, DEF_COOKIE_DOMAIN));
		cookie.setPath(StringUtil.get(path, DEF_COOKIE_PATH));
		response.addCookie(cookie);
	}
}
