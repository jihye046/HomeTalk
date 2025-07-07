<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/getUserPosts.css"/>" rel="stylesheet">
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<main class="my-posts-main">
		<div class="my-posts-container">
			<div class="page-title-section">
                <h2 class="page-title">내가 작성한 댓글</h2>
            </div>
            
            <div class="sort-buttons-section">
                <div class="sort-buttons-wrapper">
                	<button id="sort_latest" type="button" class="btn btn-sm btn-dark">최신순</button>
                </div>
            </div>
			
			<div class="posts-table-section">
				<div id="hitContainer" class="table-responsive">
					<table class="table my-posts-table">
						<thead>
							<tr>
								<th>No.</th>
								<th>작성자</th>
								<th>댓글</th>
								<th>작성일</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${getUserComments}" var="dto" varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td>
										${dto.bName}
									</td>
									<td class="post-title-cell">
										<a href="/board/detailBoard?bId=${dto.bId}&
											bGroup=${dto.bGroup}">
											${dto.bContent}
										</a>
										<i class="fa-regular fa-comment-dots"></i> 
										<span class="commentCount">${dto.commentCount}</span>
									</td>
									<td>${dto.bDate}</td>
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
</html>