/**
 * 
 */
package com.nationsky.backstage.core.web.tag;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.core.I18n;
import com.nationsky.backstage.core.web.util.WebUtil;
import com.nationsky.backstage.util.DataUtil;
import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：页面中使用的JSTL标签
 * 用法：${u:int()}
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class UtilTag {
	
	/**
	 * 获得root目录
	 * @return
	 */
	public static String root(){
		return Configuration.ROOT;
	}
	
	/**
	 * 获得配置文件属性值
	 * @param key
	 * @return
	 */
	public static String config(String key){
		return Configuration.Config.getString(key);
	}
	
	/**
	 * 判断字符串分割后是否包含sub
	 * @param sIn 字符串
	 * @param split 分隔符
	 * @param sub 是否包含此sub
	 * @return
	 */
	public static boolean contains(String sIn,String split,String sub){
		if(ValidateUtil.isNull(sIn))return false;
		if(ValidateUtil.isNull(split))return sIn.contains(sub);
		return StringUtil.tokenize(sIn, split).contains(sub);
	}
	
	/**
	 * 判断是否包含obj
	 * @param <T>
	 * @param coll
	 * @param obj
	 * @return
	 */
	public static <T extends Object> boolean containsObject(Collection<T> coll , Object obj){
		if(coll==null || coll.isEmpty())return false;
		return coll.contains(obj);
	}
	
	/**
	 * 判断collection是否包含另外一个collection
	 * @param <T>
	 * @param coll1
	 * @param coll2
	 * @return
	 */
	public static <T extends Object> boolean containsList(Collection<T> coll1,Collection<T> coll2){
		if(coll1==null || coll1.isEmpty() || coll2==null || coll2.isEmpty())return false;
		return coll1.containsAll(coll2);
	}
	
	public static boolean isNull(Object obj){
		if(obj==null)return true;
		if(obj instanceof String)return ValidateUtil.isNull(obj.toString());
		return false;
	}
	
	public static boolean isNotNull(Object obj){
		if(obj == null)return false;
		if(obj instanceof String)return ValidateUtil.isNotNull(obj.toString());
		return true;
	}
	
	public static boolean isExistsFile(String file){
		return FileUtil.exists(file);
	}
	
	public static int getInt(Object obj){
		return StringUtil.getInt(obj);
	}
	
	public static double getDouble(String obj){
		return StringUtil.getDouble(obj);
	}
	
	public static boolean getBoolean(String str){
		return StringUtil.getBoolean(str);
	}
	
	public static Date getNow(){
		return new Date();
	}
	
	public static Date getDateAgo(Date date,int days){
		return DateUtil.getDateAgo(date, days);
	}
	
	public static int getIntervalTime(Date begin,Date end,String type){
		if(ValidateUtil.isEquals("days", type)){
			return DateUtil.getIntervalDays(begin, end);
		}else
			if(ValidateUtil.isEquals("minites", type)){
				return DateUtil.getIntervalMinites(begin, end);
			}else{
				return DateUtil.getIntervalHours(begin, end);
			}
	}
	
	public static Object is(boolean boo,Object obj1,Object obj2){
		return boo?obj1:obj2;
	}
	
	public static int min(int i,int j){
		return Math.min(i, j);
	}

	public static int max(int i,int j){
		return Math.max(i, j);
	}
	
	public static long round(double dou){
		return Math.round(dou);
	}
	
	public static boolean regexIs(String term,String pattern){
		return ValidateUtil.isRegex(term, pattern);
	}
	
	public static String truncate(String text,int length){
		//return org.apache.commons.lang.StringUtils.abbreviate(text, length);
		return StringUtil.substringByByte(text, length*2, "");
	}
	
	public static String string(Object obj){
		return obj==null?"":obj.toString();
	}
	
	public static String concat2(String str1,String str2){
		return StringUtil.concat(str1,str2);
	}
	
	public static String concat4(String str1,String str2,String str3,String str4){
		return StringUtil.concat(str1,str2,str3,str4);
	}
	
	public static String concat6(Object obj0,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5){
		return StringUtil.concat(obj0,obj1,obj2,obj3,obj4,obj5);
	}
	
	public static String getFormatSecondText(int second){
		return DateUtil.getFormatSecondText(second);
	}
	
	public static String getMessage(String key,String[] params){
		return I18n.getMessage(key,(Object[])params);
	}
	
	/**
	 * 获得queryString
	 * @param request
	 * @param params不获取的参数
	 * @return
	 */
	public static String getQueryString(HttpServletRequest request,String params){
		return WebUtil.parseQueryString(request, StringUtil.tokenizeToArray(params, ","), Configuration.getEncoding());
	}
	/**
	 * 合并queryString
	 * @param queryString
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public static String appendParam(String queryString,String paramName,String paramValue){
		StringBuilder sb = new StringBuilder();
		if(ValidateUtil.isNull(queryString))
			sb.append(paramName).append(paramName.matches("=")?"":"=").append(paramValue);
		else{
			sb.append(queryString).append("&").append(paramName).append(paramName.matches("=")?"":"=").append(paramValue);
		}
		return sb.toString();
	}
	
	/**
	 * 获得合并后的queryString对象
	 * @param request
	 * @param rParams 不提取的参数以','分割
	 * @param nParams 新添加的参数列表
	 * @return
	 */
	public static String getAppendParamQueryString(HttpServletRequest request,String rParams,String nParams){
		String queryString = WebUtil.parseQueryString(request, StringUtil.tokenizeToArray(rParams, ","), Configuration.getEncoding());
		if(ValidateUtil.isNull(queryString)){
			if(ValidateUtil.isNotNull(nParams))
				queryString = StringUtil.concat("?",nParams); 
		}else{
			if(ValidateUtil.isNotNull(nParams))
				queryString = StringUtil.concat("?",queryString,"&",nParams);
			else
				queryString = StringUtil.concat("?",queryString);
		}
		return queryString;
	}
	
	public static String getUrlRequest(HttpServletRequest request,String params){
		return WebUtil.urlRequest(request, StringUtil.tokenizeToArray(params, ","), Configuration.getEncoding());
	}
	
	public static String encoding(String value){
		return WebUtil.urlEncoding(value, Configuration.getEncoding());
	}
	
	public static String decoding(String value){
		return WebUtil.urlDecoding(value, Configuration.getEncoding());
	}
	
	public static String getRemoteAddress(HttpServletRequest request){
		return WebUtil.getRemoteAddr(request);
	}
	
	public static String ascii(int i){
		return String.valueOf((char)(i));
	}
	
	public static String htmlText(String str){
		if(ValidateUtil.isNull(str))return "";
		return StringUtil.getHrefText(str);
	}
	
	public static String getFriendlyWebText(String str){
		return new StringBuffer().append("<p>").append(StringUtil.regexReplace(htmlText(str),"\\s*\\n+\\s*","</p><p>")).append("</p>").toString();
	}
	
	public static String getStyleText(String text,String style){
		return StringUtil.getText(text, style);		
	}
	
	public static List<String> intToStringList(String iString){
		List<String> list = new ArrayList<String>();
		for(String str : StringUtil.tokenizeToArray(iString, ",")){
			list.add(str);
		}
		return list;
	}
	
	public static String getParameters(String param ,String split , HttpServletRequest request){
		if(param==null)return null;
		String[] str = (String[])request.getParameterValues(param);
		if(str != null){
			StringBuilder sb = new StringBuilder();
			for(int i=0,j=str.length;i<j;i++){
				if(i>0)sb.append(split);
				sb.append(str[i]);
			}
			return sb.toString();
		}
		return null;
	}
	
	public static String get(String text,String def){
		return StringUtil.get(text, def);
	}
	
	public static String getNonNullText(String msg,String beginStyle,String endStyle){
		if(ValidateUtil.isNull(msg))
			return StringUtil.EMPTY_STRING;
		else
			return new StringBuffer().append(beginStyle).append(msg).append(endStyle).toString();
	}
	
	public static List<String> listFiles(String directory,String pattern){
		List<String> resultFiles = new ArrayList<String>();
		File dir = new File(StringUtil.concat(directory));
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files!=null){
				for(File file : files){
					String filename = FileUtil.getFileName(file.getPath());
					if(ValidateUtil.isNotNull(pattern)){
						if(ValidateUtil.isRegex(filename, pattern)){
								resultFiles.add(filename);
						}
					}else{
						resultFiles.add(filename);
					}
				}
			}
		}
		return resultFiles;
	}
	
	public static Date getDate(long currentTimeMillis){
		try{
			return DateUtil.getDateByCurrentTimeMillis(currentTimeMillis);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 获得友好的时间显示格式
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String getFriendlyIntervalTime(Date begin,Date end){
		begin = begin==null?new Date():begin;
		end = end == null?new Date():end;
		int day = DateUtil.getIntervalDays(begin, end);
		String text = null ;
		if(day>14)
			text = DateUtil.format(begin, "yyyy-MM-dd");
		else if (day == 1)
			text = I18n.getMessage("core.yestoday");
		else if (day == 2)
			text = I18n.getMessage("core.beforeYestoday");
		else if(day>2)
			text = StringUtil.concat(day,I18n.getMessage("core.dayAgo"));
		else{
			int hours = DateUtil.getIntervalHours(begin, end);
			if(hours>0)
				text = StringUtil.concat(hours,I18n.getMessage("core.hourAgo"));
			else{
				int minite = DateUtil.getIntervalMinites(begin, end);
				if(minite>0)
					text = StringUtil.concat(minite,I18n.getMessage("core.minuteAgo"));
				else{
					text = StringUtil.concat(DateUtil.getIntervalSecond(begin, end),I18n.getMessage("core.secondAgo"));					
				}
			}
		}
		return text;
	}
	
	/**
	 * 将列表倒叙
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> getReversionList(List<T> list){
		return DataUtil.getReversionList(list);
	}
	
	/**
	 * 获得指定小数位
	 * @param value
	 * @param fixed
	 * @return
	 */
	public static BigDecimal getFixedBigDecimal(String value,int fixed){
		return StringUtil.getFixedBigDecimal(value,fixed);
	}
	
	/**
	 * 获得无参数的对象方法调用
	 * @param target
	 * @param method
	 * @return
	 */
	public static Object getInvokeReturn(Object target,String method,Object[] args){
		return DataUtil.getInvokeReturn(target, method, args);
	}
	
	/**
	 * 格式化时间显示
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date,String format){
		return DateUtil.format(date, format);
	}
	
	/**
	 * 获得正则匹配的字符串
	 * @param text
	 * @param pattern
	 * @param group
	 * @return
	 */
	public static String getMatcherGroup(String text,String pattern,int group){
		return StringUtil.getMatcherGroup(text, pattern, group);
	}
	
}
