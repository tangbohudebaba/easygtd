/**
 * 
 */
package com.nationsky.backstage.util.dynamic.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.nationsky.backstage.util.dynamic.DynamicProxyInvocationHandler;
import com.nationsky.backstage.util.dynamic.IDynamicInterceptor;
import com.nationsky.backstage.util.dynamic.IDynamicProxyFactory;

/**
 * 功能：动态代理工厂实现
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DynamicProxyFactoryImpl implements IDynamicProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createProxy(Class<T> clazz, T target,
			IDynamicInterceptor interceptor) {
		InvocationHandler handler = new DynamicProxyInvocationHandler(target,interceptor);
		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{clazz}, handler);
	}

}
