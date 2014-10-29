package com.nationsky.backstage.core.bsc.aop;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nationsky.backstage.core.I18n;
import com.nationsky.backstage.util.DataUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：负责记录服务调用性能加强类，监控调用方法耗时比较长的进行记录
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
@Aspect
@Component
public class PerformanceAdvisor {
	
	private Logger log = LoggerFactory.getLogger(PerformanceAdvisor.class);
	
	@Pointcut("execution(* com.nationsky.backstage.**.bsc.impl..*.*(..))")
	protected void methodPointcute(){}
	
	@Around("methodPointcute()")
	public Object performance(ProceedingJoinPoint pjp)throws Throwable {
		StopWatch watch = new StopWatch();
		watch.start();
		Object obj = pjp.proceed();
		watch.stop();
		long usedTime = watch.getTime();
		if(ValidateUtil.isBetween(usedTime,100,300)){
			log.info((I18n.getMessage("system.info.performance.log", StringUtil.concat(DataUtil.getInvokeClassName(),"-",DataUtil.getInvokeMethodName(),"-",pjp.getSignature()),usedTime)));
		}else if(ValidateUtil.isBetween(usedTime, 301, 500)){
			log.warn((I18n.getMessage("system.info.performance.log", StringUtil.concat(DataUtil.getInvokeClassName(),"-",DataUtil.getInvokeMethodName(),"-",pjp.getSignature()),usedTime)));
		}else if(usedTime>500){
			log.error((I18n.getMessage("system.info.performance.log", StringUtil.concat(DataUtil.getInvokeClassName(),"-",DataUtil.getInvokeMethodName(),"-",pjp.getSignature()),usedTime)));
		}else{
			log.debug((I18n.getMessage("system.info.performance.log", StringUtil.concat(DataUtil.getInvokeClassName(),"-",DataUtil.getInvokeMethodName(),"-",pjp.getSignature()),usedTime)));
		}
		return obj;
	}
}
