<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/changePassword.css"/>" rel="stylesheet">
</head>
<body>
	<div class="row header-div" style="background-color: #EAEAEA">
		<div class="col-md-12 header-logo">
			<a href="/">
				<img alt="logo" src="<c:url value="/resources/images/logo2.png"/>">
			</a>
		</div>
	</div>
	<main>
		<div class="password-background">
			<div class="password-container">
				<div class="password-form">
					<div class="form-header">
						<span class="profile-title">비밀번호 변경</span>
					</div>
					<div class="form-body">
						<div class="profile-section">
							<div class="input-group">
								<label for="oldPassword">현재 비밀번호</label> 
								<input type="password" id="oldPassword" placeholder="현재 비밀번호" required>
                            	<p class="error-message" id="oldPasswordMessage"></p>
							</div>
							
							<div class="input-group">
								<label for="newPassword">새 비밀번호</label>
								<input type="password" id="newPassword" placeholder="새 비밀번호" required>
								<p class="requirement-message" id="newPasswordRequirement"></p>
							</div>
							
							<div class="input-group">
								<label for="confirmPassword">새 비밀번호 확인</label>
								<input type="password" id="confirmPassword" name="userPw" placeholder="새 비밀번호 확인" required>
								<p class="error-message" id="confirmPasswordMismatch"></p>
							</div>
						</div>
					</div>
					<div class="form-footer">
						<button id="changePasswordBtn" class="btn btn-block btn-outline-secondary" style="width: 200px;">확인</button>
					</div>
				</div>
				<div class="password-message" id="passwordMessage"></div>
			</div>
		</div>
	</main>
</body>
<script src="<c:url value="/resources/js/changePassword.js"/>"></script>
</html>