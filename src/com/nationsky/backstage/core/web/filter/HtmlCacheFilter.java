/**
 * 
 */
package com.nationsky.backstage.core.web.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nationsky.backstage.Configuration;
import com.nationsky.backstage.core.Constants;
import com.nationsky.backstage.core.web.filter.helper.HtmlResponseWrapper;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：处理动态转静态页面
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class HtmlCacheFilter implements Filter {

	private String export;
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		boolean update =  ValidateUtil.isEqualsIgnoreCase("refresh", request.getParameter("s_opt"));
		boolean preview = ValidateUtil.isEqualsIgnoreCase("preview", request.getParameter("s_opt"));
		boolean delete = ValidateUtil.isEqualsIgnoreCase("delete", request.getParameter("s_opt"));
		
		if(preview){
			chain.doFilter(arg0, arg1);
		}else{
			String path = request.getServletPath();
			if(ValidateUtil.isNull(path))return ;
			String fp = path.substring(1).replaceAll("/",File.separator.equals("\\")?"\\\\":File.separator);
			File file = new File(this.export.concat(fp));
			if(file.exists() && delete){
				file.delete();
				return;
			}
			if(!file.exists() || update){
				HtmlResponseWrapper sr = new HtmlResponseWrapper(response);
				chain.doFilter(request,sr);
				if(sr.getStatus() == HttpServletResponse.SC_OK){
					if(fp.lastIndexOf(File.separator)>0){
						String dirText = fp.substring(0,fp.lastIndexOf(File.separator));
						File dir = new File(this.export.concat(dirText));
						if(!dir.exists()){
							dir.mkdirs();
						}
					}
					String text = StringUtil.getTrim(StringUtil.removeExtraBlanks(sr.getResponseText()));
					Object persist = request.getAttribute(Constants.WEB_PAGE_PERSIST);
					if(persist==null || ValidateUtil.isEqualsIgnoreCase("true", persist.toString())){
						//当页面没有设置是否持久或者设置需要持久时,持久该页面
						FileUtil.writeFile(file, StringUtil.getTrim(text) , Configuration.getEncoding());
					}
	                response.setContentType(Configuration.getContentType());
					byte[] textByte = ValidateUtil.isNotNull(text)?text.getBytes(Configuration.getEncoding()):new byte[0];
	                response.setContentLength(textByte.length);
	                response.getOutputStream().write(textByte);
				}
			}else{
				response.setContentType(Configuration.getContentType());
				response.setContentLength((int)file.length());
	            FileUtil.readFile(file, response.getOutputStream());
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		String dir = arg0.getInitParameter("export");
		if(ValidateUtil.isNotNull(dir)){
			this.export = arg0.getServletContext().getRealPath(dir);
		}else{
			this.export = arg0.getServletContext().getRealPath("/htdocs");
		}
		this.export+=File.separator;
		File e = new File(this.export);
		if(!e.exists())e.mkdirs();
	}

}
