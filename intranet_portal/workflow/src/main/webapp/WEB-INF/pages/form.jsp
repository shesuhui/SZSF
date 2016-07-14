<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form method="post" action="${ctx}/sync/syncuser.do">
		<p>
			用户名:<input id="username" name="userName" type="text"></input>
		</p>
		<p>
			编码:<input id="code" name="code" type="text"></input>
		</p>
		<p>
			密码: <input id="password" name="password" type="password"></input>
		</p>
		<p><button type="submit">登录</button></p>

		<p>同步完成的用户名: ${user.userName}</p>
		<p>用户编码:${user.code}</p>
	</form>

</body>
</html>