/**
 * 
 */
package com.nationsky.backstage.util.dynamic;

import java.io.File;

import com.nationsky.backstage.util.FileUtil;

/**
 * 动态加载class文件
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DynamicClassLoader extends ClassLoader {
	
	public DynamicClassLoader(ClassLoader parent){
		super(parent);
	}
	/**
	 * 动态加载类
	 * @param clazzFile 类文件
	 * @param prefex 包所在根文件
	 * @return
	 */
	public Class<?> loadClass(File clazzFile,File prefixDir){
		String clazzName = FileUtil.replaceSeparator(FileUtil.removeExtension(FileUtil.getFileName(clazzFile, prefixDir)),".");
		return loadClass(clazzName,clazzFile);
	}
	
	/**
	 * 动态加载类
	 * @param clazzName 类名
	 * @param clazzFile 类文件
	 * @return
	 */
	public Class<?> loadClass(String clazzName,File clazzFile){
		return loadClass(clazzName,FileUtil.readFileStream(clazzFile));
	}
	
	/**
	 * 动态加载类
	 * @param clazzName 类名
	 * @param clazzData 类的字节码二级制数组
	 * @return
	 */
	public Class<?> loadClass(String clazzName,byte[] clazzData){
		try{
			return defineClass(clazzName, clazzData, 0, clazzData.length);
		}catch(Throwable t){
			t.printStackTrace();
		}
		return null;
	}

}
