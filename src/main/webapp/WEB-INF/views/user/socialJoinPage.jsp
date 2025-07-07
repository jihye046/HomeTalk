<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/joinPage.css"/>" rel="stylesheet">
</head>
<body>
	<div class="row header-div" style="background-color: #EAEAEA">
		<div class="col-md-6 header-logo">
			<a href="/">
				<img alt="logo" src="<c:url value="/resources/images/home.png"/>">
			</a>
		</div>
	</div>
	<main>
		<div class="join-background">
			<div class="join-form">
				<form action="/social/join" method="post">
					<input type="hidden" name="sns_email" value="${socialDto.sns_email}">
					<input type="hidden" name="sns_id" value="${socialDto.sns_id}">
					<input type="hidden" name="sns_profile" value="${socialDto.sns_profile}">
					<input type="hidden" name="sns_name" value="${socialDto.sns_name}">
					<input type="hidden" name="sns_type" value="${socialDto.sns_type}">
					<div>
						<img style="width: 60px;" id="googleLogin" alt="googleLogin" src="<c:url value="/resources/images/btn_googleLogo.png"/>">
					</div>
					<p>추가정보 입력창</p>
					<input type="text" name="sns_name" value="${socialDto.sns_name == '' ? '' : socialDto.sns_name}" 
					 placeholder="${socialDto.sns_name == '' ? 'username' : 'ss'}" required><br>
					<input type="text" name="sns_mobile" placeholder="010-xxxx-xxxx" required><br>
					<input type="text" name="sns_nickName" placeholder="Nickname" required><br>
					
					<button id="joinBtn" class="btn btn-block btn-outline-secondary" type="submit">Join</button>
					<div class="join-form-links">
						<a href="/user/loginPage">돌아가기</a>
					</div>
				</form>
			</div>
		</div>
	</main>
</body>
<script src="<c:url value="/resources/js/joinPage.js"/>"></script>
</html>
