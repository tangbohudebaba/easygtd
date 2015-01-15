<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc/top.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<title>图片上传测试</title>
</head>
<body>
	<form action="../v1/user/updateHeadPortraitImg.ac" method="post"
		enctype="multipart/form-data">
		<input type="file" id="fileToUpload" name="head">
		<input name="userId" value="3"/>
		<button type="submit">Submit</button>
	</form>
</body>
</html>