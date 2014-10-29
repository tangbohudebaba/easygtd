/**
 * 
 */
package com.nationsky.backstage.common.bsc;

import com.nationsky.backstage.core.bsc.IPersistentService;

/**
 * 功能：通用的一些工具服务接口
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface ICommonService extends IPersistentService {

	Long genNextLongPK(String key);
	
}
