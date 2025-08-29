<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<!-- driver.js -->
<script src="https://cdn.jsdelivr.net/npm/driver.js@latest/dist/driver.js.iife.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/driver.js@latest/dist/driver.css"/>

<link href="<c:url value="/resources/css/list.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/common.css"/>" rel="stylesheet">

<script>
	var paging = ${paging.page}
	var searchGubun = "${param.searchGubun}"
	var searchText = "${param.searchText}"
    var sortType = ("${param.sortType}" == "") ? "latest" : "${param.sortType}"
</script>
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	
	<!-- 팝업창 -->
	<div id="popup" class="popup-wrapper">
		<div class="popup-content">
			<button id="closePopup" class="popup-close-btn">&times;</button>
			<h3>🔥 테스트 계정을 사용해보세요</h3>
			<p class="popup-description">
	            사이트의 모든 기능을 자유롭게 체험할 수 있습니다.
	        </p>
	        <div class="account-info">
	        	<div>
	        		<span>아이디 1</span>
	        		<p>my_home</p>
	        	</div>
	        	<div>
	        		<span>비밀번호 1</span>
	        		<p>Test1234!</p>
	        	</div>
	        </div>
			<div class="account-info">
	        	<div>
	        		<span>아이디 2</span>
	        		<p>haru88</p>
	        	</div>
	        	<div>
	        		<span>비밀번호 2</span>
	        		<p>Test1234!</p>
	        	</div>
	        </div>
	        <div class="popup-options">
	        	<input type="checkbox" id="hideForDay">
	        	<label class="popup-hide-for-day" for="hideForDay">하루 동안 보지 않기</label>
	        </div>
		</div>
	</div>
	<!-- 팝업창 end -->
	
	<!-- 날씨 -->
	<div class="weather-container">
		<video id="weatherVideo" autoplay muted loop></video>
	</div>
	<!-- 날씨 end -->
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="row-inner">
						
						<!-- 가이드 버튼 -->
						<button id="start-guide-button" class="guide-start-button-with-icon">
						    <i class="fas fa-compass"></i> 가이드 시작하기
						</button>
						
						<div class="form-and-buttons-container">
							<div class="buttons-container">
								<button class="styled-button" id="sort_latest" type="button" class="btn btn-sm btn-dark">최신순</button>
								<button class="styled-button" id="sort_hit" type="button" class="btn btn-sm">조회순</button>
							</div>
							<div class="form-container">
								<form action="/board/paging">
									<input type="hidden" name="sortType" id="sortTypeInput">
									<select class="styled-select" name="searchGubun">
										<option value="bTitle" ${searchGubun == 'bTitle' ? 'selected' : ''}>제목</option>
										<option value="bContent" ${searchGubun == 'bContent' ? 'selected' : ''}>내용</option>
										<option value="unickName" ${searchGubun == 'unickName' ? 'selected' : ''}>작성자</option>
									</select> 
									<input class="styled-input" type="text" name="searchText" value="${searchText}" placeholder="검색">
								</form>
							</div>
						</div>
				</div>
			</div>
			<div class="col-md-12">
				<div id="hitContainer">
					<!-- card -->
					<div class="card-container">
						<c:forEach items="${boardList}" var="dto">
							<figure class="snip1518 hover">
								<div class="image" data-content="${dto.bContent}">
									<!-- 이미지가 없는 게시글이면 흰색 배경 기본 -->
								</div>
								<figcaption>
									<div class="post-bName">${dto.unickName}</div>
									<div class="post-bTitle">${dto.bTitle}</div>
									<footer>
										<div class="create-date">${dto.bDate}</div>
										<div class="icons">
											<div class="views"><i class="ion-eye"></i>${dto.bHit}</div>
											<div class="love"><i class="ion-heart"></i>${dto.bLike}</div>
											<div class="comment"><i class="fa-thin fa-comment fa-2xs"></i>${dto.commentCount}</div>
										</div>
									</footer>
								</figcaption>
								<a href="/board/detailBoard?
									bId=${dto.bId}&
									bGroup=${dto.bGroup}&
									page=${paging.page}&
									bName=${dto.bName}"
								>
								</a>
							</figure>
						</c:forEach>
					</div>
					<!-- card end-->
					
				</div>
				<!-- paging -->
				<nav>
					<ul class="pagination justify-content-center">
						<c:choose>
							<c:when test="${paging.page <= 1}">
								<li class="page-item"></li> <!-- 내용 표시 x -->
							</c:when>
							<c:otherwise>
								<c:choose>
<%-- 									<c:when test="${paging.page - 3 <= 1}"> --%>
									<c:when test="${paging.page - 5 <= 1}">
										<li class="page-item">
											<a class="page-link" href="/board/paging?page=${paging.page-1}&sortType="> < </a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="page-item">
											<a class="page-link" href="/board/paging?page=${paging.page-5}&sortType="> < </a>
										</li>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						<c:forEach begin="${paging.startPage}" end="${paging.endPage}"
							var="i" step="1">
							<c:choose>
								<c:when test="${i eq paging.page}">
									<li class="page-item">
										<span class="page-link" style="background-color: #ad9f94; pointer-events: none;">${i}</span>
									</li>
								</c:when>
								<c:otherwise>
									<li class="page-item">
										<a class="page-link" href="/board/paging?page=${i}&sortType=">${i}</a>
									</li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<c:choose>
							<c:when test="${paging.page >= paging.maxPage}">
								<li class="page-item"></li> <!-- 내용 표시 x -->
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${paging.page + 5 >= paging.maxPage}">
										<li class="page-item">
											<a class="page-link" href="/board/paging?page=${paging.maxPage}&sortType="> > </a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="page-item">
											<a class="page-link" href="/board/paging?page=${paging.page + 5}&sortType="> > </a>
										</li>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</ul>
				</nav>
				<!-- paging end -->
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/views/include/chatManage.jsp" %>
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	
	<div class="hidden-data" id="createResult" data-create-result="${createResult}"></div>
	<div class="hidden-data" id="deleteResult" data-delete-result="${deleteResult}"></div>
	<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>
	<div class="hidden-data" id="userNickname" data-userNickname="${sessionScope.userNickname}"></div>
	<div class="hidden-data" id="contextPath" data-context-path="${pageContext.request.contextPath}"></div>
	<div class="hidden-data" id="bucketName" data-bucket-name="${bucketName}"></div>
	
	<script src="<c:url value="/resources/js/pagingList.js"/>"></script>
	<script src="<c:url value="/resources/js/common.js"/>"></script>
</body>
</html>