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
        text-align:center;
	}
	
	.error404 h2 {
		color: #333;
		line-height: 1.5;
		font-size: 1.5rem;
	}	
	
	.error404 p {
		color: #666;
		line-height: 1.2;
	    font-size: 1.2rem;
	    margin-bottom: 20px;
	}
	
	.error404 img {
		width: 700px;
        height: auto;
	}
</style>
</head>
<body>
	<div class="error404">
		<div>
			<h2>페이지를 찾을 수 없습니다.</h2>
			<p>죄송합니다. 페이지를 찾을 수 없습니다.</p>
			<p>주소가 잘못되었거나, 삭제되었을 수 있습니다.</p>
			<a href="/" style="color: blue; text-decoration: underline;">홈으로 돌아가기</a>
		</div>
		<img alt="error404" src="<c:url value="/resources/images/error404.png"/>">
	</div>
</body>
</html>