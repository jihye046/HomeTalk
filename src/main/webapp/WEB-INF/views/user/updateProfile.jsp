<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<link href="<c:url value="/resources/css/updateProfile.css"/>" rel="stylesheet">
<!-- FilePond -->
<link href="https://unpkg.com/filepond/dist/filepond.min.css" rel="stylesheet">
</head>
<body>
	<div class="row header-div" style="background-color: #EAEAEA">
		<div class="col-md-12 header-logo">
			<a href="/">
				<img alt="logo" src="<c:url value="/resources/images/logo2.png"/>">
			</a>
		</div>
	</div>
	<main>
		<div class="profile-background">
			<div class="profile-container">
				<div class="profile-form">
					<div class="form-header">
						<span class="profile-title">프로필 편집</span>
					</div>
					<div class="form-body">
						<!-- 프로필 이미지 -->
						<div class="profile-section">
					        <img id="profileImage" src="/user/getProfileImage/${uprofile_image}" alt="Profile Image"
					        	 class="profile-image" style="cursor: pointer;" data-currentImage="${uprofile_image}">
					        <!-- 드롭다운 메뉴 -->	
							<div class="dropdown-menu" id="dropdownMenu">
								<a class="dropdown-item" href="#" id="defaultImage">기본이미지로 변경</a>
								<a class="dropdown-item" href="#" id="fileImage">
									<span>파일에서 사진 선택</span><br>
									<input type="file" class="filepond" name="file" id="profileImageInput" />
								</a>
							</div>
						</div>
						<!-- 닉네임 변경 -->
						<input type="text" id="nickname" value="${sessionScope.userNickname}"><br>
					</div>
					<!-- 변경 버튼 -->
					<div class="form-footer">
						<button id="updateButton" class="btn btn-block btn-outline-secondary">변경</button>
					</div>
				</div>
				<div class="profile-message" id="profileMessage"></div>
			</div>
		</div>
	</main>
</body>
<div class="hidden-data" id="currentNickname" data-currentNickname="${sessionScope.userNickname}"></div>
<div class="hidden-data" id="contextPath" data-context-path="${pageContext.request.contextPath}"></div>
<!-- FilePond -->
<script src="https://unpkg.com/filepond/dist/filepond.min.js"></script>
<script src="<c:url value="/resources/js/updateProfile.js"/>"></script>
</html>