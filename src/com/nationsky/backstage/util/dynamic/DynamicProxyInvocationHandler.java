/**
 * 
 */
package com.nationsky.backstage.util.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * 功能：动态代理调度器
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DynamicProxyInvocationHandler implements InvocationHandler {

	private Object target ;
	private IDynamicInterceptor interceptor;
	
	public DynamicProxyInvocationHandler(Object target,IDynamicInterceptor interceptor){
		this.target = target;
		this.interceptor = interceptor;
	}
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object obj = null;
		try{
			interceptor.before(method, args);
			obj = method.invoke(target, args);
			interceptor.after(method, args);
			return obj;
		}catch(Throwable throwable){
			interceptor.afterThrowing(method, args, throwable);
		}finally{
			interceptor.afterFinally(method, args);
		}
		return obj;
	}

}
