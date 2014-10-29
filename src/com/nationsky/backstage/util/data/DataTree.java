/**
 * 
 */
package com.nationsky.backstage.util.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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

/**
 * 功能：数据平行结构和树形结构解析
 * 工具：具有统一数据结构工具类作用
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DataTree {
	
	private Map<String,List<DataBean>> indexMap = new HashMap<String,List<DataBean>>();
	
	private DataBean rootDataBean;
	
	private static final Map<String,Class<? extends IEndpoint>> endpointMapper;
	
	public static final String INDEX_SPLIT_CHAR = ".";
	
	private static final char _char0 = '\"';
	private static final char _char1 = '\\';
	private static final char _char2 = '\b';
	private static final char _char3 = '\f';
	private static final char _char4 = '\n';
	private static final char _char5 = '\r';
	private static final char _char6 = '\t';
	
	private static final String[] REPLACE_STRING = {"\\\"","\\\\","\\b","\\f","\\n","\\r","\\t"};
	
	static{
		/**
		 * 初始化所有Endpoint节点
		 */
		endpointMapper = new HashMap<String,Class<? extends IEndpoint>>();
		endpointMapper.put(IntegerEndpoint.END_WITH, IntegerEndpoint.class);
		endpointMapper.put(LongEndpoint.END_WITH, LongEndpoint.class);
		endpointMapper.put(DecimalEndpoint.END_WITH, DecimalEndpoint.class);
		endpointMapper.put(BooleanEndpoint.END_WITH, BooleanEndpoint.class);
		endpointMapper.put(StringEndpoint.END_WITH, StringEndpoint.class);
		endpointMapper.put(TimeEndpoint.END_WITH, TimeEndpoint.class);
		endpointMapper.put(FileEndpoint.END_WITH, FileEndpoint.class);
	}
	
	private DataTree(){}
	
	/**
	 * 通过解析XML字符串来生成DataTree对象
	 * @param xml
	 * @return
	 */
	public static DataTree getByXml(String xml) {
		if(ValidateUtil.isNull(xml))return null;
		try{
			DataTree dataTree = new DataTree();
			dataTree.parseXml(xml);
			return dataTree;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过解析Json字符串来生成DataTree对象
	 * @param json
	 * @return
	 */
	public static DataTree getByJson(String json) {
		if(ValidateUtil.isNull(json))return null;
		try{
			DataTree dataTree = new DataTree();
			dataTree.parseJson(json);
			return dataTree;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 字符串转JSON
	 * @param str
	 * @return
	 */
	public static String string2Json(String str) {
		if(ValidateUtil.isNull(str))
			return null;
		StringBuffer sb = new StringBuffer();
        for (int i=0; i<str.length(); i++) {
        	char c = str.charAt(i);
        	switch (c){
        	 case _char0:
                 sb.append(REPLACE_STRING[0]);
                 break;      
             case _char1:      
                 sb.append(REPLACE_STRING[1]);
                 break;
             case _char2:
                 sb.append(REPLACE_STRING[2]);
                 break;
             case _char3:
                 sb.append(REPLACE_STRING[3]);
                 break;
             case _char4:
                 sb.append(REPLACE_STRING[4]);
                 break;
             case _char5:
                 sb.append(REPLACE_STRING[5]);
                 break;
             case _char6:
                 sb.append(REPLACE_STRING[6]);
                 break;
             default:
                 sb.append(c);
        	 }
        }
        return sb.toString();
	}
	
	/**
	 * 通过路径地址获得DataBean如果是列表返回第一个
	 * @param path
	 * @return
	 */
	public DataBean getDataBean(String path){
		List<DataBean> list = getListDataBean(path);
		if(list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	/**
	 * 通过路径地址获得DataBean列表
	 * 例如:aaa.bbb.ccc节点是Bean的列表
	 * @param path
	 * @return
	 */
	public List<DataBean> getListDataBean(String path){
		return this.indexMap.get(path);
	}
	
	/**
	 * 通过路径地址获得端点值,尝试转换
	 * @param path
	 * @return
	 */
	public <T extends IEndpoint> T getEndpoint( String path, Class<T> clazz){
		IEndpoint endpoint = this.getEndpoint(path);
		return (T)(endpoint!=null?endpoint.getConvert(clazz):null);
	}
	
	/**
	 * 通过路径地址获得端点值
	 * @param path
	 * @return
	 */
	private IEndpoint getEndpoint(String path){
		List<IEndpoint> list = getListEndpoint(path);
		if(list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	/**
	 * 获得节点属性值
	 * @param element
	 * @return
	 */
	public Object getEndpointValue(String path){
		return DataUtil.getInvokeReturn(getEndpoint(path), "getValue");
	}
	
	/**
	 * 通过路径地址获得端点值列表
	 * @param path
	 * @return
	 */
	public List<IEndpoint> getListEndpoint(String path){
		List<IEndpoint> endpoints = new ArrayList<IEndpoint>();
		if(ValidateUtil.isNotNull(path)){
			int lastIndex = path.lastIndexOf(INDEX_SPLIT_CHAR);
			List<DataBean> dataBeanList = getListDataBean(path.substring(0,lastIndex));
			if(dataBeanList!=null && !dataBeanList.isEmpty()){
				for(DataBean bean:dataBeanList){
					List<IEndpoint> eps = bean.getListEndpoint(path.substring(lastIndex+1));
					if(eps!=null){
						endpoints.addAll(eps);
					}
				}
			}
		}
		return endpoints;
	}
	
	/**
	 * 通过路径地址获得端点值列表
	 * @param path
	 * @return
	 */
	public List<?> getListEndpointValue(String path){
		List<IEndpoint> list = this.getListEndpoint(path);
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
	 * 获得根节点Bean
	 * @return
	 */
	public DataBean getRootDataBean() {
		return rootDataBean;
	}
	
	/**
	 * 获得平行索引中的key列表
	 * @return
	 */
	public Collection<String> getIndexMapKeys(){
		return this.indexMap.keySet();
	}
	
	
	private void parseXml(String xml) throws DocumentException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		Document document = DocumentHelper.parseText(xml);
		Element rootElement = document.getRootElement();
		
		DataBean rootDataBean = new DataBean(rootElement.getName());
		this.rootDataBean = rootDataBean;
		
		indexMap.put(rootElement.getName(), DataUtil.getList(rootDataBean));
		
		for(Object obj:rootElement.elements()){	
			this.recursionXmlElement((Element)obj, rootDataBean, indexMap);
		}
	}

	private void recursionXmlElement(Element element, DataBean parentDataBean,
			Map<String, List<DataBean>> indexMap) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String elementName = element.getName();
		boolean isEndpoint = false;
		for(String endWith : endpointMapper.keySet()){
			if(elementName.endsWith(endWith)){
				IEndpoint endpoint = (IEndpoint) endpointMapper.get(endWith).getMethod("buildByXmlElement", Element.class).invoke(endpointMapper.get(endWith), element);
				parentDataBean.putEndpoint(elementName.substring(0, elementName.lastIndexOf(endWith)), endpoint);
				isEndpoint = true;
				break;
			}
		}
		if(!isEndpoint){
			DataBean bean = new DataBean(elementName);
			parentDataBean.getChildElementBeans().add(bean);
			this.putIndexMapBean(element.getPath().substring(1).replace("/", INDEX_SPLIT_CHAR), bean);
			for(Object obj:element.elements()){
				this.recursionXmlElement((Element)obj, bean, indexMap);
			}
		}
	}
	
	private void parseJson(String json) throws JsonParseException, IOException {
		
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jsonParser = jsonFactory.createJsonParser(json);
		
		try{
			
			while(jsonParser.nextToken() != JsonToken.END_OBJECT){
				  if(jsonParser.getCurrentToken() == JsonToken.FIELD_NAME){
					  
					  this.rootDataBean = new DataBean(jsonParser.getCurrentName());
					  
					  JsonDataBean jsonDataBean = JsonDataBean.get(rootDataBean.getElementName(), rootDataBean);
					  this.putIndexMapBean(rootDataBean.getElementName(), rootDataBean);
					  
					  while(jsonParser.nextToken() != JsonToken.END_ARRAY){
						  if(jsonParser.getCurrentToken()==JsonToken.FIELD_NAME){
							  recursionJsonToken(jsonParser,jsonDataBean);
						  }
					  }
				  }
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			jsonParser.close();
		}
	}
	
	private void recursionJsonToken( JsonParser jsonParser, JsonDataBean parentJsonDataBean) throws Exception{
		String elementName = jsonParser.getCurrentName();
		boolean isEndpoint = false;

		for(String endWith : endpointMapper.keySet()){
			if(elementName.endsWith(endWith)){
				if(jsonParser.nextToken() == JsonToken.START_ARRAY){
					//如果是数组开头
					while(jsonParser.nextToken() != JsonToken.END_ARRAY){
						IEndpoint endpoint = (IEndpoint) endpointMapper.get(endWith).getMethod("buildByJsonElement", JsonParser.class).invoke(endpointMapper.get(endWith), jsonParser);
						parentJsonDataBean.dataBean.putEndpoint(elementName.substring(0, elementName.lastIndexOf(endWith)), endpoint);
					}
				}else{
					//如果仅是值
					IEndpoint endpoint = (IEndpoint) endpointMapper.get(endWith).getMethod("buildByJsonElement", JsonParser.class).invoke(endpointMapper.get(endWith), jsonParser);
					parentJsonDataBean.dataBean.putEndpoint(elementName.substring(0, elementName.lastIndexOf(endWith)), endpoint);
				}
				isEndpoint = true;
				break;
			}
		}

		if(!isEndpoint){
			DataBean bean = new DataBean(elementName);
			parentJsonDataBean.dataBean.addChildElementBean(bean);
			
			JsonDataBean jsonDataBean = JsonDataBean.get(bean.getElementName(), bean);
			jsonDataBean.elementPath = StringUtil.concat(parentJsonDataBean.elementPath,INDEX_SPLIT_CHAR,elementName);
			this.putIndexMapBean(jsonDataBean.elementPath, bean);
			
			if(jsonParser.nextToken() != JsonToken.START_ARRAY){
				throw new RuntimeException(" json parse error!!!");
			}
			
			while(jsonParser.nextToken() != JsonToken.END_ARRAY){
				if(jsonParser.getCurrentToken()==JsonToken.FIELD_NAME){
					recursionJsonToken(jsonParser,jsonDataBean);
				}
			}
		}
	}

	private void putIndexMapBean(String elementPath, DataBean dataBean) {
		
		if(this.indexMap.containsKey(elementPath)){
			this.indexMap.get(elementPath).add(dataBean);
		}else{
			this.indexMap.put(elementPath, DataUtil.getList(dataBean));
		}
	}

	private static class JsonDataBean{
		String elementPath;
		DataBean dataBean;
		
		private static JsonDataBean get(String elementPath,DataBean dataBean){
			JsonDataBean me = new JsonDataBean();
			me.elementPath = elementPath;
			me.dataBean = dataBean;
			return me;
		}
	}
}
