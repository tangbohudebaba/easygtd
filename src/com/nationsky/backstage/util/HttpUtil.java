package com.nationsky.backstage.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;

import com.nationsky.backstage.Configuration;

/**
 * 功能：提供HTTP方面的通用类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class HttpUtil {
	
	/** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManagerUtil() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
            httpUrlConn.connect();  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes(ConvertUtil.UTF_8));  
                outputStream.close();  
            }  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, ConvertUtil.UTF_8);  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
        } catch (ConnectException ce) {  
        } catch (Exception e) {  
        }finally{
        	
        }
        return buffer.toString();  
    }  
	
	/**
	 * 通过post
	 * @param urlStr 访问地址
	 * @param queryString 参数
	 * @return
	 */
	public static String getResult(String urlStr, String queryString) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(queryString);
			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ConvertUtil.UTF_8));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	/**
	 * 通过GET方法获得
	 * @param urlStr
	 * @return
	 */
	public static String getResult(String urlStr){
		assert(ValidateUtil.isNotNull(urlStr));
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ConvertUtil.UTF_8));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	/**
	 * 通过GET方法获得
	 * @param urlStr
	 * @param charsetName 编码
	 * @return
	 */
	public static String get(String urlStr){
		return get(urlStr, null, null);
	}
	
	/**
	 * 通过GET方法获得
	 * @param urlStr
	 * @param charsetName 编码
	 * @return
	 */
	public static String get(String urlStr, String charsetName){
		return get(urlStr, charsetName, null);
	}
	
	/**
	 * 通过GET方法获得
	 * @param urlStr
	 * @param charsetName 编码
	 * @param cookieStr cookie字符串
	 * @return
	 */
	public static String get(String urlStr, String charsetName, String cookieStr){
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			if(cookieStr != null && cookieStr.length() > 0){
				connection.setRequestProperty("Cookie", cookieStr);
			}
			connection.connect();
			BufferedReader reader = null;
			if(charsetName != null){
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charsetName));
			}else{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	/**
	 * 通过GET方法获得Zip流
	 * @param url
	 * @param queryString
	 * @param encode
	 * @return
	 */
	public static String getZip(String url,String queryString,String encode) {
		String response = null;
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (StringUtils.isNotBlank(queryString)) {
				method.setQueryString(URIUtil.encodeQuery(queryString));
			}
		    client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();     
			    GZIPInputStream gzip = new GZIPInputStream(in);
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();			    
			    byte[] buf = new byte[1024];
			    int n;
			    while ((n = gzip.read(buf)) != -1) {
			        baos.write(buf, 0, n);
			    }	
			    gzip.close();
			    in.close();
			    response = new String(baos.toByteArray(),encode);			
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response;
	}
	
	
	/**
	 * 地址转码
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String getURLEncode(String str,String... charset){
		if(ValidateUtil.isNull(str))return null;
		String result = null;
		try {
			result = java.net.URLEncoder.encode(str, ValidateUtil.isNullArray(charset)?Configuration.getEncoding():charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 地址转码
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String getURLDecode(String str,String... charset){
		if(ValidateUtil.isNull(str))return null;
		String result = null;
		try {
			result = java.net.URLDecoder.decode(str, ValidateUtil.isNullArray(charset)?Configuration.getEncoding():charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
