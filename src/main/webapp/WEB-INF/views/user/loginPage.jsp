<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/loginPage.css"/>" rel="stylesheet">
</head>
<body>
	<main>
		<div class="login-background">
			<div class="login-form">
				<form action="<c:url value="/user/login"/>" method="post">
					<a href="/">
						<img class="logo-image" alt="logo-image" src="<c:url value="/resources/images/logo2.png"/>">
					</a>
					<input type="text" value="${empty userId ? '' : userId}" name="userId" placeholder="아이디"><br>
					<input type="password" name="userPw" placeholder="비밀번호"><br>
					<button class="btn btn-block btn-outline-secondary" type="submit">Sign In</button>
					<div class="login-form-links">
							<a href="/user/joinPage">회원가입</a>
							<a href="/user/verify-user?mode=id">아이디찾기</a>
							<a href="/user/password-userid-input">비밀번호찾기</a>
					</div>
				</form><br>
				<span>다른 계정으로 로그인하기</span>
				<div class="social-button">
					<a href="/social/naverLogin">
						<img class="social-login-logo" id="naverLogin" alt="naverLogin" src="<c:url value="/resources/images/btnG_Squareicon.png"/>">
					</a>
					<a href="/social/googleLogin">
						<img class="social-login-logo" id="googleLogin" alt="googleLogin" src="<c:url value="/resources/images/btn_googleLogo.png"/>">
					</a>
				</div>
			</div>
		</div>
	</main>
</body>
<div class="hidden-data" id="joinResult" data-join-result="${joinResult}"></div>
<div class="hidden-data" id="loginFail" data-loginFail-result="${loginFail}"></div>

<script src="<c:url value="/resources/js/loginPage.js"/>"></script>
</html>
