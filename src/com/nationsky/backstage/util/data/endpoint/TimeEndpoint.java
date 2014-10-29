/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.dom4j.Element;

import com.nationsky.backstage.util.DateUtil;
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
public final class TimeEndpoint extends AbstractEndpoint {
	
	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"t");
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private Date value;
	
	/**
	 * 构造静态方法
	 * @param value
	 * @return
	 */
	public static TimeEndpoint get(Date value){
		TimeEndpoint endpoint = new TimeEndpoint();
		endpoint.value = value;
		return endpoint;
	}
	
	public static TimeEndpoint buildByTarget(Object obj){
		if(obj instanceof java.sql.Date){
			return get(DateUtil.getDateByCurrentTimeMillis(((java.sql.Date)obj).getTime()));
		}
		return get((Date)obj);
	}
	
	public static TimeEndpoint buildByXmlElement(Element element) {		
		return get(DateUtil.parse(element.getTextTrim(), TIME_FORMAT));
	}
	
	public static TimeEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		return get(DateUtil.parse(jsonParser.getText(), TIME_FORMAT));
	}
	
	private TimeEndpoint(){}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getEndpointEnum()
	 */
	public EndpointEnum getEndpointEnum() {
		
		return IEndpoint.EndpointEnum.TIME;
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public Date getValue() {
		
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
		
		return DateUtil.format(value, TIME_FORMAT);
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#toJson()
	 */
	public String toJsonValue() {
		
		return StringUtil.concat("\"",DateUtil.format(value, TIME_FORMAT),"\"");
	}
	
	public String toString() {
		return DateUtil.format(value, TIME_FORMAT);
	}
}
