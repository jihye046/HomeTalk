<% response.setStatus(200); %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>error404.jsp</title>
<style>
	body {
		padding: 0;
		margin: 0;
	}

	.error404 {
		height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
	}
	
	.error404 img {
		width: 800px;
        height: 800px;
	}
</style>
</head>
<body>
	<div class="error404">
		<img alt="error404" src="<c:url value="/resources/images/error404.png"/>">
	</div>
</body>
</html>