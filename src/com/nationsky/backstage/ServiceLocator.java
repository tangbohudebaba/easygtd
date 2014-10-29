/**
 * 
 */
package com.nationsky.backstage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nationsky.backstage.core.bsc.Service;
import com.nationsky.backstage.core.web.listener.StartupListener;
import com.nationsky.backstage.util.DataUtil;

/**
 * 功能：服务定位器，用于处理一些无法定位Bean的java调用
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class ServiceLocator {
	
	/**
	 * 
	 */
	private static ApplicationContext context;
	
	/**
	 * 通过名称获得service对象
	 * @param <T>
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Service> T getService(String name){
		return (T) getApplicationContext().getBean(name);
	}
	
	/**
	 * 获得service对象
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T extends Service> T getService(Class<T> clazz){
		try{
			return getApplicationContext().getBean(clazz);
		}catch(Exception e){
			return null;
		}
	}
	
	public static ApplicationContext getApplicationContext(){
		if(context == null)
			context = getApplicationContext("spring.cfg.xml");
		return context;
	}
	
	/**
	 * 此方法仅仅提供应用启动监听器调用
	 * @param context
	 */
	public static void initialize(ApplicationContext context){
		if(DataUtil.getInvokeClass() == StartupListener.class){
			ServiceLocator.context = context;
		}else{
			throw new java.lang.IllegalArgumentException("The Method Can Only Invoke By StartupListener");
		}
	}
	
	private static synchronized ApplicationContext getApplicationContext(String...locations){
		if(context == null){
			context = new ClassPathXmlApplicationContext(locations);
		}
		return context;
	}
}
