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
				<h2 class="join-title">추가정보 입력창</h2>
				<form id="join-form" action="/social/join" method="post" class="join-form">
					<input type="hidden" name="uemail" value="${socialDto.uemail}">
					<input type="hidden" name="userId" value="${socialDto.userId}">
<%-- 					<input type="text" name="sns_profile" value="${socialDto.sns_profile}"> --%>
					<input type="hidden" name="username" value="${socialDto.username}">
					<input type="hidden" name="user_type" value="${socialDto.user_type}">
					
					<div class="input-group">
						<label for="username">이름</label>	
						<input type="text" 
							id="username" 
							name="username" 
							value="${socialDto.username == '' ? '' : socialDto.username}" 
							placeholder="이름을 입력해주세요" required><br>
					</div>
					<div class="input-group">
						<label for="umobile">휴대폰 번호</label>
						<input type="text" 
							id="umobile" 
							name="umobile" 
							placeholder="숫자만 입력해주세요" required><br>
					</div>
					<div class="input-group">
						<label for="unickName">닉네임</label>					
						<input type="text" 
							id="unickName" 
							name="unickName" 
							maxlength="8" 
							placeholder="닉네임을 입력해주세요" required><br>
						<p id="nicknameRequirement" class="requirement-message"></p>
					</div>
					
					<button id="joinBtn" 
						class="btn-submit" 
						type="submit" disabled="disabled">회원가입</button>
					
					<div class="form-links">
						<a href="/user/loginPage">돌아가기</a>
					</div>
				</form>
			</div>
		</div>
	</main>
	
	<script src="<c:url value="/resources/js/socialJoinPage.js"/>"></script>
</body>
</html>
