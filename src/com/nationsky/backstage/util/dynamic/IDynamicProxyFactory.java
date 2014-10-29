/**
 * 
 */
package com.nationsky.backstage.util.dynamic;

/**
 * 功能：动态代理工厂接口
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface IDynamicProxyFactory {

	public <T> T createProxy(Class<T> clazz , T target, IDynamicInterceptor interceptor);
	
}
