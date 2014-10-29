/**
 * 
 */
package com.nationsky.backstage.core.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.ServiceLocator;
import com.nationsky.backstage.common.ScheduleManager;

/**
 * 功能：系统启动初始化类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class StartupListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServiceLocator.initialize(WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()));
		if(Configuration.Config.getBoolean("platform.scheduleManagerStartup", true)){
			ScheduleManager.get().startupAllSchedules();
		}
	}
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
