<%@ page contentType="text/html;charset=UTF-8"%><%@include file="../inc/top.inc.jsp"%>
<form action="./login.ac" method="post">
<p>${u:getNonNullText(message['error'],'<font color="red">','</font>')}</p>
<p>
用户名:<input name="username"/>${u:getNonNullText(message['username'],'<font color="red">','</font>')}
</p>
<p>
密码:<input type="password" name="password"/>${u:getNonNullText(message['password'],'<font color="red">','</font>')}
</p>
<p>
<button type="submit">登录</button>
</p>
</form>