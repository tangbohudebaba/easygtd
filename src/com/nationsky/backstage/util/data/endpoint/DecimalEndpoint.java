/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import java.io.IOException;
import java.math.BigDecimal;

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
public final class DecimalEndpoint extends AbstractEndpoint {

	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"d");
	
	private BigDecimal value;
	
	/**
	 * 构造静态方法
	 * @param value
	 * @return
	 */
	public static DecimalEndpoint get(BigDecimal value){
		DecimalEndpoint endpoint = new DecimalEndpoint();
		endpoint.value = value;
		return endpoint;
	}
	
	public static DecimalEndpoint buildByTarget(Object obj){
		return get(new BigDecimal(obj.toString()));
	}
	
	public static DecimalEndpoint buildByXmlElement(Element element) {
		
		return get(StringUtil.getBigDecimal(element.getTextTrim()));
	}
	
	public static DecimalEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		return get(StringUtil.getBigDecimal(jsonParser.getText()));
	}
	
	private DecimalEndpoint(){}
	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		
		return IEndpoint.EndpointEnum.DECIMAL;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public BigDecimal getValue() {
		
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
