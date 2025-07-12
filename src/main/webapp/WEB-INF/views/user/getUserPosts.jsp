<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/getUserPosts.css"/>" rel="stylesheet">
<script type="module" src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main class="my-posts-main">
		<div class="my-posts-container">
			<div class="page-title-section">
                <h2 class="page-title">내가 작성한 게시글</h2>
            </div>
            
            <div class="sort-and-search-section">
	            <div class="sort-buttons-wrapper">
                	<button id="sort_latest" type="button" class="sortBtn btn btn-sm btn-dark">최신순</button>
					<button id="sort_hit" type="button" class="sortBtn btn btn-sm">조회순</button>
	            </div>
	            <div class="search-box-wrapper">
	            	<select id="searchOption" class="form-select">
	                    <option value="bTitle">제목</option>
	                    <option value="bContent">내용</option>
	                </select>
	                <input type="text" id="searchInput" class="form-control" placeholder="검색어를 입력하세요.">
	                <button id="searchButton" type="button" class="btn btn-sm btn-dark">검색</button>
	            </div>
            </div>
			
			<div class="posts-table-section">
				<div id="hitContainer" class="table-responsive">
					<table class="table my-posts-table">
						<thead>
							<tr>
								<th>No.</th>
								<th>작성자</th>
								<th>제목</th>
								<th>작성일</th>
								<th>조회수</th>
							</tr>
						</thead>
						<tbody id="postTableBody">
							<c:forEach items="${getUserPosts}" var="dto" varStatus="status"> 
								<tr>
									<td>${status.count}</td>
									<td>
										${dto.unickName}
									</td>
									<td class="post-title-cell">
										<a href="/board/detailBoard?bId=${dto.bId}&
											bGroup=${dto.bGroup}">
											${dto.bTitle}
										</a>
										<i class="fa-regular fa-comment-dots"></i> 
										<span class="commentCount">${dto.commentCount}</span>
									</td>
									<td>${dto.bDate}</td>
									<td>${dto.bHit}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>			
		</div>
	</main>
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
<div class="hidden-data" id="createResult" data-create-result="${createResult}"></div>
<div class="hidden-data" id="deleteResult" data-delete-result="${deleteResult}"></div>
<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>
<div class="hidden-data" id="userNickname" data-userNickname="${sessionScope.userNickname}"></div>

<script src="<c:url value="/resources/js/getUserPosts.js"/>"></script>
</html>