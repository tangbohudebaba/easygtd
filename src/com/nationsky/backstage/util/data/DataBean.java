/**
 * 
 */
package com.nationsky.backstage.util.data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nationsky.backstage.util.BeanUtil;
import com.nationsky.backstage.util.DataUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;
import com.nationsky.backstage.util.data.endpoint.BooleanEndpoint;
import com.nationsky.backstage.util.data.endpoint.DecimalEndpoint;
import com.nationsky.backstage.util.data.endpoint.FileEndpoint;
import com.nationsky.backstage.util.data.endpoint.IntegerEndpoint;
import com.nationsky.backstage.util.data.endpoint.LongEndpoint;
import com.nationsky.backstage.util.data.endpoint.StringEndpoint;
import com.nationsky.backstage.util.data.endpoint.TimeEndpoint;
import com.nationsky.backstage.util.data.endpoint.FileEndpoint.FileItem;


/**
 * 功能：数据存储BEAN
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DataBean {
	
	private String elementName;	
	
	private Map<String, List<IEndpoint>> endpointElementMap = new LinkedHashMap<String,List<IEndpoint>>();
	
	private List<DataBean> childElementBeans = new ArrayList<DataBean>();
	
	public static final Map<Class<? extends IEndpoint>,Class<?>[]> ENDPOINT_TARGET_CLAZZ;

	static{
		ENDPOINT_TARGET_CLAZZ = new HashMap<Class<? extends IEndpoint>,Class<?>[]>();
		ENDPOINT_TARGET_CLAZZ.put(BooleanEndpoint.class, new Class<?>[]{boolean.class,Boolean.class});
		ENDPOINT_TARGET_CLAZZ.put(IntegerEndpoint.class, new Class<?>[]{int.class,Integer.class,short.class,Short.class});
		ENDPOINT_TARGET_CLAZZ.put(LongEndpoint.class, new Class<?>[]{long.class,Long.class,BigInteger.class});
		ENDPOINT_TARGET_CLAZZ.put(DecimalEndpoint.class, new Class<?>[]{double.class,Double.class,float.class,Float.class,BigDecimal.class});
		ENDPOINT_TARGET_CLAZZ.put(TimeEndpoint.class, new Class<?>[]{java.sql.Date.class,java.util.Date.class});
		ENDPOINT_TARGET_CLAZZ.put(StringEndpoint.class, new Class<?>[]{byte.class,Byte.class,char.class,Character.class,Enum.class,String.class});
		ENDPOINT_TARGET_CLAZZ.put(FileEndpoint.class, new Class<?>[]{FileItem.class});
	}
	
	public DataBean(){}
	
	public DataBean(String elementName){
		this.elementName = elementName;
	}
	
	/**
	 * 静态创建方法
	 * @param string
	 * @return
	 */
	public static DataBean get(String elementName) {
		return get(elementName,(Object)null);
	}
	
	/**
	 * 通过java bean获得DataBean
	 * 注意:java bean中的复合类型不会被处理
	 * 支持的类型:
	 * java.math.BigInteger,boolean,java.lang.Boolean,byte,java.lang.Byte,char,java.lang.Character,java.util.Date,java.sql.Date
	 * java.math.BigDecimal,double,java.lang.Double,float,java.lang.Float,int,java.lang.Integer,long,java.lang.Long,short,java.lang.Short
	 * @param elementName
	 * @param bean
	 * @return
	 */
	public static DataBean get(String elementName,Object bean){
		DataBean dataBean = new DataBean(elementName);
		if(bean!=null){
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					Class<?> propertyClazz = pds[i].getPropertyType();//属性类型
					String propertyName = pds[i].getName();//属性名
					if(ValidateUtil.isEquals("class", propertyName)){//每个类都有一个隐藏的class属性名,所以过滤掉
						continue;
					}
					Object propertyValue = BeanUtil.getProperty(bean,pds[i]);//属性值
					if(propertyValue==null){
						continue;
					}
					outter:{
						for(Class<?> key:ENDPOINT_TARGET_CLAZZ.keySet()){
							for(Class<?> enumClazz:ENDPOINT_TARGET_CLAZZ.get(key)){
								if(enumClazz.isInstance(propertyValue) || ( propertyClazz.isEnum() && enumClazz == Enum.class)){
									dataBean.putEndpoint(propertyName,(IEndpoint) key.getMethod("buildByTarget", Object.class).invoke(key, propertyValue));
									break outter;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return  dataBean;
	}
	
	/**
	 * 通过java bean列表获得Data Bean
	 * @param elementName
	 * @param list
	 * @return
	 */
	public static DataBean get(String elementName,List<?> list){
		DataBean listBean = DataBean.get(elementName);
		if(!ValidateUtil.isNullCollection(list)){
			for(Object object:list){
				listBean.addChildElementBean(get(StringUtil.toLowerCaseFirstChar(object.getClass().getSimpleName()),object));
			}
		}
		return listBean;
	}
	
	/**
	 * 获得节点名
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * 获得节点下面的bean列表
	 * @return the childElementBeans
	 */
	public List<DataBean> getChildElementBeans() {
		return childElementBeans;
	}
	
	/**
	 * 获得所有节点的Key
	 * @return
	 */
	public Collection<String> getEndpointKeys(){
		return this.endpointElementMap.keySet();
	}
	
	/**
	 * 添加节点
	 * @param element
	 * @param endpoints
	 * @return
	 */
	public DataBean putEndpoint(String element,IEndpoint... endpoints){
		if(!ValidateUtil.isNullArray(endpoints)){
			if(this.endpointElementMap.containsKey(element)){
				this.endpointElementMap.get(element).addAll(DataUtil.getList(endpoints));
			}else{
				this.endpointElementMap.put(element, DataUtil.getList(endpoints));
			}
		}		
		return this;
	}
	
	/**
	 * 添加节点下的Bean
	 * @param dataBean
	 * @return
	 */
	public DataBean addChildElementBean(DataBean...dataBeans) {
		if(!ValidateUtil.isNullArray(dataBeans)){
			this.childElementBeans.addAll(DataUtil.getList(dataBeans));
		}		
		return this;
	}
	
	/**
	 * 获得节点属性
	 * @param string
	 * @return
	 */
	private IEndpoint getEndpoint(String element) {
		List<IEndpoint> endpoints = this.endpointElementMap.get(element);
		if(endpoints!=null && !endpoints.isEmpty()){
			return endpoints.get(0);
		}
		return null;
	}
	
	/**
	 * 获得节点属性值
	 * @param element
	 * @return
	 */
	public Object getEndpointValue(String element){
		return DataUtil.getInvokeReturn(getEndpoint(element), "getValue");
	}
	
	/**
	 * 获得节点属性
	 * @param element
	 * @return
	 */
	public List<IEndpoint> getListEndpoint(String element){
		return this.endpointElementMap.get(element);
	}
	
	/**
	 * 获得节点值list
	 * @param element
	 * @return
	 */
	public List<?> getListEndpointValue(String element){
		List<IEndpoint> list = this.getListEndpoint(element);
		if(list==null)
			return null;
		List<Object> resultList = new ArrayList<Object>();
		for(IEndpoint endpoint : list){
			if(endpoint!=null)
				resultList.add(endpoint.getValue());
		}
		return resultList;
	}
	
	/**
	 * 生成不带xml标头的字符串
	 * @return
	 */
	public String toXmlUnHeader(){
		StringBuilder sb = new StringBuilder();
		this.toXmlElement(sb);
		return sb.toString();
	}
	
	/**
	 * 生成Xml
	 * @return
	 */
	public String toXml(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.toXmlElement(stringBuilder);
		return stringBuilder.toString();
	}
	
	private void toXmlElement(StringBuilder stringBuilder){
		stringBuilder.append("<").append(elementName).append(">");
		for(String key:endpointElementMap.keySet()){
			List<IEndpoint> endpoints = endpointElementMap.get(key);
			for(IEndpoint endpoint : endpoints){
				stringBuilder.append("<").append(key).append(endpoint.getEndpointEndWith()).append(">");
				stringBuilder.append(endpoint.toXmlValue());
				stringBuilder.append("</").append(key).append(endpoint.getEndpointEndWith()).append(">");
			}
		}
		for(DataBean bean:childElementBeans){
			bean.toXmlElement(stringBuilder);
		}
		stringBuilder.append("</").append(elementName).append(">");
	}
	
	/**
	 * 生成Json
	 * @return
	 */
	public String toJson(){
		StringBuilder stringBuilder = new StringBuilder();
		toJson(stringBuilder);
		return stringBuilder.toString();
	}
	
	private void toJson(StringBuilder stringBuilder){
		stringBuilder.append("{").append("\"").append(elementName).append("\":[");
		if(!this.endpointElementMap.isEmpty()){
			stringBuilder.append("{");
			int i = 0 ;
			for(String key:endpointElementMap.keySet()){
				if(i++>0)stringBuilder.append(",");
				List<IEndpoint> endpoints = endpointElementMap.get(key);
				int k = 0;
				stringBuilder.append("\"").append(key).append(endpoints.get(0).getEndpointEndWith()).append("\":");
				if(endpoints.size()>1)
					stringBuilder.append("[");
				for(IEndpoint endpoint : endpoints){
					if(k++>0)stringBuilder.append(",");
					stringBuilder.append(endpoint.toJsonValue());
				}
				if(endpoints.size()>1)
					stringBuilder.append("]");
			}
			stringBuilder.append("}");
			if(!childElementBeans.isEmpty()){
				stringBuilder.append(",");
			}
		}
		
		if(!childElementBeans.isEmpty()){
			int i = 0;
			for(DataBean bean:childElementBeans){
				if(bean==null)continue;
				if(i++>0)stringBuilder.append(",");
				bean.toJson(stringBuilder);
			}
		}
		
		stringBuilder.append("]}");
	}

}
