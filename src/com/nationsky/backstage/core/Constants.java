/**
 * 
 */
package com.nationsky.backstage.core;

/**
 * 功能:定义核心模块中的常量
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface Constants {
	
	/**
	 * 网页是否持久化的对象名称
	 * 当不需要持久化的页面可以通过request.setAttribute("web.page.persist",false)来停用
	 */
	public static final String WEB_PAGE_PERSIST = "web.page.persist";
	
}
