<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<link href="<c:url value="/resources/css/detailPage.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/fixedButton.css"/>" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fancyapps/fancybox@3.5.0/dist/jquery.fancybox.min.css">

<!-- 지도 표시 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsKey}&libraries=services"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
			
				<!-- 포스트 상단 -->
				<div class="post-header">
					
					<!-- 글 제목, 작성자 정보 -->
					<div class="title-and-name">
					
						<!-- 글 제목 -->
						<h2>${dto.bTitle}</h2>
						
						<div style="padding: 20px 0px 20px 0px;">
							<!-- 작성자 프로필 이미지 -->
							<img src="${imageUrl}" alt="image" class="user-avatar">
							
							<!-- 작성자명 -->
							<span class=bName>${dto.unickName}</span>
							
							<!-- 작성자 팔로우 버튼 -->
							<button class="button-primary" type="button" id="followButton">팔로우</button>
							
							<!-- 채팅 보내기 버튼 -->
							<button class="button-primary" type="button" id="chatButton" data-bName="${dto.bName}">
								<i class="fa-regular fa-comment-dots"></i>
							</button>
						</div>
						
					</div>
					<!-- 글 제목, 작성자 정보 end -->
					
					<!-- 드롭다운 버튼 -->
					<div class="dropdown">
					
						<!-- 드롭다운 토글 버튼 -->
						<button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
				    		<i class="fa-solid fa-ellipsis-vertical" style="margin-left: 10px; color: #808080;"></i>
				    	</button>
				    	
				    	<!-- 드롭다운 옵션 (수정, 삭제) -->
						<div class="dropdown-menu dropdown-menu-right">
				    		<a class="dropdown-item" href="/board/updatePage?bId=${dto.bId}">수정</a>
				    		<a class="dropdown-item" href="/board/deleteBoard?bId=${dto.bId}"
				    			onclick="return confirm('삭제하시겠습니까?')"
			    			>삭제
			    			</a>
			    		</div>
			    		
			    	</div>
			    	<!-- 드롭다운 버튼 end -->
			    	
				</div>
				<!-- 포스트 상단 end -->
				
				<hr>
								
				<!-- 포스트 중단 -->	
				<div class="post">
					${dto.bContent}
					
					<!-- 지도 표시 -->
					<div id="mapContainer">
						<div id="mapView" data-bAddress="${dto.bAddress}"></div>
						<div id="addressInfo"></div>
					</div>
					
					<!-- 태그 표시 -->
					<div class="tag-list">
						<c:forEach items="${tagList}" var="tag">
							<a href="#" class="tag-item"># ${tag.tagName}</a>
						</c:forEach>
					</div>
					
					<!-- 게시글 통계 정보 (작성일자, 좋아요, 스크랩, 조회수 등) -->
					<div class="post-meta-div">
					    <span class="post-meta-item">${dto.bDate}</span>
					    <span class="separator"> | </span> <span class="post-meta-item">좋아요</span>
					    <span class="post-meta-item" id="totalLikes">${dto.bLike}</span>
					    <span class="separator"> | </span> <span class="post-meta-item">북마크</span>
					    <span class="post-meta-item">${dto.bBookmark}</span>
					    <span class="separator"> | </span> <span class="post-meta-item">조회수</span>
					    <span class="post-meta-item">${dto.bHit}</span>
					</div>
					
					<hr id="postImageDivider" class="hidden-hr">
				</div>
				<!-- 포스트 중단 end -->
				
				<!-- 미리보기 이미지 갤러리 -->
				<div class="image-container"></div>
				
				<!-- 전체 이미지 보기 모달 -->
				<div id="gallery-modal" class="gallery-modal">
					<div class="gallery-modal-content">
						<div class="modal-header">
							<span>사진 모아보기</span>
							<span class="close-button" id="closeModal">&times;</span>
						</div>
						
						<!-- 이미지 영역 -->
						<div class="modal-image-container"></div>
					</div>
				</div>
				<!-- 전체 이미지를 보여줄 모달 end -->
				
				<!-- 고정 버튼 -->
				<div class="fixed-buttons">
				
					<!-- 좋아요 버튼 -->
					<button class="icon-button like ">
						<i class="fa-heart" style="color: #666666;"></i>
						<span class="count">${dto.bLike}</span>
					</button>			
					
					<!-- 북마크 버튼 -->
					<button class="icon-button bookmark">
						<i class="fa-bookmark" style="color: #666666;"></i>
						<span class="count">${dto.bBookmark}</span>
					</button>
					
					<!-- 공유 버튼 -->
					<div class="share">
						<button class="icon-button share">
							<i class="fa-regular fa-share-from-square" style="color: #666666;"></i>
						</button>
					
						<!-- 공유 옵션 -->
						<div class="share-option-div">
						
							<!-- 네이버 -->
							<button class="share-option naver" onclick="shareNaver()">
					    		<span>
									<script type="text/javascript" src="https://ssl.pstatic.net/share/js/naver_sharebutton.js"></script>
									<script type="text/javascript">new ShareNaver.makeButton({"type": "b"});</script>
								</span>
							</button>
							
							<!-- 카카오 -->
							<button class="share-option kakaoTalk" onclick="shareKakao()">
								<img src="https://buly.kr/DPTKLrS" alt="kakao" style="width:20px;">
							</button>
							
						</div>
						<!-- 공유 옵션 end -->
						
					</div>
					<!-- 공유 버튼 end -->
					
					<!-- 링크 복사 버튼 -->
					<button class="icon-button link">
						<i class="fa-solid fa-link" style="color: #666666;"></i>
					</button>
					
					<!-- 맨 위로 버튼 -->
					<button class="icon-button back-to-top">
						<i class="fa-solid fa-angle-up" style="color: #666666;"></i>
					</button>
					
				</div> 
				<!-- 고정 버튼 end -->
				
				<hr>
				
				<!-- 댓글 div -->
				<div class="reply-form">
					
					<!-- 댓글 수 -->
					<div class="commentCount-div">
						<span>댓글</span><span class="commentCount">${dto.commentCount}</span>
					</div>
					
					<!-- 댓글 입력 창 -->
					<div class="comment-input-container">
						<input class="styled-input" type="text" id="comment-input" placeholder="댓글" required>
						<button class="submit-comment button-filled-primary" type="button" id="commentBtn">댓글</button>
					</div>
					
					<!-- 댓글 리스트 -->
					<div class="comments">
						<div>
							<c:forEach items="${commentsPagingList}" var="comment"  varStatus="status">
							
								<!-- 댓글인 경우 -->
								<c:if test="${comment.bIndent == 1}">
									<article>
										<c:choose>
											<c:when test="${comment.bContent == '작성자가 삭제한 댓글입니다.'}">
												<p class="post-content removeMessage">${comment.bContent}</p>
											</c:when>
											<c:otherwise>
												<div class="user-info">
													<div class="left-info">
															<img id="profile-photo-${comment.bId}" src="${profileImageUrls[status.index]}" />
														<span class="author-name"><a href="#">${comment.unickName}</a></span>
													</div>
													<c:if test="${comment.bName == sessionScope.userId}">
														<button type="button" class="button-filled-primary comment-remove"
															data-comment-remove-bId="${comment.bId}"
															data-bGroup="${comment.bGroup}"
															data-bStep="${comment.bStep}"
															data-bIndent="${comment.bIndent}"
														>
															<i class="fa-regular fa-trash-can"></i>
														</button>
													</c:if>
												</div>
												<p class="post-content">${comment.bContent}</p>
												<time class="post-time">${comment.bDate}</time>
												<button type="button" class="button-filled-primary comment-child-btn"
													data-bGroup="${comment.bGroup}"
													data-bStep="${comment.bStep}"
													data-bIndent="${comment.bIndent}"
												>
													답글 달기
												</button>
												<button type="button" class="button-filled-primary"
													id="thumbupButton"
													data-recommend-bId="${comment.bId}"
												>
													<i class="${comment.recommended ? 'fa-solid fa-thumbs-up' : 'fa-regular fa-thumbs-up'}"></i>
													<span class="total-Recommendation">${comment.bLike}</span>
												</button>
											</c:otherwise>
										</c:choose>
								    </article>	
								</c:if>
								
								<!-- 답글인 경우 -->
								<c:if test="${comment.bIndent != 1}">
									<article class="comment-child">
										<div class="user-info">
											<div class="left-info">
												<img id="profile-photo-${comment.bId} comment-child" src="${profileImageUrls[status.index]}" />
												<span class="author-name comment-child"><a href="#">${comment.unickName}</a></span>
											</div>
											<c:if test="${comment.bName == sessionScope.userId}">
												<button type="button" class="button-filled-primary comment-child comment-remove" 
													data-comment-remove-bId="${comment.bId}"
													data-bGroup="${comment.bGroup}"
													data-bStep="${comment.bStep}"
													data-bIndent="${comment.bIndent}"
												>
													<i class="fa-regular fa-trash-can"></i>
												</button>
											</c:if>
										</div>
									    <p class="post-content comment-child">${comment.bContent}</p>
										<time class="post-time comment-child">${comment.bDate}</time>
										<button type="button" class="button-filled-primary comment-child"
											id="thumbupButton" 
											data-recommend-bId="${comment.bId}"
										>
											<i class="${comment.recommended ? 'fa-solid fa-thumbs-up' : 'fa-regular fa-thumbs-up'}"></i>
								    		<span class="total-Recommendation">${comment.bLike}</span>
							    		</button>
								    </article>
								</c:if>
								
						    </c:forEach>
						    
						    <!-- 댓글 페이징 블럭 -->
							<nav>
								<ul class="pagination justify-content-center">
								
									<!-- Previous 버튼 -->
									<c:choose>
										<c:when test="${commentsPaging.page <= 3}">
											<li class="page-item"></li> <!-- < 버튼 표시 x -->
										</c:when>
										<c:otherwise>
													<li class="page-item">
														<a class="page-link" href="#"
															data-bGroup="${dto.bGroup}"
															data-page="${commentsPaging.page-3}"
															data-sortType=""
														> < 
														</a>
													</li>
										</c:otherwise>
									</c:choose> 
									
									<!-- 개별 페이지 버튼 -->
									<c:forEach begin="${commentsPaging.startPage}" end="${commentsPaging.endPage}"
										var="i" step="1">
										<c:choose>
											<c:when test="${i eq commentsPaging.page}">
												<li class="page-item">
													<span class="page-link" style="pointer-events: none;">${i}</span>
												</li>
											</c:when>
											<c:otherwise>
												<li class="page-item">
													<a class="page-link" href="#"
														data-bGroup="${dto.bGroup}"
														data-page="${i}"
														data-sortType=""
													> ${i}
													</a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach> 
									
									<!-- Next 버튼 -->
									<c:choose>
										<c:when test="${commentsPaging.maxPage <= (commentsPaging.page+3) || commentsPaging.page >= commentsPaging.maxPage}">
											<li class="page-item"></li>
										</c:when>
										<c:otherwise>
													<li class="page-item">
														<a class="page-link" href="#"
															data-bGroup="${dto.bGroup}"
															data-page="${commentsPaging.page + 3}"
															data-sortType=""
														> > 
														</a>
													</li>
										</c:otherwise>
									</c:choose>
								</ul>
							</nav>
							<!-- 댓글 페이징 블럭 end -->
							
						</div>
					</div>
					<!-- 댓글 리스트 end -->
				 
				 </div>
				 <!-- 댓글 div end -->
				 
				 <!-- 링크 복사 알림 메시지 div  -->
				 <div class="copy-message" id="copyMessage">링크가 복사되었습니다.</div>
				 
			</div> 
		</div>
	</div> 
	
	<!-- 채팅아이콘 -->
	<%@ include file="/WEB-INF/views/include/chatManage.jsp" %>
	
	<!-- 카카오톡 공유 임시 DOM -->
	<div id="tempDiv"style="display: none;" ></div>
	
	<!-- fancybox 이미지 갤러리 -->
	<script src="https://cdn.jsdelivr.net/npm/@fancyapps/fancybox@3.5.0/dist/jquery.fancybox.min.js"></script>	
<div class="hidden-data" id="updateResult" data-update-result="${updateResult}"></div>
<div class="hidden-data" id="bId" data-bId="${dto.bId}"></div>
<div class="hidden-data" id="bTitle" data-bTitle="${dto.bTitle}"></div>
<div class="hidden-data" id="bContent" data-bContent='${dto.bContent}'></div>
<div class="hidden-data" id="isLiked" data-isLiked="${isLiked}"></div>
<div class="hidden-data" id="isBookmarked" data-isBookmarked="${isBookmarked}"></div>
<div class="hidden-data" id="userNickname" data-userId="${sessionScope.userNickname}"></div>
<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>

<script src="<c:url value="/resources/js/common.js"/>"></script>
<script src="<c:url value="/resources/js/fixedButton.js"/>"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script> <!-- 카카오 공유 -->
<script src="<c:url value="/resources/js/detailPage.js"/>"></script>
</body>


<script>
const userId = document.querySelector("#userId").getAttribute("data-userId")

/* 결과 메시지
================================================== */
const setMessage = (msg) => {
	const message = document.querySelector("#copyMessage")
	message.textContent = msg
	message.classList.add('show')
	setTimeout(() => {
		message.classList.remove('show')
	}, 2000)
}

/* 댓글 리스너, 페이징
================================================== */
const replyBtn = document.querySelector("#commentBtn")
const replyTable = document.querySelector(".comments")
const replyInput = document.querySelector("#comment-input")
const COMMENTS_PAGE_LIMIT = 6
const COMMENTS_BLOCK_LIMIT = 3
let currentPage = 1

	// 현재 페이지 블록
const setPage = (page) => {
	currentPage = page
}

	// 댓글 개수UI 업데이트
const editCommentCount = (commentCount) => {
	const commentCountDiv = document.querySelector(".commentCount-div")
	const commentCountSpan = commentCountDiv.querySelector(".commentCount")
	commentCountSpan.textContent = commentCount
}
	
	// 댓글UI 업데이트
const removeMessage = '작성자가 삭제한 댓글입니다.'
const editCommentTable = (replyList, profileImageUrls) => {
	
	const formatDate = (timestamp) => {
		const year = timestamp.getFullYear()
		const month = String(timestamp.getMonth() + 1).padStart(2, '0')
		const day = String(timestamp.getDate()).padStart(2, '0')
		//const hours = String(timestamp.getHours()).padStart(2, '0')
		//const minutes = String(timestamp.getMinutes()).padStart(2, '0')
		
		return `\${year}-\${month}-\${day}`
	}
	
	let output = `<div>`
		for(let i in replyList){
			const formattedDate = formatDate(new Date(replyList[i].bDate))
			
			if(replyList[i].bIndent == 1){ // 댓글
			output += `<article>`
				if(replyList[i].bContent == removeMessage) {
					output += `<p class="post-content removeMessage">\${replyList[i].bContent}</p>`
				} else {
					output += `<div class="user-info">
								<div class="left-info">
									<img id="profile-photo-\${replyList[i].bId}" src="\${profileImageUrls[i]}" />
									<span class="author-name"><a href="#">\${replyList[i].unickName}</a></span>
								</div>`
								if(replyList[i].bName == userId){
									output += `
										<button type="button" class="button-filled-primary comment-remove"
											data-comment-remove-bId="\${replyList[i].bId}"
											data-bGroup="\${replyList[i].bGroup}"
											data-bStep="\${replyList[i].bStep}"
											data-bIndent="\${replyList[i].bIndent}"
										>
											<i class="fa-regular fa-trash-can"></i>
										</button>`
								}
					output += `</div>
					    	<p class="post-content">\${replyList[i].bContent}</p>
					    	<time class="post-time">\${formattedDate}</time>
					    	<button type="button" class="button-filled-primary comment-child-btn" 
					    		data-bGroup="\${replyList[i].bGroup}"
					    		data-bStep="\${replyList[i].bStep}"
					    		data-bIndent="\${replyList[i].bIndent}"
				    		> 
					    		답글 달기
					    	</button>
				    		<button type="button" class="button-filled-primary" 
				    			id="thumbupButton"
				    			data-recommend-bId="\${replyList[i].bId}"
			    			>
						    	<i class="\${replyList[i].recommended ? 'fa-solid fa-thumbs-up' : 'fa-regular fa-thumbs-up'}" ></i>
						    	<span class="total-Recommendation">\${replyList[i].bLike}</span>
					    	</button> `
				}
				output += `</article>`
			} else { // 답글
				output += `<article class="comment-child">
								<div class="user-info">
									<div class="left-info">
										<img id="profile-photo-\${replyList[i].bId} comment-child" src="\${profileImageUrls[i]}" />
										<span class="author-name comment-child"><a href="#">\${replyList[i].unickName}</a></h4>
									</div>`
								if(replyList[i].bName == userId){
									output += `
										<button type="button" class="button-filled-primary comment-child comment-remove"
											data-comment-remove-bId="\${replyList[i].bId}"
											data-bGroup="\${replyList[i].bGroup}"
											data-bStep="\${replyList[i].bStep}"
											data-bIndent="\${replyList[i].bIndent}"
										>
											<i class="fa-regular fa-trash-can"></i>
										</button>
									`
								}
									
					output += `</div>
							    <p class="post-content comment-child">\${replyList[i].bContent}</p>
							    <time class="post-time comment-child">\${formattedDate}</time>
							    <button type="button" class="button-filled-primary comment-child"
							    	id="thumbupButton"
							    	data-recommend-bId="\${replyList[i].bId}"
						    	>
							    	<i class="\${replyList[i].recommended ? 'fa-solid fa-thumbs-up' : 'fa-regular fa-thumbs-up'}"></i>
							    	<span class="total-Recommendation">\${replyList[i].bLike}</span>
					    		</button>
							</article>`
			}
		}
	output += `</div>`
	return output
}

	// 답글UI 업데이트
const replyChildUI = () => {
	let output = `<div class="comment-input-container commentCell">
					<input type="text" name="bContent" class="styled-input" required>
					<button class="submit-comment button-filled-primary" type="button" id="commentChildSendBtn">작성</button>
				  </div>`
	return output
}

	// 페이지네이션 블록UI 업데이트
const updatePagingBlock = (dto, commentsPaging) => {
	let output = `<nav><ul class="pagination justify-content-center">`
		
	// Previous 버튼
	if(commentsPaging.page <= COMMENTS_BLOCK_LIMIT) {
		output += `<li class="page-item"></li>`
	} else {
		let previousPageLink = `<a class="page-link" href="#"
									data-bGroup="${dto.bGroup}"
									data-page="\${commentsPaging.page - COMMENTS_BLOCK_LIMIT}"
									data-sortType=""
								> <
								</a>`
		output += `<li class="page-item">\${previousPageLink}</li>`
	}
	
	// 페이지 블록 번호
	for(let i = commentsPaging.startPage; i <= commentsPaging.endPage; i++){
	    let pagingLink = (i == commentsPaging.page) ? 
	    	`<span class="page-link" style="pointer-events: none;">\${i}</span>` :
	    	`<a class="page-link" href="#"
	    	 	data-bGroup="${dto.bGroup}"
	    	 	data-page="\${i}"
	    	 	data-sortType=""
    	 	 >\${i}
    	 	 </a>`
		        
		    output += `<li class="page-item">\${pagingLink}</li>`
	}
		    
	// Next 버튼
	if(commentsPaging.page >= commentsPaging.maxPage){
		output += `<li class="page-item"></li>`
	} else {
		let nextPageLink = (commentsPaging.page + COMMENTS_BLOCK_LIMIT >= commentsPaging.maxPage) ? 
			`<a class="page-link" href="#"
				data-bGroup="${dto.bGroup}"
				data-page="\${commentsPaging.maxPage}"
				data-sortType=""
			 > >
			 </a>` :
			`<a class="page-link" href="#" 
			 	data-bGroup="${dto.bGroup}"
			 	data-page="\${commentsPaging.page + COMMENTS_BLOCK_LIMIT}"
			 	data-sortType=""
		 	 > >
		 	 </a>`
		output += `<li class="page-item">\${nextPageLink}</li>`
	} 
	
	return output
}

	// 댓글 UI 업데이트 및 리스너 재등록
const updateCommentUI = (data) => {
	replyInput.value = ''
	replyTable.innerHTML = ''
	let dto = data.commentsListResponse.commentsPagingList
	let commentsPaging = data.commentsListResponse.commentsPagingDto
	let profileImageUrls = data.commentsListResponse.profileImageUrls
	let commentsCount = data.commentsCount // dto.setCommentCount를 안했기때문에 dto. 값이 아니라 서버에서 직접 map에 담은 값으로 확인 
	
	// UI 업데이트
	let output = editCommentTable(dto, profileImageUrls) // 댓글 UI
	output += updatePagingBlock(dto, commentsPaging) // 페이징블록 UI
	replyTable.innerHTML = output // 댓글 컨테이너(class='comments') UI
	editCommentCount(commentsCount) // 댓글 개수 UI
		
	// 리스너 재등록
	ajaxBlockLink() // 댓글 블록 링크
	registerEventListeners() // 답글 등록
	handleRecommendation() // 댓글, 답글 추천
	handleCommentRemove() // 댓글, 답글 삭제
}

	// 댓글 블록 링크 리스너
const ajaxBlockLink  = () => {
	document.querySelectorAll('a.page-link').forEach(function(link){
		link.addEventListener('click', function(){
			event.preventDefault()
			
			const bGroup = link.getAttribute('data-bGroup')
			const pageNum = link.getAttribute('data-page')
			setPage(pageNum)
			const sortType = link.getAttribute('data-sortType')
			
			$.ajax({
				url: "/board/commentsPaging/ajax",
				data: {
					bGroup: `\${bGroup}`,
					page: `\${pageNum}`,
					sortType: `\${sortType}`
				},
				success: function(data){
					replyInput.value = '' 
					replyTable.innerHTML = ''
					let dto = data["commentsPagingList"]; // 객체로 리턴했기때문에 Map으로 리턴했을때와는 접근 방식 다름
			        let commentsPaging = data["commentsPagingDto"];
					let profileImageUrls = data["profileImageUrls"]
			        
			        // UI 업데이트
			        let output = editCommentTable(dto, profileImageUrls) // 댓글
					output += updatePagingBlock(dto, commentsPaging) // 페이징 블록
					replyTable.innerHTML = output
					
					// 리스너 재등록
					ajaxBlockLink() // 댓글 블록 링크
					registerEventListeners() // 답글
					handleRecommendation() // 추천 버튼
					handleCommentRemove() // 댓글 삭제 버튼
				},
				error: function(error){
					console.log(error)
				} 
			})
		})
	})
}

	// 댓글 버튼 리스너
replyBtn.addEventListener('click', function(){
	let replyInputValue = replyInput.value
	if(!replyInputValue || !(replyInputValue.trim())){
		alert("내용을 입력해주세요.")
		return
	} else {
		$.ajax({
			type: "post",
			url: "/board/replyInsert",
			data: {
				page: currentPage,
				bId: "${dto.bId}",
				bContent: replyInputValue,
				bGroup: "${dto.bGroup}",
				bStep: "${dto.bStep}",
				bIndent: "${dto.bIndent}"
			},
			dataType: "json",
			success: function(data){
				updateCommentUI(data)
			},
			error: function(){
				console.log("error")
			}
		})
	}
})

	// 답글 버튼 리스너
function registerEventListeners(){
	const childReplyBtns = document.querySelectorAll(".comment-child-btn")
	childReplyBtns.forEach(childReplyBtn => { 
	    childReplyBtn.addEventListener('click', function(){
	        
	        // 다른 답글 창이 열려있다면 닫기
	        const openReplyCells = document.querySelectorAll('.commentCell');
			openReplyCells.forEach(cell => {
			    cell.parentNode.removeChild(cell);
			})

	        const article = this.closest('article') // 클릭된 버튼이 속한 행 찾기
	        const bGroup = this.getAttribute('data-bGroup')
	        const bStep = this.getAttribute('data-bStep')	
	        const bIndent = this.getAttribute('data-bIndent')
	        
	        // 새로운 입력 필드 추가
            const output = replyChildUI()
            // 행의 끝에 새로운 입력 필드 추가
            article.insertAdjacentHTML('afterend', output);
            
        	const sendButton = article.nextElementSibling.querySelector('button');
            sendButton.addEventListener('click', function() {
            	const bContent = article.nextElementSibling.querySelector('input').value
            	if(!bContent || !(bContent.trim())){
            		alert("내용을 입력해주세요.")
            		return
            	} else {
            		$.ajax({
                    	type: "post",
                    	url: "replyChildInsert",
                    	data: {
                    		page: currentPage,
                    		bContent: bContent,
                    		bGroup: bGroup,
                    		bStep: bStep,
                    		bIndent: bIndent
                    	},
                    	dataType: "json",
                    	success: function(data){
                    		updateCommentUI(data)
                    	},
                    	error: function(){
                    		console.log("error")
                    	}
                    })
            	}
            	article.nextElementSibling.remove() // 답글 창 닫기
            })
	    })
	})
}

/* 댓글 추천 
================================================== */
	// 추천 등록
const addRecommend = (bId, icon, totalRecommendation) => {
	fetch('/board/addRecommend', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		}, 
		body: JSON.stringify({
			bId: bId
		})
	})
	.then((response) => response.json())
	.then((data) => {
		icon.classList.remove('fa-regular')
		icon.classList.add('fa-solid')
		totalRecommendation.textContent = data
	})
	.catch(error => {
		console.log('error: ', error)
	})
}

	// 추천 취소
const removeRecommend = (bId, icon, totalRecommendation) => {
	fetch('/board/removeRecommend', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		}, 
		body: JSON.stringify({
			bId: bId
		})
	})
	.then((response) => response.json())
	.then((data) => {
		icon.classList.remove('fa-solid')
		icon.classList.add('fa-regular')
		totalRecommendation.textContent = data
	})
	.catch(error => {
		console.log('error: ', error)
	})
}

	// 추천 핸들러
const handleRecommendation = () => {
	document.querySelectorAll('#thumbupButton').forEach(thumbupBtn => {
		thumbupBtn.addEventListener('click', function() {
			const icon = thumbupBtn.querySelector('i')
			const bId = thumbupBtn.getAttribute('data-recommend-bId')
			const totalRecommendation = thumbupBtn.querySelector('.total-Recommendation')

			if(icon.classList.contains('fa-regular')) {
				addRecommend(bId, icon, totalRecommendation)
			} else if(icon.classList.contains('fa-solid')) {
				removeRecommend(bId, icon, totalRecommendation)
			}
		})
	})
}

/* 댓글, 답글 삭제
================================================== */
	// 댓글인지, 답글인지 판단
const handleCommentRemove = () => {
	document.querySelectorAll('.comment-remove').forEach(removeButton => {
		removeButton.addEventListener('click', function(){
			const confirmation = confirm('삭제하시겠습니까?')
			if(confirmation) {
				const bId = removeButton.getAttribute('data-comment-remove-bId')
				const bGroup = this.getAttribute('data-bGroup')
				const bStep = this.getAttribute('data-bStep')
				const bIndent = this.getAttribute('data-bIndent')
				
				if(bIndent == 1) {
					commentRemove(bId, bGroup, bStep, bIndent)
				} else {
					commentChildRemove(bId, bGroup, bStep)
				}
			}
		})
	})
}

	// 댓글 삭제
const commentRemove = (bId, bGroup, bStep, bIndent) => {
	$.ajax({
		type: "post",
		url: "/board/removeReply",
		data: {
			page: currentPage,
			bId: bId,
			bGroup: bGroup,
			bStep: bStep,
			bIndent: bIndent
		},
		dataType: "json",
		success: function(data) {
			updateCommentUI(data)
			setMessage(data.msg)
		},
		error: function(error){
			console.log("error:", error)
		}
	})
}

	// 답글 삭제
const commentChildRemove = (bId, bGroup, bStep) => {
	$.ajax({
		type: "post",
		url: "/board/removeChildReply",
		data: {
			page: currentPage,
			bId: bId,
			bGroup: bGroup,
			bStep: bStep
		},
		dataType: "json",
		success: function(data) {
			updateCommentUI(data)
			setMessage(data.msg)
		},
		error: function(error){
			console.log("error:", error)
		}
	})
}

/* 페이지 로드 시 실행될 함수
================================================== */
registerEventListeners()
ajaxBlockLink()
handleRecommendation()
handleCommentRemove()
</script>
</html>