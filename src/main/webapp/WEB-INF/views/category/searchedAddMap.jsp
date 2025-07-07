<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/searchedAddMap.css"/>" rel="stylesheet">

<!-- ckeditor -->
<%-- <script src="${pageContext.request.contextPath}/resources/static/ckeditor/build/ckeditor.js"></script> --%>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsKey}&libraries=services"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main class="container">
		<div class="row">
			<div class="col-md-2"></div>
				<div class="col-md-8 detailPage-main">
					<div class="search-box">
						<input type="text" id="inputAdd" placeholder="주소" disabled>
						<input type="button" onclick="searchedAdd()" value="주소 검색"><br>
						<div id="map"></div>
					</div>
				</div>
			<div class="col-md-2"></div>
		</div>
	</main>
</body>
<script src="<c:url value="/resources/js/common.js"/>"></script>
<script src="<c:url value="/resources/js/searchedAdd.js"/>"></script>
</html>