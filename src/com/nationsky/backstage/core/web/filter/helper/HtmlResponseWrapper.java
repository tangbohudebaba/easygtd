package com.nationsky.backstage.core.web.filter.helper;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 功能：静态页面响应封装
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class HtmlResponseWrapper extends HttpServletResponseWrapper {

	protected CharArrayWriter charWriter;
    protected PrintWriter writer;
    protected boolean getOutputStreamCalled;
    protected boolean getWriterCalled;
    private int status = SC_OK;
    public HtmlResponseWrapper(HttpServletResponse response){
        super(response);
        // Create the writer
        charWriter = new CharArrayWriter();
    }
    public ServletOutputStream getOutputStream()throws IOException{
        if (getWriterCalled)
        {
            throw new IllegalStateException(
                    "getWriter already called");
        }
        getOutputStreamCalled = true;
        return super.getOutputStream();
    }
    public PrintWriter getWriter()throws IOException{
        if (writer != null)
        {
            return writer;
        }
        // Can't call getWriter if getOutputStream
        // has already been called
        if (getOutputStreamCalled)
        {
            throw new IllegalStateException(
                    "getOutputStream already called");
        }
        getWriterCalled = true;
        writer = new PrintWriter(charWriter);
        return writer;
    }
    
    public String getResponseText(){
    	return this.toString();
    }
    
    public String toString(){
        String s = "";
        // Only return a String if the writer was
        // used.
        if (writer != null)
        {
            s = charWriter.toString();
        }
        return s;
    }
   
    public char[] toCharArray(){//将响应数据以字符数组返回
         return(charWriter.toCharArray());
    }
   
    public int getStatus() { return status; }
    
    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }

    public void setStatus(int status, String string) {
        //super.setStatus(status, string);
        this.status = status;
    }

    public void sendError(int status, String string) throws IOException {
        super.sendError(status, string);
        this.status = status;
    }

    public void sendError(int status) throws IOException {
        super.sendError(status);
        this.status = status;
    }

    public void sendRedirect(String location) throws IOException {
        super.sendRedirect(location);
        this.status = SC_MOVED_TEMPORARILY;
    }

}
