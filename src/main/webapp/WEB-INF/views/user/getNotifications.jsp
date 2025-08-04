<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/getNotifications.css"/>" rel="stylesheet">

<!-- axios -->
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	
	<main class="my-posts-main">
		<div class="my-posts-container">
			<div class="page-header">
	            <h2 class="page-title">알림</h2>
	            <button id="markAllAsReadBtnPage" class="mark-all-read-btn-page">모두 읽기</button>
	        </div>
			<ul class="noti-list"></ul>
			<div class="spinner-grow spinner-grow-sm"></div>
		</div>
		<div class="no-notifications-message" id="noNotificationsMessage"></div>			
	</main>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	
	<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>
	<div class="hidden-data" id="userNickname" data-userNickname="${sessionScope.userNickname}"></div>
	<div class="hidden-data" id="notificationAllList" 
		data-notificationAllList='${fn:escapeXml(notificationAllListJson)}'>
	</div>
	
	<script src="<c:url value="/resources/js/getNotifications.js"/>"></script>
</body>
</html>