<%@ page contentType="text/html;charset=UTF-8"%><%@include file="inc/top.inc.jsp"%>
<c:set var="url" value="${url==null?path:url}"/>
<%
response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
response.addHeader("Location",(String)pageContext.getAttribute("url"));
%>