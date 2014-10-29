package com.nationsky.backstage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.nationsky.backstage.Configuration;
/**
 * 
 * 功能：zip压缩通用类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class ZipUtil{
	
	/**
	 * 压缩字符串
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String compress(String str) {
		if(ValidateUtil.isNull(str)){
			return str;
		}
	    try {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    	gzip.write(str.getBytes());
		    gzip.close();
		    return out.toString(ConvertUtil.ISO_8859_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 解压缩字符串
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String uncompress(String str) {
		if(ValidateUtil.isNull(str)){
			return str;
		}
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(ConvertUtil.ISO_8859_1));
		    GZIPInputStream gunzip = new GZIPInputStream(in);
		    byte[] buffer = new byte[256];
		    int n;
		    while ((n = gunzip.read(buffer)) >= 0) {
		      out.write(buffer, 0, n);
		    }
		    return out.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 解压缩zip
	 * @param 要解压缩的文件列表
	 * @param 解压缩到的路径
	 * @throws IOException
	 */
	public static void unzip(String[] paramArrayOfString, String paramString)throws IOException{
		if (paramArrayOfString == null) return;
	    ZipInputStream localZipInputStream = null;
	    FileInputStream localFileInputStream = null;
	    CheckedInputStream localCheckedInputStream = null;
	    byte[] arrayOfByte = new byte[1024];
	    FileOutputStream localFileOutputStream = null;
	    try
	    {
	      File localFile1 = new File(paramString);
	      for (int i = 0; i < paramArrayOfString.length; ++i)
	      {
	        localFileInputStream = new FileInputStream(paramArrayOfString[i]);
	        localCheckedInputStream = new CheckedInputStream(localFileInputStream, new Adler32());
	        localZipInputStream = new ZipInputStream(localCheckedInputStream);
	        ZipEntry localZipEntry = null;
	        while ((localZipEntry = localZipInputStream.getNextEntry()) != null)
	        {
		        File localFile2 = new File(localFile1, localZipEntry.getName());
		        if (localZipEntry.isDirectory()){
		        	if (!(localFile2.mkdirs()))
		        		localFileOutputStream.close();
		        	throw new IllegalArgumentException("Can't make dir:" + localFile2);
		        }
		        if (!(localFile2.getParentFile().exists()))
		            localFile2.getParentFile().mkdirs();
		        localFileOutputStream = new FileOutputStream(localFile2);
		        int j = 0;
		        while ((j = localZipInputStream.read(arrayOfByte)) != -1)
		        	localFileOutputStream.write(arrayOfByte, 0, j);
	        }
	        localFileOutputStream.close();
	      }
	    }
	    finally
	    {
	      try{
	        if (localFileOutputStream != null)
	          localFileOutputStream.close();
	      }catch (Exception localException1){
	      }
	      try{
	        if (localZipInputStream != null)
	          localZipInputStream.close();
	        if (localCheckedInputStream != null)
	          localCheckedInputStream.close();
	        if(localFileInputStream!=null)
	        	localFileInputStream.close();
	      }catch (Exception localException2){
	        localException2.printStackTrace();
	      }
	    }
	}
	/**
	 * zip压缩
	 * @param 压缩后的地址
	 * @param 要压缩的路径
	 * @param 压缩路径下面的文件名
	 */
	public static void zip(String zipfilepath, String filedirfile, String[] filenames) {
		zip(zipfilepath,filedirfile,filenames,Configuration.getEncoding());
	}
	/**
	 * zip压缩
	 * @param 压缩后的地址
	 * @param 要压缩的路径
	 * @param 压缩路径下面的文件名
	 * @param 编码
	 */
	public static void zip(String zipfilepath, String filedirfile, String[] filenames,String encoding){
		org.apache.tools.zip.ZipOutputStream localZipOutputStream = null;
		FileOutputStream localFileOutputStream = null;
		CheckedOutputStream localCheckedOutputStream = null;
		byte[] arrayOfByte = new byte[1024];
		try{
			localFileOutputStream = new FileOutputStream(zipfilepath);
		    localCheckedOutputStream = new CheckedOutputStream(localFileOutputStream, new CRC32());
		    localZipOutputStream = new org.apache.tools.zip.ZipOutputStream(localCheckedOutputStream);
		    localZipOutputStream.setLevel(9);
		    localZipOutputStream.setEncoding(encoding);
		    for (int i = 0; i < filenames.length; ++i)
		        zip(localZipOutputStream, arrayOfByte, filedirfile, filenames[i]);
		}catch (IOException localIOException){
			localIOException.printStackTrace();
		}finally{
			try{
				if (localZipOutputStream != null)
		          localZipOutputStream.close();
		        if (localCheckedOutputStream != null)
		          localCheckedOutputStream.close();
		        if(localFileOutputStream!=null)
		        	localFileOutputStream.close();
		    }catch (Exception localException){
		    	  localException.printStackTrace();		        
		    }
		}
	}
	
	/**
	 * zip压缩流
	 * @param out 压缩输出流
	 * @param 要压缩的路径
	 * @param 压缩路径下面的文件名
	 * @param 编码
	 */
	public static void zipStream(OutputStream out,String filedirfile, String[] filenames,String encoding){
		org.apache.tools.zip.ZipOutputStream localZipOutputStream = null;
		CheckedOutputStream localCheckedOutputStream = null;
		byte[] arrayOfByte = new byte[1024];
		try{
			localCheckedOutputStream = new CheckedOutputStream(out, new CRC32());
			localZipOutputStream = new org.apache.tools.zip.ZipOutputStream(localCheckedOutputStream);
		    localZipOutputStream.setEncoding(encoding);
		    localZipOutputStream.setLevel(9);
		    for (int i = 0; i < filenames.length; ++i)
		        zip(localZipOutputStream, arrayOfByte, filedirfile, filenames[i]);
		}catch (IOException localIOException){
			localIOException.printStackTrace();
		}finally{
			try{
				if (localZipOutputStream != null)
		          localZipOutputStream.finish();
				if (localCheckedOutputStream != null)
			          localCheckedOutputStream.close();
		    }catch (Exception localException){
		    	  localException.printStackTrace();		        
		    }
		}
	}

	private static void zip(org.apache.tools.zip.ZipOutputStream paramZipOutputStream, byte[] paramArrayOfByte, String filedir, String filename)throws IOException{
		Object localObject;
		File localFile = new File(filedir + "/" + filename);
		if (!(localFile.exists()))
		   return;
		if (localFile.isDirectory()){
			localObject = localFile.list();
			if (localObject != null)
		        for (int i = 0; i < ((Object[])localObject).length; ++i)
		          zip(paramZipOutputStream, paramArrayOfByte, filedir, filename + "/" + ((Object[])localObject)[i]);
		}else{
			localObject = new org.apache.tools.zip.ZipEntry(filename);
			paramZipOutputStream.putNextEntry((org.apache.tools.zip.ZipEntry)localObject);
			FileInputStream localFileInputStream = new FileInputStream(localFile);
			int j;
			while ((j = localFileInputStream.read(paramArrayOfByte)) > -1)
			{
				paramZipOutputStream.write(paramArrayOfByte, 0, j);
			}
			localFileInputStream.close();
			paramZipOutputStream.flush();
		}
	}
	  
}