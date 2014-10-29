/**
 * 
 */
package com.nationsky.backstage.common.bsc.dao;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface SequenceDAO {

	/**
	 * 获得通用的自增ID
	 * @param key
	 * @return
	 */
	Long genNextLongPK(String key);
	
}
