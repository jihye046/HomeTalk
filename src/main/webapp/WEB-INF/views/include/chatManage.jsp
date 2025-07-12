<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- 채팅 아이콘 -->
    <div id="chatIconWrapper">
		<div id="chatIcon" class="chat-icon" onclick="loadChatRooms()">
	    	<i class="fas fa-comment-dots"></i> 
	    	<div class="badge"></div>
	   	</div>
    </div>
    
    <!-- 모달창 -->
    <div id="chatModal" class="chat-modal">
       	<!-- 채팅방 목록 컨테이너 -->
        <div class="chat-modal-content">
	        <div class="chat-rooms">
	        	<!-- 채팅방 검색 -->
	        	<div class="input-wrapper">
			    	<i class="fa-solid fa-magnifying-glass" style="color: #6d6f74;"></i>
			    	<input id="searchTextInput" placeholder="검색" type="text">
			    </div>
				<!-- 채팅방 목록 -->
				<div class="roomList" id="roomList"></div>
	        </div> 
	        
	        <!-- 채팅 내용 -->
	        <div class="chat-records" >
	            <div id="header"></div>
				<div id="dateDisplay"></div>
				<div id="chatList"></div>
				<!-- <input type="text" id="msg" placeholder = "메시지를 입력하세요." onkeydown="handleKeyDown(event)"> -->
				<input type="text" id="msg" placeholder = "메시지를 입력하세요." >
	        </div>
        </div>
    </div>
<div class="hidden-data" id="userId" data-userId="${sessionScope.userId}"></div>
<div class="hidden-data" id="userNickname" data-userNickname="${sessionScope.userNickname}"></div>
<div class="hidden-data" id="contextPath" data-context-path="${pageContext.request.contextPath}"></div>
<script src="<c:url value="/resources/js/chatPage1.js"/>"></script>