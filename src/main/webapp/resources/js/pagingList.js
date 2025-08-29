/* 게시글 등록 결과 알림
================================================== */
const createElement = document.querySelector("#createResult") 
let createResult = createElement ? createElement.getAttribute("data-create-result") : null

if(createResult == "true") {
	alert("게시글이 등록되었습니다.")
}

/* 게시글 삭제 결과 알림
================================================== */
const deleteResult = document.querySelector("#deleteResult").getAttribute("data-delete-result")

if(deleteResult == "true"){
	alert("게시글이 삭제되었습니다.")
}


/* 로그인 환영 메시지 표시
================================================== */
	// 로그인 사용자 정보 가져오기
const userId = document.querySelector("#userId").getAttribute("data-userId")
const userNickname = document.querySelector("#userNickname").getAttribute("data-userNickname")

if(userId) {
	// 환영 메시지 표시
	document.querySelector("#welcomeText").innerHTML = 
	`
		<a class="menubar-button-primary" href="/user/myPage">${userNickname}</a><span style="font-size: 16px;">님 환영합니다.<span>
	`
} else {
	// 로그인 안 된 경우 기본 메시지(필요 시 활성화)
	//document.querySelector("#welcomeText").innerHTML = "로그인"		
}

/* 가이드 투어 설정 및 실행
================================================== */
const driver = window.driver.js.driver

const tourSteps = [
	{
		element: ".weather-container",
		popover: {
			title: "🌦️ 실시간 날씨 정보 (위치 권한 필요)",
			description: 
			`
				이 곳에서는 현재 위치의 실시간 날씨 상황을 반영한 영상을 볼 수 있습니다.<br>
				만약 이 공간이 비어있다면 브라우저에서 위치 권한이 <strong>거부</strong>된 상태일 수 있습니다.<br><br>
				실시간 날씨 정보를 확인하려면, 아래 방법을 따라 위치 권한을 <strong>허용</strong>으로 변경해 주세요.
			`,
			showButtons: ['next', 'close']
		}
	},
	{
		element: ".weather-container",
		popover: {
			title: "위치 권한 변경 방법",
		    description: 
	    	`
	    		• <strong>Chrome / Edge</strong>: 주소창 왼쪽 아이콘 → 사이트 설정 → 위치 허용<br>
	    		• <strong>Safari</strong>: 메뉴 → 설정 → 웹사이트 → 위치 권한<br><br>
	    		🔄 변경 후 페이지를 새로고침해주세요.	
	    	 `,
			showButtons: ['previous', 'next', 'close']
		}
	},
	{
		element: ".card-container",
		popover: {
			title: "회원들의 게시글 둘러보기",
			description:
			`
				이곳은 회원님들이 작성한 다양한 게시글을 한눈에 확인하는 공간입니다. <br>
				궁금한 점이 있다면 검색 기능을 활용하거나, <br>
				직접 새로운 게시글을 작성하여 소통해보세요!✨
			` ,
			showButtons: ['previous', 'next', 'close']
		}
	},
	{
		element: ".weather-nav-item",
		popover: {
			title: "☀️ 날씨 페이지 바로가기",
			description: 
			`
				이곳을 클릭하여 현재 위치 기반의 <strong>상세한 날씨 정보</strong>와 <br>
				주간, 시간별 예보를 확인해보세요.
			`,
			showButtons: ['previous', 'next', 'close']
		}
	},
	{
		element: "#chatIconWrapper",
		popover: {
			title: "💬 실시간 채팅으로 소통하기",
			description: 
			`
				이곳을 클릭하면 다른 사용자들과 나눴던 <strong>이전 대화 내용을 불러오고</strong>,
				<strong>실시간 대화</strong>도 이어갈 수 있어요.<br><br>
				궁금한 점을 물어보거나 의견을 나누고 싶을 때 언제든지 이용해보세요. 😊 <br><br>
				<strong>※ 이 기능은 로그인 후에만 사용 가능합니다.</strong>
			`,
			showButtons: ['previous', 'next', 'close']
		}
	}
]

const driverObj = driver({
	popoverClass: 'driverjs-theme',
	showProgress: true,
	steps: tourSteps,
	allowClose: true,
	side: "bottom",
	align: "start",
	nextBtnText: '다음',
	prevBtnText: '이전',
	doneBtnText: '완료',
	closeBtnText: '닫기'
})

const guideButton = document.querySelector("#start-guide-button")
guideButton.addEventListener('click', () => {
	driverObj.drive()	
})

/* 자동 팝업
================================================== */
	// 체크박스 상태에 따라 로컬스토리지에 팝업 숨김 만료 시간 저장/삭제
const updatePopupHideTime = (hideForDayCheckbox) => {
	if(hideForDayCheckbox.checked) {
		const expireTime = Date.now() + 24 * 60 * 60 * 1000 // 24시간 후
		localStorage.setItem('hidePopupUntil', expireTime.toString())
	} else {
		localStorage.removeItem('hidePopupUntil')
	}
	popup.classList.remove('active')
}

	// 자동 팝업 표시 및 닫기
const showAutoPopup = () => {
	const popup = document.querySelector("#popup")
	const popupCloseBtn = document.querySelector("#closePopup")
	const hideForDayCheckbox = document.querySelector("#hideForDay")
	const hideUntil = localStorage.getItem('hidePopupUntil')
	const now = Date.now()

	// 로컬스토리지에 저장된 시간이 있고, 현재 시간이 만료시간보다 작으면 팝업 숨김
	if(hideUntil && now < Number(hideUntil)){
		popup.classList.remove('active')
	} else {
		setTimeout(() => {
			popup.classList.add('active')
		}, 500)
	}

	// 팝업 닫기
	if(popup && popupCloseBtn) {
		// 닫기 버튼 클릭 시
		popupCloseBtn.addEventListener('click', () => {
			updatePopupHideTime(hideForDayCheckbox)
		})

		// 팝업 배경 클릭 시
		popup.addEventListener('click', (event) => {
			if(event.target == popup) {
				updatePopupHideTime(hideForDayCheckbox)
				popup.classList.remove('active')
			}
		})
	}
}

/* 주요 날씨에 따라 메인 설정
================================================== */
	// 위도, 경도 가져오기
const weatherLocation = (position) => {
	const locationObj = {
		latitude: position.coords.latitude,
		longitude: position.coords.longitude
	}
	return locationObj
}

	// 서울(기본) 위도, 경도
const weatherDefaultLocation = () => {
	const SEOUL_LATITUDE = 37.5665
	const SEOUL_LONGITUDE = 126.9780
	const defaultLocationObj = {
		latitude: SEOUL_LATITUDE,
		longitude: SEOUL_LONGITUDE
	}
	return defaultLocationObj
}

	// 현재 날씨 정보 업데이트 (메인 화면 변경)
const updateCurrentWeatherInfo = (currentWeatherDto) => {
	const weather = currentWeatherDto.weather[0].main // Clear, Wind, Clouds, Rain, Sno
	updateMainImageByWeather(weather)
}

	// 날씨에 따라 메인 비디오 소스 변경
const updateMainImageByWeather = (weather) => {
	const video = document.querySelector("#weatherVideo")
	const bucketName = document.querySelector("#bucketName").getAttribute("data-bucket-name")
	let basePath = null

	if(!bucketName){
		// local, vm
		const path = document.querySelector("#contextPath").getAttribute("data-context-path")
		basePath = `${path}/resources/images/weather/`
	} else {
		// gcs
		basePath = `https://storage.googleapis.com/${bucketName}/weather/`
	}
	const extension = '.mp4'
	const updateSrc = basePath + weather + extension
	
	video.src = updateSrc
}

	// 서버에 날씨 정보 요청
const getWeatherInfo = (latitude, longitude) => {
	if (!(latitude) || !(longitude)) {
		const defaultLocation  = weatherDefaultLocation()
		latitude = defaultLocation.latitude;
		longitude = defaultLocation.longitude;
	}

	$.ajax({
		type: "get",
		url: "/weather/getCurrentWeather",
		data: {latitude, longitude},
		success: function(currentWeatherDto){
			updateCurrentWeatherInfo(currentWeatherDto)
		},
		error: function(error){
			console.error("날씨 정보 가져오기 실패", error)
		}
	})
}

	// 현재 위치 정보 가져와서 날씨 정보 요청
const getCurrentLocationAndFetchWeather = () => {
	if ("geolocation" in navigator) {
		navigator.geolocation.getCurrentPosition((position) => {
			const locationObj = weatherLocation(position)
			getWeatherInfo(locationObj.latitude, locationObj.longitude)
		})
	} else {
		console.log("현재 위치 사용 불가능")
	}
}

/* 게시글 정렬 및 버튼 처리
================================================== */

const sort_latest = document.querySelector("#sort_latest")
const sort_hit = document.querySelector("#sort_hit")

	// 페이지 로드 시 최신순 기본 활성화
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("sort_latest").classList.add("active")
	let page = paging
	let gubun = searchGubun
	let text = searchText
	
	updatePaginationLinks(sortType)
	updateSortBtnStyle(sortType)
	updateBoardCards()
})

const sortTypeInput = document.querySelector('#sortTypeInput')

	// 최신순 버튼 클릭 시
sort_latest.addEventListener('click',function(){
	if(!this.classList.contains('active')){
		sort('latest') // 현재 페이지 정렬
		updateLatestBtnClass()
		sortTypeInput.value = 'latest' // 검색할 때 정렬 유지
		updatePaginationLinks('latest') // 페이지 이동 시 sortType 파라미터값 유지
	}
})

	// 조회순 버튼 클릭 시
sort_hit.addEventListener('click', function(){
	if(!this.classList.contains('active')){
		sort('hit')
		updateHitBtnClass()
		sortTypeInput.value = 'hit'
		updatePaginationLinks('hit')
	}
})

	// 페이지네이션 링크에 정렬 타입 쿼리 파라미터 추가
const updatePaginationLinks = (sortType) => {
	document.querySelectorAll('.page-link').forEach(function(link){
		try{
			let url = new URL(link.href)
			url.searchParams.set('sortType', sortType)
			link.href = url.toString()
		} catch(error){
			console.log("invalid url:", link.href)
		}
	})
}

/* 활성화된 버튼 스타일 업데이트
================================================== */
const updateSortBtnStyle = (sortType) => {
	if(sortType == 'latest'){
		updateLatestBtnClass()
	} else if(sortType == 'hit'){
		updateHitBtnClass()
	}
}

	// 최신순 버튼 활성화, 조회순 버튼 비활성화
const updateLatestBtnClass = () => {
	sort_latest.classList.remove('btn', 'btn-dark')
	sort_latest.classList.add('active', 'btn', 'btn-dark')
	
	sort_hit.classList.remove('btn', 'btn-dark', 'active')
	sort_hit.classList.add('btn')
}

	// 최신순 버튼 비활성화, 조회순 버튼 활성화
const updateHitBtnClass = () => {
	sort_hit.classList.remove('btn', 'btn-dark')
	sort_hit.classList.add('active', 'btn', 'btn-dark')
	
	sort_latest.classList.remove('btn', 'btn-dark', 'active')
	sort_latest.classList.add('btn')
}

/* Date 객체를 "YYYY-MM-DD" 형식의 문자열로 변환
================================================== */
const formatDate = (timestamp) => {
	const year = timestamp.getFullYear()
	const month = String(timestamp.getMonth() + 1).padStart(2, '0')
	const day = String(timestamp.getDate()).padStart(2, '0')
	
	return `${year}-${month}-${day}`
}

/* 정렬 후 게시글 목록 업데이트
================================================== */
const updateSortedByHits = (pagingList, paging) => {
	const hitContainer = document.querySelector("#hitContainer")
	hitContainer.innerHTML = ''
	let tableOutput = `<div class="card-container">`
	pagingList.forEach(function(dto){
		const formattedDate = formatDate(new Date(dto.bDate))
		tableOutput +=
	  		`
	  			<figure class="snip1518 hover">
					<div class="image" data-content="${dto.bContent}">
					</div>
					<figcaption>
						<div class="post-bName">${dto.unickName}</div>
						<div class="post-bTitle">${dto.bTitle}</div>
						<footer>
							<div class="create-date">${formattedDate}</div>
							<div class="icons">
								<div class="views"><i class="ion-eye"></i>${dto.bHit}</div>
								<div class="love"><i class="ion-heart"></i>${dto.bLike}</div>
								<div class="comment"><i class="fa-thin fa-comment fa-2xs"></i>${dto.commentCount}</div>
							</div>
						</footer>
					</figcaption>
					<a href="/board/detailBoard?bId=${dto.bId}&bGroup=${dto.bGroup}&page=${paging.page}&bName=${dto.bName}"></a>
				</figure>
	  		`
	})
	tableOutput += `</div>`
	hitContainer.innerHTML = tableOutput
	
	updateBoardCards()
}

/* 게시글 정렬 AJAX 요청 및 목록 업데이트
================================================== */
const sort = (type) => {
	let page = paging
	let gubun = searchGubun
	let text = searchText
	
	$.ajax({
		type: "get",
		data: {
			page: page,
			searchGubun: gubun,
			searchText: text,
			sortType: type
		},
		url: "/board/paging/ajax",
		dataType: "json",
		success: function(data){
			let pagingList = data["sort_hitPagingList"]
            let paging = data["pageDto"]
			updateSortedByHits(pagingList, paging)
		},
		error: function(error){
			console.error("sort_hit fail", error)
		}
	})
}

/* 페이징
================================================== */
const pagination = (paging) => {
	let output = `<nav><ul class="pagination justify-content-center">`
	
	// 이전 버튼: 현재 페이지가 1이면 비활성화, 아니면 이전 또는 5페이지 전으로 이동
	if(paging.page <= 1){
		output += `<li class="page-item"></li>`
	} else {
		let previousPageLink = (paging.page - 5 <= 1) ?
			`<a class="page-link" href="/board/paging?page=${paging.page-1}"> < </a>` :
        	`<a class="page-link" href="/board/paging?page=${paging.page-5}"> < </a>`

		output += `<li class="page-item">${previousPageLink}</li>`
	}
	
	// 페이지 번호 버튼 생성
	for(let i = paging.startPage; i <= paging.endPage; i++){
	    let pagingLink = (i == paging.page) ? 
	    	`<span class="page-link" style="background-color: #ad9f94; pointer-events: none;">${i}</span>` :
	    	`<a class="page-link" href="/board/paging?page=${i}">${i}</a>`
	        
	    output += `<li class="page-item">${pagingLink}</li>`
	}
	
	// 다음 버튼: 현재 페이지가 최대 페이지면 비활성화, 아니면 다음 또는 5페이지 후로 이동
	if(paging.page >= paging.maxPage){
		output += `<li class="page-item"></li>`
		// paginationOutput += `<li class="page-item"></li>`
	} else {
		let nextPageLink = (paging.page + 5 >= paging.maxPage) ? 
			`<a class="page-link" href="/board/paging?page=${paging.maxPage}"> > </a>` :
			`<a class="page-link" href="/board/paging?page=${paging.page + 5}"> > </a>`
			
		output += `<li class="page-item">${nextPageLink}</li>`
	}
	return output
}

const badge = document.querySelector(".badge")

/* 게시글의 첫 번째 이미지 추출하여 카드에 표시,
   첫 번째 이미지가 없으면 기본 이미지로 대체
================================================== */
const updateBoardCards = () => {
	const boardCards = document.querySelectorAll('.image')
	boardCards.forEach(function (card) {
		const bContent = card.getAttribute('data-content')
		const parser = new DOMParser()
		const doc = parser.parseFromString(bContent, 'text/html') // HTML로 파싱
		const firstImg = doc.querySelector('img') // 첫 번째 <img> 태그 찾기
		
		if (firstImg) {
			const imageSrc = firstImg.src
			card.innerHTML = `<img src="${imageSrc}" loading="lazy" alt="image"/>`
		} else {
			// 기본 이미지
			card.innerHTML = '<img src="https://i.seadn.io/gae/OGpebYaykwlc8Tbk-oGxtxuv8HysLYKqw-FurtYql2UBd_q_-ENAwDY82PkbNB68aTkCINn6tOhpA8pF5SAewC2auZ_44Q77PcOo870?auto=format&dpr=1&w=1000" loading="lazy" alt="image">' // 기본 설정 이미지 없음 
		}
	})
}

/* 페이지 로드 시 실행될 함수
   - 자동 팝업 표시
   - 게시글 카드 이미지 표시
   - 현재 위치 기반 날씨 정보 가져오기
================================================== */
showAutoPopup()
updateBoardCards()
getCurrentLocationAndFetchWeather()