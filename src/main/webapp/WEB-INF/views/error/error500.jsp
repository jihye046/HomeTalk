<% response.setStatus(200); %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>error500.jsp</title>
<style>
	body {
		pdding: 0;
		margin: 0;
	}

	.error500 {
		height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
	}
	
	.error500 img {
		height: 800px;
	}
</style>
</head>
<body>
	<div>500에러 발생</div>
	<div class="error500">
		<img alt="error500" src="<c:url value="/resources/images/error500.png"/>">
	</div>
</body>
</html>