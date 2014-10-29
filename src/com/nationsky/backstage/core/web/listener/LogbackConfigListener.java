/**
 * 
 */
package com.nationsky.backstage.core.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class LogbackConfigListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(LogbackConfigListener.class);
	
	private static final String CONFIG_LOCATION = "logbackConfigLocation";
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//从web.xml中加载指定文件名的日志配置文件   
        String logbackConfigLocation = event.getServletContext().getInitParameter(CONFIG_LOCATION);  
        String fn = event.getServletContext().getRealPath(logbackConfigLocation);  
        try {  
            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();  
            loggerContext.reset();  
            JoranConfigurator joranConfigurator = new JoranConfigurator();  
            joranConfigurator.setContext(loggerContext);  
            joranConfigurator.doConfigure(fn);  
            logger.debug("loaded slf4j configure file from {}", fn);  
        }  
        catch (JoranException e) {  
            logger.error("can loading slf4j configure file from " + fn, e);  
        } 
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

}
