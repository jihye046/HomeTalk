const createElement = document.querySelector("#createResult") 
let createResult = createElement ? createElement.getAttribute("data-create-result") : null

if(createResult == "true") {
	alert("게시글이 등록되었습니다.")
}

const deleteResult = document.querySelector("#deleteResult").getAttribute("data-delete-result")

if(deleteResult == "true"){
	alert("게시글이 삭제되었습니다.")
}

const userId = document.querySelector("#userId").getAttribute("data-userId")
const userNickname = document.querySelector("#userNickname").getAttribute("data-userNickname")

if(userId) {
	document.querySelector("#welcomeText").innerHTML = `<a class="menubar-button-primary" href="/user/myPage">${userNickname}</a><span style="font-size: 16px;">님 환영합니다.<span>`
} else {
	//document.querySelector("#welcomeText").innerHTML = "로그인"		
}

/* 가이드
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
	// 로컬스토리지에 팝업 숨김 만료 시간 저장 또는 삭제
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

	// 팝업 표시
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

	// 오늘 주요 날씨 정보
const updateCurrentWeatherInfo = (currentWeatherDto) => {
	const weather = currentWeatherDto.weather[0].main // Clear, Wind, Clouds, Rain, Sno
	updateMainImageByWeather(weather)
}

	// 날씨에 따라 메인 화면 변경
const updateMainImageByWeather = (weather) => {
	const path = document.querySelector("#contextPath").getAttribute("data-context-path")
	const video = document.querySelector("#weatherVideo")
	const basePath = `${path}/resources/images/weather/`
	const extension = '.mp4'
	const updateSrc = basePath + weather + extension

	video.src = updateSrc
}

	// 서버에서 날씨 정보 가져오기
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

	// 현위치 정보 가져오기
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

/* 목록 정렬
================================================== */

const sort_latest = document.querySelector("#sort_latest")
const sort_hit = document.querySelector("#sort_hit")

	// 최신순 기본 설정
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

sort_latest.addEventListener('click',function(){
	if(!this.classList.contains('active')){
		sort('latest') // 현재 페이지 정렬
		updateLatestBtnClass()
		sortTypeInput.value = 'latest' // 검색할 때 정렬 유지
		updatePaginationLinks('latest') // 페이지 이동 시 sortType 파라미터값 유지
	}
})

sort_hit.addEventListener('click', function(){
	if(!this.classList.contains('active')){
		sort('hit')
		updateHitBtnClass()
		sortTypeInput.value = 'hit'
		updatePaginationLinks('hit')
	}
})

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

const updateSortBtnStyle = (sortType) => {
	if(sortType == 'latest'){
		updateLatestBtnClass()
	} else if(sortType == 'hit'){
		updateHitBtnClass()
	}
}

const updateLatestBtnClass = () => {
	sort_latest.classList.remove('btn', 'btn-dark')
	sort_latest.classList.add('active', 'btn', 'btn-dark')
	
	sort_hit.classList.remove('btn', 'btn-dark', 'active')
	sort_hit.classList.add('btn')
}

const updateHitBtnClass = () => {
	sort_hit.classList.remove('btn', 'btn-dark')
	sort_hit.classList.add('active', 'btn', 'btn-dark')
	
	sort_latest.classList.remove('btn', 'btn-dark', 'active')
	sort_latest.classList.add('btn')
}

	// 현재 위치한 페이지 정렬
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

const updateSortedByHits = (pagingList, paging) => {
	const hitContainer = document.querySelector("#hitContainer")
	hitContainer.innerHTML = ''
	let tableOutput = `<div class="card-container">`
	pagingList.forEach(function(dto){
		tableOutput +=
	  		`
	  			<figure class="snip1518 hover">
					<div class="image" data-content="${dto.bContent}">
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
					<a href="/board/detailBoard?bId=${dto.bId}&bGroup=${dto.bGroup}&page=${paging.page}"></a>
				</figure>
	  		`
	})
	tableOutput += `</div>`
	hitContainer.innerHTML = tableOutput
	
	updateBoardCards()
}

/* 페이징
================================================== */
const pagination = (paging) => {
	let output = `<nav><ul class="pagination justify-content-center">`
	
	// Previous 버튼 추가
	if(paging.page <= 1){
		output += `<li class="page-item"></li>`
	} else {
		let previousPageLink = (paging.page - 5 <= 1) ?
			`<a class="page-link" href="/board/paging?page=${paging.page-1}"> < </a>` :
        	`<a class="page-link" href="/board/paging?page=${paging.page-5}"> < </a>`

		output += `<li class="page-item">${previousPageLink}</li>`
	}
	
	// 페이지번호 추가 및 링크 생성
	for(let i = paging.startPage; i <= paging.endPage; i++){
	    let pagingLink = (i == paging.page) ? 
	    	`<span class="page-link" style="background-color: #ad9f94; pointer-events: none;">${i}</span>` :
	    	`<a class="page-link" href="/board/paging?page=${i}">${i}</a>`
	        
	    output += `<li class="page-item">${pagingLink}</li>`
	}
	
	// Next 버튼 추가
	if(paging.page >= paging.maxPage){
		paginationOutput += `<li class="page-item"></li>`
	} else {
		let nextPageLink = (paging.page + 5 >= paging.maxPage) ? 
			`<a class="page-link" href="/board/paging?page=${paging.maxPage}"> > </a>` :
			`<a class="page-link" href="/board/paging?page=${paging.page + 5}"> > </a>`
			
		output += `<li class="page-item">${nextPageLink}</li>`
	}
	return output
}

const badge = document.querySelector(".badge")

// 게시글 이미지
const updateBoardCards = () => {
	const boardCards = document.querySelectorAll('.image')
	boardCards.forEach(function (card) {
		const bContent = card.getAttribute('data-content')
		const parser = new DOMParser()
		const doc = parser.parseFromString(bContent, 'text/html') // HTML로 파싱
		const firstImg = doc.querySelector('img') // 첫 번째 <img> 태그 찾기
		
		if (firstImg) {
			const imageSrc = firstImg.src
			card.innerHTML = `<img src="${imageSrc}" alt="image"/>`
		} else {
			//card.innerHTML = '<img src="https://buly.kr/1RDn8CU" alt="image">' // 기본 설정 이미지 있음
			card.innerHTML = '<img src="https://i.seadn.io/gae/OGpebYaykwlc8Tbk-oGxtxuv8HysLYKqw-FurtYql2UBd_q_-ENAwDY82PkbNB68aTkCINn6tOhpA8pF5SAewC2auZ_44Q77PcOo870?auto=format&dpr=1&w=1000" alt="image">' // 기본 설정 이미지 없음 
		}
	})
}

/* 페이지 로드 시 실행될 함수
================================================== */
showAutoPopup()
updateBoardCards()
getCurrentLocationAndFetchWeather()