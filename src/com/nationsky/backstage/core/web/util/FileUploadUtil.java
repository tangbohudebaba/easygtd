/**
 * 
 */
package com.nationsky.backstage.core.web.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：文件上传工具类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class FileUploadUtil {
	
	/**
	 * 设置最多只允许在内存中存储的数据,单位:字节sizeThreshold 
	 */
	private static final int _THRESHOLD_SIZE = 4096;
	private static final int _MAX_UPLOAD_SIZE = 10485760;
	private static final File _TEMPORARY_FILE = new File(Configuration.TEMPORARY_PATH);
	

	/**
	 * 上传文件列表到指定目录
	 * @param distDir
	 * @param request
	 * @param allowExts
	 * @return
	 */
	public static boolean upload(String distDir,HttpServletRequest request,List<String> allowExts){
		
		if(!ServletFileUpload.isMultipartContent(request)) return false;
		//如果没有上传目的目录，则创建它
		if(!(new File(distDir).isDirectory())){
            try{
                new File(distDir).mkdirs();
            }catch(SecurityException e){
            	e.printStackTrace();
            }
        }
		
		//上传项目只要足够小，就应该保留在内存里。 
        //较大的项目应该被写在硬盘的临时文件上。 
        //非常大的上传请求应该避免。 
        //限制项目在内存中所占的空间，限制最大的上传请求，并且设定临时文件的位置。      
        //设置最多只允许在内存中存储的数据,单位:字节
        
		DiskFileItemFactory  factory = new DiskFileItemFactory();
		factory.setSizeThreshold(FileUploadUtil._THRESHOLD_SIZE);
		//设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(_TEMPORARY_FILE);
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//设置允许用户上传文件大小,单位:字节
        //10M 
		sfu.setSizeMax(_MAX_UPLOAD_SIZE);
		
		try {
			FileItemIterator fii = sfu.getItemIterator(request);
			while(fii.hasNext()){
				FileItemStream item = fii.next();
				if(!item.isFormField()){
					String name = item.getName();
					if(ValidateUtil.isNull(name))continue;
					String filename = FileUtil.getFileName(name);
					String ext = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
					if(!allowExts.contains(ext))continue;
					InputStream is = item.openStream();
					FileUtil.writeFile(new File(StringUtil.concat(distDir,File.separator,filename)), Streams.asString(is,Configuration.getEncoding()), Configuration.getEncoding());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	

	/**
	 * 上传一个文件列表到指定目录
	 * @param distDir
	 * @param customFilename 自定义不带扩展名的文件名
	 * @param request
	 * @param allowExts
	 * @return
	 */
	public static String upload(String distDir,String customFilename, HttpServletRequest request,List<String> allowExts){
		
		if(!ServletFileUpload.isMultipartContent(request)) return null;
		//如果没有上传目的目录，则创建它
		if(!(new File(distDir).isDirectory())){
            try{
                new File(distDir).mkdirs();
            }catch(SecurityException e){
            	e.printStackTrace();
            }
        }
		
		//上传项目只要足够小，就应该保留在内存里。 
        //较大的项目应该被写在硬盘的临时文件上。 
        //非常大的上传请求应该避免。 
        //限制项目在内存中所占的空间，限制最大的上传请求，并且设定临时文件的位置。      
        //设置最多只允许在内存中存储的数据,单位:字节
        
		DiskFileItemFactory  factory = new DiskFileItemFactory();
		factory.setSizeThreshold(FileUploadUtil._THRESHOLD_SIZE);
		//设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(_TEMPORARY_FILE);
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//设置允许用户上传文件大小,单位:字节
        //10M 
		sfu.setSizeMax(_MAX_UPLOAD_SIZE);
		
		String saveFileName = null;
		try {
			FileItemIterator fii = sfu.getItemIterator(request);
			while(fii.hasNext()){
				FileItemStream item = fii.next();
				if(!item.isFormField()){
					String name = item.getName();
					if(ValidateUtil.isNull(name))continue;
					String filename = FileUtil.getFileName(name);
					String ext = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
					if(!allowExts.contains(ext))continue;
					InputStream is = item.openStream();
					saveFileName = StringUtil.concat(distDir,File.separator,customFilename+"."+ext);
					FileUtil.writeFile(new File(saveFileName), Streams.asString(is,Configuration.getEncoding()), Configuration.getEncoding());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return saveFileName;
	}
	
	/**
	 * 保存上传文件
	 * @param fieldName 字段名
	 * @param distDir 目标目录
	 * @param distFilename 目标文件名不包含扩展名
	 * @param request
	 * @param allowExts 可上传的扩展名不包含点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean upload(String fieldName ,String distDir, String distFilename,HttpServletRequest request,List<String> allowExts){
		
		if(!ServletFileUpload.isMultipartContent(request)) return false;
		//如果没有上传目的目录，则创建它
		if(!(new File(distDir).isDirectory())){
            try{
                new File(distDir).mkdirs();
            }catch(SecurityException e){
            	e.printStackTrace();
            }
        }
		
		//上传项目只要足够小，就应该保留在内存里。 
        //较大的项目应该被写在硬盘的临时文件上。 
        //非常大的上传请求应该避免。 
        //限制项目在内存中所占的空间，限制最大的上传请求，并且设定临时文件的位置。      
        //设置最多只允许在内存中存储的数据,单位:字节
        
		DiskFileItemFactory  factory = new DiskFileItemFactory();
		factory.setSizeThreshold(FileUploadUtil._THRESHOLD_SIZE);
		//设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(_TEMPORARY_FILE);
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//设置允许用户上传文件大小,单位:字节
        //10M 
		sfu.setSizeMax(_MAX_UPLOAD_SIZE);
		
		try {
			List<FileItem> items = sfu.parseRequest(request);
			boolean saved = false;
			for(FileItem item : items){
				if(!item.isFormField() && (ValidateUtil.isNotNull(fieldName) && ValidateUtil.isEquals(item.getFieldName(), fieldName))){
					String name = item.getName();
					if(ValidateUtil.isNull(name))continue;
					String filename = FileUtil.getFileName(name);
					String ext = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
					if(!allowExts.contains(ext))continue;
					if(ValidateUtil.isNotNull(distFilename))filename = StringUtil.concat(distFilename,".",ext);
					item.write(new File(StringUtil.concat(distDir,File.separator,filename)));
					saved = true;
				}
			}
			return saved;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
