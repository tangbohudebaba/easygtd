/**
 * 
 */
package com.nationsky.backstage.util.data.endpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.dom4j.Element;

import com.nationsky.backstage.util.ConvertUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.data.DataTree;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class FileEndpoint extends AbstractCustomEndpoint {

	public static final String END_WITH = StringUtil.concat(DataTree.INDEX_SPLIT_CHAR,"f");
	
	public static final String FILE_NAME_ENDPOINT = StringUtil.concat("fileName",StringEndpoint.END_WITH);
	public static final String FILE_TYPE_ENDPOINT = StringUtil.concat("fileType",StringEndpoint.END_WITH);
	public static final String FILE_CONTENT_ENDPOINT = StringUtil.concat("fileContent",StringEndpoint.END_WITH);
	
	private FileItem value;
	
	public static FileEndpoint get(String fileName,String fileType,byte[] fileContent){
		FileItem fileItem = new FileItem();
		fileItem.setFileName(fileName);
		fileItem.setFileType(fileType);
		fileItem.setFileContent(fileContent);
		return buildByTarget(fileItem);
	}
	
	public static FileEndpoint buildByTarget(Object fileItem){
		FileEndpoint endpoint = new FileEndpoint();
		endpoint.value = (FileItem)fileItem;
		return endpoint;
	}
	
	public static FileEndpoint buildByXmlElement(Element element) {		
		return get(
				element.elementText(FILE_NAME_ENDPOINT),
				element.elementText(FILE_TYPE_ENDPOINT),
				ConvertUtil.convertDecodeBase64(element.elementText(FILE_CONTENT_ENDPOINT))
		);
	}
	
	public static FileEndpoint buildByJsonElement(JsonParser jsonParser) throws JsonParseException, IOException{
		Map<String,String> map = new HashMap<String,String>();
		while(jsonParser.nextToken() != JsonToken.END_OBJECT){
			if(jsonParser.getCurrentToken()==JsonToken.FIELD_NAME){
				map.put(jsonParser.getCurrentName(), jsonParser.nextTextValue());
			}
		}
		return get(
				map.get(FILE_NAME_ENDPOINT),
				map.get(FILE_TYPE_ENDPOINT),
				ConvertUtil.convertDecodeBase64(map.get(FILE_CONTENT_ENDPOINT))
		);
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.endpoint.IEndpoint#getValue()
	 */
	public FileItem getValue() {
		
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
		
		return StringUtil.concat("<",FILE_NAME_ENDPOINT,">",StringUtil.getCDATAData(value.getFileName()),
				"</",FILE_NAME_ENDPOINT,"><",FILE_TYPE_ENDPOINT,">",StringUtil.getCDATAData(value.getFileType()),
				"</",FILE_TYPE_ENDPOINT,"><",FILE_CONTENT_ENDPOINT,">",StringUtil.getCDATAData(ConvertUtil.convertEncodeBase64(value.getFileContent())),
				"</",FILE_CONTENT_ENDPOINT,">");
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.util.data.IEndpoint#toJson()
	 */
	public String toJsonValue() {
		
		return StringUtil.concat("{\"",FILE_NAME_ENDPOINT,"\":\"",DataTree.string2Json(value.getFileName()),"\"",
				",\"",FILE_TYPE_ENDPOINT,"\":\"",StringUtil.replace(value.getFileType(), "\"", "\\\""),"\"",
				",\"",FILE_CONTENT_ENDPOINT,"\":\"",DataTree.string2Json(ConvertUtil.convertEncodeBase64(value.getFileContent())),"\"}");
	}
	
	public static class FileItem{
		
		private String fileName;
		private String fileType;
		private byte[] fileContent;
		
		public FileItem(){}
		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * @param fileName the fileName to set
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		/**
		 * @return the fileType
		 */
		public String getFileType() {
			return fileType;
		}
		/**
		 * @param fileType the fileType to set
		 */
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		/**
		 * @return the fileContent
		 */
		public byte[] getFileContent() {
			return fileContent;
		}
		/**
		 * @param fileContent the fileContent to set
		 */
		public void setFileContent(byte[] fileContent) {
			this.fileContent = fileContent;
		}

	}
	
}

