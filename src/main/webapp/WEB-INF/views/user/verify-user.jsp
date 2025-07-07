<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/verify-user.css"/>" rel="stylesheet">
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<main>
		<div class="find-id-background">
			<div class="find-id-card">
			
				<!-- 인증 -->
				<h2 class="find-id-title">본인 인증</h2>
				
				<form id="verify-form" method="post" class="find-id-form">
					<!--
						비밀번호 찾기 시 사용자가 입력한 아이디를 비밀번호 변경 페이지로 전달하기 위해 숨김 필드로 포함함.
						아이디 찾기 컨트롤러에도 빈 값으로 전송되지만 동작에는 영향 없음
					-->
					 <input type="hidden" name="verifyUserId" value="${verifyUserId}">

					<div class="auth-method-selection">
						<!-- 휴대전화 인증은 유료 SMS API 필요로 인해 현재 미구현 -->
						<!-- 이메일 인증만 지원하며, UI는 추후 확장 대비해 유지 -->
						<!-- <label class="radio-label">
							<input type="radio" name="authMethod" value="phone">
							휴대전화 인증
						</label> -->
						<label class="radio-label">							
							<input type="radio" name="authMethod" value="email" checked>
							이메일 인증
						</label>
					</div>
					
					<div class="auth-method-description">
						<p id="phoneDescription">회원정보에 등록된 휴대전화 번호와 입력한 번호가 같아야<br>인증번호를 받을 수 있습니다.</p>
						<p id="emailDescription">회원정보에 등록된 이메일 주소와 입력한 이메일 주소가 같아야<br>인증번호를 받을 수 있습니다.</p>
					</div>
					
					<!-- 휴대폰 인증 -->
					<div id="phoneAuthSection" class="input-group">
						<label for="namePhone">이름</label>
						<input type="text" id="namePhone" name="userNamePhone" placeholder="이름을 입력해주세요">
						
						<label for="mobileNumber">휴대전화</label>
						<div class="phone-input-wrapper">
							<select id="mobilePrefix" name="mobilePrefix">
								<option value="+82">+82 (대한민국)</option>
							</select>
							<input type="text" id="mobileNumber" name="umobile">
							<button type="button" id="sendSmsButton" class="btn-verify">인증번호 받기</button>
						</div>
					</div>
					
					<!-- 이메일 인증 -->
					<div id="emailAuthSection" class="input-group">
						<label for="nameEmail">이름</label>
						<input type="text" id="nameEmail" name="userNameEmail" placeholder="이름을 입력해주세요">
						
						<label for="userEmail">이메일 주소</label>
						<div class="email-input-wrapper">
							<input type="text" id="userEmail" name="userEmail" placeholder="이메일 아이디를 입력해주세요">
							<input type="email" id="directEmailDomain" name="directEmailDomain" placeholder="이메일 주소를 입력해주세요">
							<select id="emailDomain" name="emailDomain">
								<option value="@naver.com" selected>@naver.com</option>
								<option value="@gmail.com">@gmail.com</option>
								<option value="@daum.net">@daum.net</option>
								<option value="@nate.com">@nate.com</option>
								<option value="direct">직접 입력</option>
							</select>
							<button type="button" id="sendEmailButton" class="btn-verify">인증번호 받기</button>
						</div>
					</div>
					
					<!-- 인증번호 입력 -->
					<div class="input-group verification-code-group">
						<label for="verificationCode">인증번호</label>
						<input type="text" maxlength="6" id="verificationCode" name="verificationCode" placeholder="인증번호 6자리 입력">
						<div class="verification-status">
							<span id="verificationMessage" class="message"></span>
							<span id="verificationTimer" class="timer-message"></span>
						</div>
					</div>
					
					<button type="button" id="submitButton" class="btn-submit" disabled>다음</button>
					
					<!-- 로그인 페이지로 돌아가기 -->
					<div class="form-links">
						<a href="/user/loginPage">로그인 페이지로 돌아가기</a>
					</div>
				</form>
				
			</div>
		</div>
	</main>
</body>
<script src="<c:url value="/resources/js/verify-user.js"/>"></script>
<div class="hidden-data" id="mode" data-mode="${mode}"></div>
<div class="hidden-data" id="verifyUserId" data-verifyUserId="${verifyUserId}"></div>
</html>