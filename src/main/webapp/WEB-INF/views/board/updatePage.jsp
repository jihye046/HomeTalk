<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/updatePage.css"/>" rel="stylesheet">

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
        			<div class="col-md-8 updatePage-main">
			            <form class="update-form" action="/board/updateBoard" method="post">
			                <input type="hidden" name="bId" value="${dto.bId}">
			                <input type="hidden" name="bGroup" value="${dto.bGroup}">
			                <input type="hidden" name="bName" value="${dto.bName}">
		                	<input class="bTitleInput" type="text" size="100" name="bTitle" value="${dto.bTitle}">
			                <textarea rows="10" cols="40" name="bContent" id="editor">${dto.bContent}</textarea>
			                <!-- 주소 검색 -->
							<div class="search-box">
								<input type="text" id="inputAdd" name="bAddress" value="${dto.bAddress}" readonly>
								<input type="button" class="address-search-btn" onclick="searchedAdd()" value="주소 검색"><br>
							</div>
							
							<!-- 태그 -->
							<input id="tagInput" name="tags" placeholder="태그는 열개까지만 가능합니다.">
			                <button class="post-submit-btn" type="submit" id="updateBtn">완료</button>
			            </form>
		            </div>
	            <div class="col-md-2"></div>
            </div>
        </div>
    </main>
</body>
<div class="hidden-data" id="tagJsonList" data-tagJsonList="${fn:escapeXml(tagJsonList)}"></div>
<div class="hidden-data" id="allTagJsonList" data-allTagJsonList="${fn:escapeXml(allTagJsonList)}"></div>
<div class="hidden-data" id="initRequestUrl" data-initRequestUrl="${initRequestUrl}"></div>

<script src="<c:url value="/resources/js/uploadAdapter.js"/>"></script>
<script src="<c:url value="/resources/js/updatePage.js"/>"></script>
</html>