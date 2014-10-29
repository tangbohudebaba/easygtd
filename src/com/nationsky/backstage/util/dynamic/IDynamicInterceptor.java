/**
 * 
 */
package com.nationsky.backstage.util.dynamic;

import java.lang.reflect.Method;

/**
 * 功能：动态代理周期拦截器
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface IDynamicInterceptor {

	void before(Method method,Object[] args);
	
	void after(Method method,Object[] args);
	
	void afterThrowing(Method method,Object[] args,Throwable throwable);
	
	void afterFinally(Method method,Object[] args);
	
}
