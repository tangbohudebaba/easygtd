/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;


/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class AbstractCustomEndpoint extends AbstractEndpoint {
	
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		return EndpointEnum.CUSTOM;
	}
	
}
