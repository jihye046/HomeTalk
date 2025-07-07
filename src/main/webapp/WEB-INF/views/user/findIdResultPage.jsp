<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/findIdResultPage.css"/>" rel="stylesheet">
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<main>
		<div class="result-background">
            <div class="result-card">
                <h2 class="result-title">아이디 찾기 완료 🎉</h2>
                <p class="result-description">회원님의 정보와 일치하는 아이디 목록입니다.</p>

                <div class="id-list-container">
                	<c:forEach items="${findIdResultList}" var="dto">
	                    <div class="id-item">
	                        <span class="found-id">${dto.userId}</span>
	                        <span class="signup-date">가입일: ${dto.create_date }</span>
	                    </div>
                	</c:forEach>
                </div>

                <div class="button-group">
                    <button type="button" class="btn-primary" onclick="location.href='/user/loginPage'">로그인하기</button>
                    <button type="button" class="btn-secondary" onclick="location.href='/user/password-userid-input'">비밀번호 찾기</button>
                </div>
            </div>
        </div>
	</main>
</body>
<script src="<c:url value="/resources/js/findIdResultPage.js"/>"></script>
</html>