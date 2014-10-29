/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import com.nationsky.backstage.util.data.IEndpoint;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class AbstractEndpoint implements IEndpoint {

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#getConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IEndpoint> T getConvert(Class<T> clazz) {
		return (T) this;
	}

}
