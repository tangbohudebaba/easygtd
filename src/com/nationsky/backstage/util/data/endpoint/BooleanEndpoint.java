/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.dom4j.Element;

import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;
import com.nationsky.backstage.util.data.DataTree;
import com.nationsky.backstage.util.data.IEndpoint;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public final class BooleanEndpoint extends AbstractEndpoint {
	
	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"b");
	
	private Boolean value;
	
	/**
	 * 构造静态方法
	 * @param value
	 * @return
	 */
	public static BooleanEndpoint get(Boolean value){
		BooleanEndpoint endpoint = new BooleanEndpoint();
		endpoint.value = value;
		return endpoint;
	}
	
	public static BooleanEndpoint buildByTarget(Object obj){
		return get(Boolean.valueOf(obj.toString()));
	}
	
	public static BooleanEndpoint buildByXmlElement(Element element) {		
		return get(ValidateUtil.isEquals("1", element.getTextTrim()));
	}
	
	public static BooleanEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		return get(ValidateUtil.isEquals("1", jsonParser.getText()));
	}
	
	private BooleanEndpoint(){}
	
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		
		return IEndpoint.EndpointEnum.BOOLEAN;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public Boolean getValue() {
		
		return value;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEndWith()
	 */
	public String getEndpointEndWith() {
		
		return END_WITH;
	}

	public String toXmlValue() {
		
		return this.value?"1":"0";
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#toJson()
	 */
	public String toJsonValue() {
		
		return StringUtil.concat("\"",this.value?"1":"0","\"");
	}

	public String toString() {
		return this.value.toString();
	}

}
