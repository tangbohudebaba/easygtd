/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.dom4j.Element;

import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.data.DataTree;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public final class LongEndpoint extends AbstractEndpoint{

	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"l");
	
	private Long value;
	
	/**
	 * 构造静态方法
	 * @param value
	 * @return
	 */
	public static LongEndpoint get(Long value){
		LongEndpoint endpoint = new LongEndpoint();
		endpoint.value = value;
		return endpoint;
	}
	
	public static LongEndpoint buildByTarget(Object obj){
		return get(Long.valueOf(obj.toString()));
	}
	
	public static LongEndpoint buildByXmlElement(Element element) {
		
		return get(StringUtil.getLong(element.getTextTrim()));
	}
	
	public static LongEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		return get(StringUtil.getLong(jsonParser.getText()));
	}
	
	private LongEndpoint(){}
	
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		
		return EndpointEnum.LONG;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public Long getValue() {
		
		return value;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEndWith()
	 */
	public String getEndpointEndWith() {
		
		return END_WITH;
	}
	
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#toXmlElement()
	 */
	public String toXmlValue() {
		
		return this.value.toString();
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#toJson()
	 */
	public String toJsonValue() {
		
		return StringUtil.concat("\"",this.value,"\"");
	}
	
	public String toString() {
		return this.value.toString();
	}
}
