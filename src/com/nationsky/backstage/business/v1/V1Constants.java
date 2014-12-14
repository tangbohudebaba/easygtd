/**
 * 
 */
package com.nationsky.backstage.business.v1;

import java.io.File;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.core.Constants;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface V1Constants extends Constants {
	String headImgPath = Configuration.ROOT+File.separator+"uploads"+File.separator;
	String headImgpathHttp = Configuration.get("httpRootPath")+"uploads/";
	int baiduPushDeployStatus = Configuration.Config.getInt("push.deployStatus");
	
}
