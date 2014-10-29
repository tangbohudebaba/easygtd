/**
 * 
 */
package com.nationsky.backstage.core;

import java.util.List;

/**
 * 功能：处理无线子集的接口
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface InfiniteSubset<T extends PO> {

	
	/**
	 * 获得父类
	 * @return
	 */
	T getProcessParent();
	
	/**
	 * 获得父类链表
	 * @return
	 */
	List<T> getProcessLinkedList();
	
	/**
	 * 获得次子集的Path
	 * @return
	 */
	String getProcessLinkedPath();
	
}
