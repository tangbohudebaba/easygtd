package com.nationsky.backstage;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;

/**
 * 功能：系统配置类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class Configuration{
	
	public static XMLConfiguration Config = null;
	
	private static final String _RES_BASE_NAME="resources";
	/**
	 * 后面不带文件分隔符的地址
	 */
	public static String ROOT;
    public static String CURRENT_PATH;
    public static String TEMPLATE_PATH;
    public static String TEMPLATE_WEB_PATH;
    public static String TEMPORARY_PATH;
    public static String LOG_PATH;
    public static String CLASSPATH;
    public static String CONFIG_PATH;

	public static String TEMPLATE_URI="/WEB-INF/template/views";
    
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	
	private static final String SECRET_CONFIG = "site.secret";
	private static final String DEFAULT_SECRET_KEY = "SECRET_CONFIG";
	
	static{
		ROOT = new File(Configuration.class.getResource("/").getPath()).getParentFile().getParent().toString();
    	CURRENT_PATH = new File(Configuration.class.getResource("/").getPath()).toString();
    	TEMPLATE_PATH = StringUtil.concat(ROOT,File.separator,"WEB-INF",File.separator,"template");
    	TEMPLATE_WEB_PATH = StringUtil.concat(TEMPLATE_PATH,File.separator,"views");
    	TEMPORARY_PATH = StringUtil.concat(ROOT,File.separator,"WEB-INF",File.separator,"tmp");
    	CONFIG_PATH = StringUtil.concat(ROOT,File.separator,"WEB-INF",File.separator,"conf");
    	LOG_PATH = StringUtil.concat(ROOT,File.separator,"WEB-INF",File.separator,"logs");
    	StringBuilder sb = new StringBuilder();
    	List<File> files = FileUtil.getFiles(new File(StringUtil.concat(ROOT,File.separator,"WEB-INF",File.separator,"lib")));
    	sb.append(".");
    	for(File file:files){
    		sb.append(System.getProperty("path.separator"));
    		sb.append(file.getAbsolutePath());
    	}
    	sb.append(System.getProperty("path.separator"));
    	sb.append(CURRENT_PATH);
    	CLASSPATH = sb.toString();
    	try {
    		Config = new XMLConfiguration();
    		Config.setFile(new File(StringUtil.concat(CURRENT_PATH,File.separator,"system.cfg.xml")));
    		Config.setReloadingStrategy(new FileChangedReloadingStrategy());
			Config.load();
			Config.setEncoding("UTF-8");
			/*Config.setListDelimiter(' ');
			Config.setDelimiterParsingDisabled(true);*/
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得字符串属性
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return Config.getString(key);
	}
	/**
	 * 获得密钥
	 * @return
	 */
	public static String getSecurityKey(){
		return StringUtil.get(get(SECRET_CONFIG),DEFAULT_SECRET_KEY);
	}
	/**
	 * 获得编码
	 * @return
	 */
	public static String getEncoding(){
		return "utf-8";
	}
	/**
	 * 获得ContentType
	 * @return
	 */
	public static String getContentType(){
		return "text/html;charset=UTF-8";
	}
	
	/**
	 * 获得资源信息
	 * @param key
	 * @param 信息参数
	 * @return
	 */
	public static String getResource(String key,Object... params){
		if(params==null || params.length < 1)
			return ResourceBundle.getBundle(_RES_BASE_NAME,Locale.CHINA).getString(key);
		else
			return MessageFormat.format(ResourceBundle.getBundle(_RES_BASE_NAME,Locale.CHINA).getString(key), params);
	}
	
	/**
	 * 获得资源信息
	 * @param key
	 * @return
	 */
	public static String getResource(String key){
		String value = key;
		try{
			ResourceBundle resource = ResourceBundle.getBundle(_RES_BASE_NAME, Locale.CHINA);
			String temp = resource.getString(key);
			if(temp != null) value = temp;
		}catch(Exception e){
			
		}
		return value;
	}
	
}
