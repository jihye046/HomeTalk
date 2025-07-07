<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/password-reset.css"/>" rel="stylesheet">
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<main>
		<div class="password-reset-background">
            <div class="password-reset-card">
                <h2 class="password-reset-title">비밀번호 변경</h2>
                
                <form action="/user/password-reset" id="password-reset-form" method="post" class="password-reset-form">
                    <input type="hidden" name="verifyUserId" value="${verifyUserId}">
                    <div class="input-group">
                        <label for="newPassword">새 비밀번호</label>
                        <input type="password" id="newPassword" name="newPassword" placeholder="새 비밀번호를 입력해주세요 (8~16자)">
                        <p id="passwordRequirement" class="requirement-message">영문, 숫자, 특수문자 조합 8~16자</p>
                    </div>
                    
                    <div class="input-group">
                        <label for="confirmPassword">새 비밀번호 확인</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호를 다시 입력해주세요">
                        <p id="passwordMismatchMessage" class="error-message"></p>
                    </div>
                    
                    <button type="submit" id="resetPasswordButton" class="btn-submit" disabled>비밀번호 변경</button>
                    
                    <div class="form-links">
                        <a href="/user/loginPage">로그인 페이지로 돌아가기</a>
                    </div>
                </form>
            </div>
        </div>
	</main>
</body>
<script src="<c:url value="/resources/js/password-reset.js"/>"></script>
</html>