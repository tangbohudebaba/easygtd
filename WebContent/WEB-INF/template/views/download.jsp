<%@ page contentType="text/html;charset=UTF-8"%><%@include file="inc/top.inc.jsp"%>
<%
	String encryptFilename = request.getParameter("file");
String targetFileName = null;
java.io.File file = null;
String str = com.nsc.meap.util.HashUtil.decrypt(com.nsc.meap.Configuration.getSecurityKey(),encryptFilename);
java.util.List<String> list = com.nsc.meap.util.StringUtil.tokenize(str,"|");
String filePath = null;
String fileName = null;
if(list.size()>0)
	filePath = list.get(0);
if(list.size()>1)
	fileName = list.get(1);
file = com.nsc.meap.util.ValidateUtil.isNull(filePath)?null:new java.io.File(filePath);
targetFileName = com.nsc.meap.util.ValidateUtil.isNull(fileName)?com.nsc.meap.util.FileUtil.getFileName(filePath):fileName;
response.setContentType("application/octet-stream");

if(file == null || !file.exists()){
	request.setAttribute("ERROR","file not exists");
	response.sendError(501,"file not exists");
	return;
}
 
java.io.RandomAccessFile raf = null;
try{
	raf = new java.io.RandomAccessFile(file, "r");
	response.setHeader("Server", "www.baidu.com");
	response.setHeader("Accept-Ranges", "bytes");
	long pos = 0;
	long len;
	len = raf.length();
	if (request.getHeader("Range") != null) {
		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);   
		String range = request.getHeader("Range").replaceAll("bytes=", "");
		java.util.StringTokenizer st = new java.util.StringTokenizer(range,"-");
		if(st.hasMoreTokens()){
	pos = Long.parseLong(st.nextToken());
		}			
	}
	response.setHeader("Content-Length", Long.toString(len - pos));
	if (pos != 0) {
		response.setHeader("Content-Range", new StringBuffer()
		.append("bytes ")
		.append(pos)
		.append("-")
		.append(Long.toString(len - 1))
		.append("/")
		.append(len)
		.toString()
		);
	}
	response.setCharacterEncoding("utf8");
	response.setHeader("Content-Disposition", new StringBuffer()   
	.append("attachment;filename=\"")
	.append(com.nationsky.backstage.core.web.util.WebUtil.urlEncoding(targetFileName,"utf-8"))
	.append("\"").toString());
	raf.seek(pos);
	byte[] b = new byte[1*1024];//姣忔璇诲彇1M
	int i;
	while ((i = raf.read(b)) != -1) {
		response.getOutputStream().write(b, 0, i);
	}
}catch(java.io.IOException ioe){
	//System.out.println(ioe.getMessage());
}finally{
	try{
		if(raf!=null)raf.close();
		out.clear();
		out = pageContext.pushBody();
	}catch(Exception e){
		response.sendError(501,"file not exists");
		return;
	}
}
%>