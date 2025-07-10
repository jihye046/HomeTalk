<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="<c:url value="/resources/css/createPage.css"/>" rel="stylesheet">

<!-- tagify -->
<link href="https://cdn.jsdelivr.net/npm/@yaireo/tagify/dist/tagify.css" rel="stylesheet" type="text/css" />
<script src="https://cdn.jsdelivr.net/npm/@yaireo/tagify"></script>

<!-- ckeditor -->
<script src="${pageContext.request.contextPath}/resources/static/ckeditor/build/ckeditor.js"></script>

<!-- 주소 검색 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main>
		<div class="container">
        	<div class="row">
        		<div class="col-md-2"></div>
					<div class="col-md-8 createPage-main">
						<form action="/board/createBoard" method="post" class="create-form">
							<input type="hidden" size="100" name="bName" placeholder="${sessionScope.userId}" value="${sessionScope.userId}" readonly>
							<input class="bTitleInput" type="text" size="100" name="bTitle" placeholder="제목" required>
							<textarea id="editor" name="bContent" placeholder="내용을 입력해주세요"></textarea>
							
							<!-- 주소 검색 -->
							<div class="search-box">
								<input type="text" id="inputAdd" name="bAddress" placeholder="주소" readonly>
								<input type="button" class="address-search-btn" onclick="searchedAdd()" value="주소 검색"><br>
							</div>
							
							<!-- 태그 -->
							<input id="tagInput" name="tags" placeholder="태그는 열개까지만 가능합니다.">
							
							<button class="post-submit-btn" type="submit">등록</button>
						</form>
					</div>
				<div class="col-md-2"></div>
			</div>
		</div>
	</main>
<%-- 	<%@ include file="/WEB-INF/views/include/footer.jsp" %> --%>
	<div id="allTagJsonList" data-allTagJsonList="${fn:escapeXml(allTagJsonList)}"></div>
	<div class="hidden-data" id="initRequestUrl" data-initRequestUrl="${initRequestUrl}"></div>
	
	<script src="<c:url value="/resources/js/createPage.js"/>"></script>
	<script src="<c:url value="/resources/js/uploadAdapter.js"/>"></script>
</body>
</html>