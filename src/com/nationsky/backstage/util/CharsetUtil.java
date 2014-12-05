/**
 * 
 */
package com.nationsky.backstage.util;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class CharsetUtil {

	/**
	 * 获得JDK默认编码
	 * @return
	 */
	public static String getDefaultCharset(){
		return Charset.defaultCharset().toString();
	}
	/**
	 * 获得远程URL文件的编码格式
	 * @param url
	 * @return
	 */
	public static String getReomoteURLFileEncode(URL url) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return ConvertUtil.UTF_8;
		}
	} 

	/**
	 * 获得文件流的编码格式
	 * @param is
	 * @return
	 */
	public static String getInputStreamEncode(InputStream is) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(is, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return ConvertUtil.UTF_8;
		}
	} 

	/**
	 * 获得本地文件的编码格式
	 * @param file
	 * @return
	 */
	public static String getLocalteFileEncode(File file) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(file.toURI().toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return ConvertUtil.UTF_8;
		}
	}
	
	/**
	 * 获得字符串的编码格式
	 * @param str
	 * @return
	 */
	public static String getStringEncode(String str) {
		return getEncode(str.getBytes());
	} 

	/**
	 * 获得字符串的编码格式
	 * @param str
	 * @return
	 */
	public static String getEncode(byte[] _byte) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		InputStream myIn=new ByteArrayInputStream(_byte);
		try {
			charset = detector.detectCodepage(myIn,8);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return ConvertUtil.UTF_8;
		}
	}
	
	public static void main(String[] args) {
		//String url = "http://newcar.xcar.com.cn/pub_js/car_arr_newcar_2009_ps.js";
//		String a = HttpUtil.get(url,"gb2312");
//		System.out.println(a);
//		String b = getEncode(a.getBytes());
//		System.out.println(b);
//		try {
//			String aa=new String(a.getBytes("gbk"),"iso-8859-1");
//			FileUtil.writeFile("d:/a/a.txt", a, "utf-8");
//			System.out.println(aa);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
		try {

			String strUrl = "http://newcar.xcar.com.cn/pub_js/car_arr_newcar_2009_ps.js";
			URL url = new URL(strUrl);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setConnectTimeout(5000);
			urlCon.setReadTimeout(5000);
			InputStream in = urlCon.getInputStream();

			OutputStream out = new FileOutputStream("D:\\out.txt");
			int current=in.read();
			while(current!=-1){
				out.write(current);
				current=in.read();
			}

			in.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		



		
		
	}
}
