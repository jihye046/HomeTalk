const activeClass = "fa-solid" // ♥
const inactiveClass = "fa-regular" // ♡
const bId = document.querySelector("#bId").getAttribute("data-bId")

/* 좋아요
================================================== */
const likeBtn = document.querySelector(".icon-button.like") // <button>
const likeIcon = likeBtn.querySelector(".fa-heart") // <i>
const isLiked = document.querySelector("#isLiked").
				getAttribute("data-isLiked") === "true"

	// 초기 화면
const setInitialLikeIcon = () => {
    if(isLiked){
        likeIcon.classList.add(activeClass)
    } else {
        likeIcon.classList.add(inactiveClass)
    }
}

	// 좋아요 클릭했을 때
likeBtn.addEventListener('click', function(){
	const likeCount = likeBtn.querySelector(".count")
	if(likeIcon.classList.contains(activeClass)){ // 좋아요 눌려진 상태
		$.ajax({
			type: "post",
			data: {
				bId: bId
			},
			url: "/board/removeLike",
			success: function(data){
				likeIcon.classList.remove(activeClass)
				likeIcon.classList.add(inactiveClass)
				likeCount.textContent = data // 카운트
			},
			error: function(error){
				console.error("fail", error)
			}
		})
    } else if(likeIcon.classList.contains(inactiveClass)){ // 좋아요 안눌려진 상태
        $.ajax({
			type: "post",
			data: {
				bId: bId 
			},
			url: "/board/addLike",
			success: function(data){
				likeIcon.classList.remove(inactiveClass)
				likeIcon.classList.add(activeClass)
                likeCount.textContent = data // 카운트
			},
			error: function(error){
				console.error("fail", error)
			}
		})
    }
})

/* 북마크
================================================== */
const bookmarkBtn = document.querySelector(".icon-button.bookmark") // <button>
const bookmarkIcon = bookmarkBtn.querySelector(".fa-bookmark") // <i>
const isBookmarked = document.querySelector("#isBookmarked").
					getAttribute("data-isBookmarked") === "true"

	// 초기 화면
const setInitialBookmarkIcon = () => {
	if(isBookmarked){
		bookmarkIcon.classList.add(activeClass)
	} else {
		bookmarkIcon.classList.add(inactiveClass)
	}
}

	// 북마크 클릭했을 때
bookmarkBtn.addEventListener('click', function(){
	const bookmarkCount = bookmarkBtn.querySelector('.count')

	if(bookmarkIcon.classList.contains(activeClass)){
		fetch('/board/removeBookmark', { // 북마크 눌려진 상태
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
			bookmarkIcon.classList.remove(activeClass)
			bookmarkIcon.classList.add(inactiveClass)
			bookmarkCount.textContent = data.bookmarkCount
		})
		.catch(error => {
			console.error('Error: ', error)
		})
	} else {
		fetch('/board/addBookmark', { // 북마크 안눌려진 상태
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
			bookmarkIcon.classList.remove(inactiveClass)
			bookmarkIcon.classList.add(activeClass)
			bookmarkCount.textContent = data.bookmarkCount
		})
		.catch(error => {
			console.error(`Error: ${error}`)
		})
	}
})

/* 공유
================================================== */
const shareBtn = document.querySelector(".share")
const shareOptionDiv = document.querySelector(".share-option-div")

const bTitle = document.querySelector("#bTitle").getAttribute("data-bTitle")
const bContent = document.querySelector("#bContent").getAttribute("data-bContent")
window.bContent = bContent

shareBtn.addEventListener('click', function(){
	shareBtn.classList.toggle('active')

	if(shareBtn.classList.contains('active')){
		shareOptionDiv.style.display = 'flex'
	} else {
		shareOptionDiv.style.display = 'none'
	}
})

	// 네이버 공유
function shareNaver() {
	const title = document.querySelector("#bTitle").getAttribute("data-bTitle")
	const currentUrl = window.location.href
	
	if (!currentUrl) {
        console.error("Current URL is null or undefined")
        return; 
    }
	
	window.open(
		"https://share.naver.com/web/shareView?url=" 
		+ (encodeURI(encodeURIComponent(currentUrl))) 
		+ "&title=" 
		+ encodeURI(title),
		'_blank' // 새 창에서 열기
	)
}

	// 카카오 공유
function shareKakao() {
	const imgUrl = getThumbnailUrl()
	const currentURL = window.location.href // 쿼리 포함한 전체 URL
	const origin = window.location.origin // 현재 도메인 + 프로토콜만 추출 (예: https://yourdomain.com)
	getKakaoKey()
		.then(kakaoKey => {
			// 초기화가 안되었을 때만 초기화
			if(!Kakao.isInitialized()) {
				Kakao.init(kakaoKey) 
			}
			
			Kakao.Link.sendDefault({
				objectType: 'feed',
				itemContent: {
					profileText: 'HomeTalk',
					profileImageUrl: `${origin}/resources/images/logo2.png`
				},
				content: {
					title: '',
					description: bTitle,
					imageUrl: imgUrl,
					link: {
	                    mobileWebUrl: currentURL,
	                    webUrl: currentURL,
	                },
				},
				buttons: [
	                {
	                    title: '웹으로 보기',
	                    link: {
	                        mobileWebUrl: currentURL,
	                        webUrl: currentURL,
	                    },
	                },
	            ],
	            installTalk: true,
			})
		})
}

function getThumbnailUrl() {
	// 임시 DOM 요소를 만들어 bContent의 HTML을 파싱
    const tmpTag = document.querySelector("#tempDiv")
    tmpTag.innerHTML = bContent
    
	// 게시글에 이미지가 없는 경우 설정할 기본 이미지
	const defaultImageUrl = 'https://buly.kr/611YAUp'
	
	// 첫 번째 img 요소의 src를 가져오고, 없을 경우 기본 이미지 URL 반환
	const imgElement = tmpTag.querySelector('img')
	return imgElement ? imgElement.src : defaultImageUrl
}

function getKakaoKey() {
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "post",
			url: "/board/getKakaoKey",
			success: function(kakaoKey){
				resolve(kakaoKey)
			},
			error: function(error){
				reject(error)
			}
		})
	})
}

/* 링크
================================================== */
const linkBtn = document.querySelector(".icon-button.link")
linkBtn.addEventListener('click', function(){
	const url = window.location.href
	navigator.clipboard.writeText(url)
		.then(() => {
			copyMessage.classList.add('show')
			setTimeout(() => {
				copyMessage.classList.remove('show')
			}, 2000);
		})
		.catch(err => {
			alert('다시 시도해주세요.')
		})
})

/* 맨 위로
================================================== */
const backToTopBtn = document.querySelector(".back-to-top")
backToTopBtn.addEventListener('click', function(){
	window.scrollTo({top:0, behavior: "smooth"})
})

/* 페이지 로드 시 실행될 함수
================================================== */
setInitialLikeIcon()
setInitialBookmarkIcon()