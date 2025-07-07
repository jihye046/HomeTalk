<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/myPage.css"/>" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Inter&display=swap" rel="stylesheet" />
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main class="container">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8 detailPage-main">
				<div class="myPage-title">
					<h1>마이페이지</h1>
					<p class="page-desc">대표 프로필과 비밀번호를 수정하실 수 있습니다.</p>
				</div>
	
				<div class="mypage-links card-area">
					<div class="mypage-link">
						<a href="/user/getUserPosts">
							<i class="fa-regular fa-pen-to-square"></i>
							작성한 게시글
						</a>
					</div>
					<div class="mypage-link">
						<a href="/user/getUserLikedPosts">
							<i class="fa-regular fa-heart"></i>
							좋아요한 게시글
						</a>
					</div>
					<div class="mypage-link">
						<a href="/user/getUserComments">
							<i class="fa-regular fa-comment"></i>
							작성한 댓글
						</a>
					</div>
				</div>
	
				<div class="mypage-links settings-area">
					<div class="mypage-link">
						<a href="/user/updateProfileForm">프로필 수정</a>
					</div>
					<div class="mypage-link">
						<a href="/user/verify-user?mode=password&userId=${sessionScope.userId}">비밀번호 변경</a>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</main>
	<!-- 채팅아이콘 -->
	<%@ include file="/WEB-INF/views/include/chatManage.jsp" %>
</body>
<script src="<c:url value="/resources/js/myPage.js"/>"></script>
<script src="<c:url value="/resources/js/common.js"/>"></script>
</html>