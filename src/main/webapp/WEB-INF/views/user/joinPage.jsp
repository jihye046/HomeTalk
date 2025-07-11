<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/joinPage.css"/>" rel="stylesheet">

<!-- axios -->
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<!-- lodach -->
<script src="https://cdn.jsdelivr.net/npm/lodash@4.17.21/lodash.min.js"></script>
</head>
<body>
	<main>
		<div class="join-background">
	        <div class="join-card">
	            <h2 class="join-title">회원가입</h2>
	            <p class="join-subtitle">새로운 계정을 만들고 서비스를 이용해보세요.</p>
	
	            <form id="join-form" action="/user/join" method="post" class="join-form">
	            	<input type="hidden" name="user_type" value="NORMAL">
	                <div class="input-group">
	                    <label for="username">이름</label>
	                    <input type="text" id="username" name="username" placeholder="이름을 입력해주세요" required>
	                </div>
	                <div class="input-group">
	                    <label for="umobile">휴대폰 번호</label>
	                    <input type="text" id="umobile" name="umobile" placeholder="숫자만 입력해주세요" required>
	                </div>
	                <div class="input-group">
	                    <label for="userId">아이디</label>
	                    <div class="id-input-wrapper">
	                    	<input type="text" id="userId" name="userId" placeholder="아이디를 입력해주세요" required>
	                    	<button type="button" id="checkIdButton" class="btn-check-id" disabled>중복 확인</button>
	                    </div>
	                    <p id="idRequirement" class="requirement-message"></p>
	                </div>
	                <div class="input-group">
	                    <label for="password">비밀번호</label>
	                    <input type="password" id="password" name="userPw" placeholder="새 비밀번호를 입력해주세요 (8~16자)" required>
		                <p id="passwordRequirement" class="requirement-message">영문, 숫자, 특수문자 조합 8~16자</p>
	                </div>
	                <div class="input-group">
	                    <label for="confirmPassword">새 비밀번호 확인</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호를 다시 입력해주세요">
                        <p id="passwordMismatchMessage" class="error-message"></p>
	                </div>
	                <div class="input-group">
	                    <label for="unickName">닉네임</label>
	                    <input type="text" id="unickName" name="unickName" maxlength="8" placeholder="닉네임을 입력해주세요" required>
						<p id="nicknameRequirement" class="requirement-message"></p>
	                </div>
	                <div class="input-group email-verification-group">
	                    <label for="uemail">이메일 주소</label>
	                    <div class="email-input-wrapper">
	                        <input type="email" id="uemail" name="uemail" placeholder="이메일 주소를 입력해주세요" required>
	                        <button type="button" id="mailCodeButton" class="btn-verify-email">인증번호 발송</button>
	                    </div>
	                </div>
	
	                <div class="input-group mail-Check-Box">
	                    <label for="mailCheckInput">인증번호</label>
	                    <input id="mailCheckInput" type="number" maxlength="6" placeholder="인증번호 6자리 입력" required>
	                    
	                    <!-- 인증 결과 실시간 -->
	                    <div class="verification-status">
		                    <span id="mailCheckSpan" class="verification-message"></span>
		                    <!-- verify-timer 추가 -->
		                    <span id="verificationTimer" class="timer-message"></span>
	                    </div>
	                </div>
	                
	                <button id="joinBtn" class="btn-submit" type="submit" disabled="disabled">회원가입</button>
	
	                <div class="form-links">
	                    <a href="/user/loginPage">이미 계정이 있으신가요? 로그인</a>
	                </div>
	            </form>
	        </div>
	    </div>
	</main>
</body>
<script src="<c:url value="/resources/js/joinPage.js"/>"></script>
</html>
