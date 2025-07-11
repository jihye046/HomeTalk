<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
		<!-- menu -->
		<div class="navigation-wrap bg-light start-header start-style">
		<div class="container">
			<div class="row">
				<div class="col-12">
					<nav class="navbar navbar-expand-md navbar-light">
						<a class="navbar-brand" href="/">
							<img alt="logo-image" src="<c:url value="/resources/images/logo2.png"/>">
						</a>	
						<div class="collapse navbar-collapse" id="navbarSupportedContent">
							<ul class="navbar-nav ml-auto py-4 py-md-0">
								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
									<a class="nav-link" href="/">Community</a>
								</li>
								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4 weather-nav-item">
									<a class="nav-link" href="/weather/weatherPage">Weather</a>
								</li>
								<!-- Map 삭제 -->
<!-- 								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4"> -->
<!-- 									<a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Map</a> -->
<!-- 									<div class="dropdown-menu"> -->
<!-- 										<a class="dropdown-item" href="/map/searchedAddMap">검색한 주소지 지도</a> -->
<!-- 										<a class="dropdown-item" href="/map/specificAddMap">특정 주소지 지도</a> -->
<!-- 										<a class="dropdown-item" href="/map/findRoutePage">특정 주소지까지 길찾기</a> -->
<!-- 										<a class="dropdown-item" href="/map/findRoute">검색한 주소지까지 길찾기</a> -->
<!-- 									</div> -->
<!-- 								</li> -->
								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
									<a class="nav-link" href="/user/myPage">Mypage</a>
								</li>
								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
									<a class="nav-link" href="/board/contact">Contact</a>
								</li>
							</ul>
						</div>
						<div class="col-md-6 header-welcomeText">
							<h5 id="welcomeText"></h5>
<%-- 							<a class="menubar-button-primary" id="loginLogoutLink" href="${empty sessionScope.userId ? --%>
<%-- 														  '/user/loginPage' :  --%>
<%-- 														  '/user/logout'}"  --%>
<!-- 														  style="padding: 10px; font-size: 14px;"> -->
<%-- 														  ${empty sessionScope.userId ? 'LOGIN' : 'Logout'}</a> --%>
							<a class="menubar-button-primary" id="loginLogoutLink"
							   href="<c:url value="${empty sessionScope.userId ? '/user/loginPage' : '/user/logout'}" />"
							   style="padding: 10px; font-size: 14px;">
							   ${empty sessionScope.userId ? 'LOGIN' : 'Logout'}
							</a>
							<a class="menubar-button-primary write" href="/board/createPage" style="font-size: 14px;"> 
								글쓰기
							</a>
						</div>
					</nav>		
				</div>
			</div>
		</div>
	</div>
	<div class="ee5">
	</div>
<script src="<c:url value="/resources/js/loginInfo.js"/>"></script>