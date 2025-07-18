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
		padding: 0;
		margin: 0;
	}

	.error500 {
		height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        text-align:center;
	}
	
	.error500 h2 {
		color: #333;
		line-height: 1.5;
		font-size: 1.3rem;
	}	
	
	.error500 p {
		color: #666;
		line-height: 1.2;
	    font-size: 1.0rem;
	    margin-bottom: 20px;
	}
	
	.error500 img {
		width: 600px;
 		height: auto; 
	}
</style>
</head>
<body>
	<div class="error500">
		<div>
			<h2>일시적인 오류가 발생했습니다.</h2>
			<p>죄송합니다. 현재 서버에 문제가 발생하여 페이지를 불러올 수 없습니다.</p>
			<p>일시적인 오류일 수 있으니, 잠시 후 다시 시도해 주세요.</p>
			<a href="/" style="color: blue; text-decoration: underline;">홈으로 돌아가기</a>
		</div>
		<img alt="error500" src="<c:url value="/resources/images/error500.png"/>">
	</div>
</body>
</html>