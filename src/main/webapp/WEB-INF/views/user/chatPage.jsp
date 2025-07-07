<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/chatPage1.css"/>" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Inter&display=swap" rel="stylesheet" />
</head>
<body>
	<div id="main">
		<div id="header"><small>${receiver}님과의 대화방</small></div>
		<div id="dateDisplay"></div>
		<div id="chatList"></div>
 		<input type="text" id="msg" placeholder = "대화 내용을 입력하세요." onkeydown="handleKeyDown(event)">
	</div>
</body>
<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>
<div class="hidden-data" id="bName" data-bName="${receiver}"></div>

<script src="<c:url value="/resources/js/directChat.js"/>"></script>
<div class="hidden-data" id="contextPath" data-context-path="${pageContext.request.contextPath}"></div>
</html>