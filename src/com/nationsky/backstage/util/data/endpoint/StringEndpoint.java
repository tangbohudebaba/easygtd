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
import com.nationsky.backstage.util.data.IEndpoint;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public final class StringEndpoint extends AbstractEndpoint {
	
	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"s");
	
	private String value;
	
	/**
	 * 构造静态方法
	 * @param value
	 * @return
	 */
	public static StringEndpoint get(String value){
		StringEndpoint endpoint = new StringEndpoint();
		endpoint.value = value;
		return endpoint;
	}
	
	public static StringEndpoint buildByTarget(Object obj){
		return get(obj.toString());
	}
	
	public static StringEndpoint buildByXmlElement(Element element) {
		return get(element.getTextTrim());
	}
	
	public static StringEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		return get(jsonParser.getText());
	}
	
	private StringEndpoint(){}
	
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		return IEndpoint.EndpointEnum.STRING;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public String getValue() {
		
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
		
		return StringUtil.getCDATAData(this.value);
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#toJson()
	 */
	public String toJsonValue() {
		return StringUtil.concat("\"",DataTree.string2Json(value),"\"");
	}
	
	public String toString() {
		return this.value;
	}
}
