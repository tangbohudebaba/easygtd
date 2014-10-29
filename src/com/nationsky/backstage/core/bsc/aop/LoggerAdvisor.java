package com.nationsky.backstage.core.bsc.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 功能：用于记录所有服务方法调用的增强类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
@Aspect
@Component
public class LoggerAdvisor {
	
	Logger log = LoggerFactory.getLogger(LoggerAdvisor.class);   

	@Pointcut("execution(* com.nationsky.backstage.**.bsc.impl..*.*(..))")
	protected void servicePointcute(){}

	@Before("servicePointcute()")
	public void log(JoinPoint jp) {
		log.info("executing...." + jp.getSignature());
	}
}
