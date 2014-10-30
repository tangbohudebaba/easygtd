/**
 * 
 */
package com.nationsky.backstage.business.common;

import com.nationsky.backstage.ServiceLocator;
import com.nationsky.backstage.business.v1.bsc.ISecurityService;

/** 
 * @title : 
 * @description : 
 * @projectname : easygtd
 * @classname : CommonService
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年10月30日 下午5:42:46 
 */

public class CommonService {
	public static ISecurityService securityService=ServiceLocator.getService(ISecurityService.class);
}
