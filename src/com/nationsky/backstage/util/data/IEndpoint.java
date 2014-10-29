/**
 * 
 */
package com.nationsky.backstage.util.data;


/**
 * 功能：端点标签即标签下的内容由自己实现类解析
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface IEndpoint {
	
	/**
	 * 功能：基础类型Endpoint
	 * @author yubaojian0616@163.com
	 *
	 * mobile enterprise application platform
	 * Version 0.1
	 */
	public enum EndpointEnum {
		INTEGER, LONG, DECIMAL, TIME, FILE, STRING, BOOLEAN, CUSTOM
	}
	
	/**
	 * 获得节点的枚举
	 * @return
	 */
	EndpointEnum getEndpointEnum();
	
	/**
	 * 获得节点值
	 * @return
	 */
	Object getValue();
	
	/**
	 * 获得节点的EndWith
	 * @return
	 */
	String getEndpointEndWith();
	
	/**
	 * 获得类型转换
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	<T extends IEndpoint> T getConvert(Class<T> clazz);
	
	/**
	 * 生成Xml节点值
	 * @return
	 */
	String toXmlValue();
	
	/**
	 * 生成Json节点值
	 * @return
	 */
	String toJsonValue();
}
