<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/weather.css"/>" rel="stylesheet">

<!-- Bootstrap Icons CSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.8.1/font/bootstrap-icons.min.css">

<!-- ckeditor -->
<%-- <script src="${pageContext.request.contextPath}/resources/static/ckeditor/build/ckeditor.js"></script> --%>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main class="container" style="padding:  30px 0px 100px 0px;">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8 detailPage-main" >
				<div class="currentLocation">
					<i id="currentLocationBtn" class="fa-solid fa-location-crosshairs">
						<span id="city"></span>
					</i><br>
				</div>
				<div class="currentWeather-container">
					<div class="currentLocationWeather"></div>
					<div class="currentHourWeather"></div>
				</div>
				<h3>주간예보</h3>
				<div class="currentLocationWeeklyWeather"></div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</main>
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
<div class="hidden-data" id="latitude" data-latitude="${sessionScope.latitude}"></div>
<div class="hidden-data" id="longitude" data-longitude="${sessionScope.longitude}"></div>

<script src="<c:url value="/resources/js/common.js"/>"></script>
<script src="<c:url value="/resources/js/weather.js"/>"></script>
</html>