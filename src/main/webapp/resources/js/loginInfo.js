(function($) { "use strict";

	$(function() {
		var header = $(".start-style");
		$(window).scroll(function() {    
			var scroll = $(window).scrollTop();
		
			if (scroll >= 10) {
				header.removeClass('start-style').addClass("scroll-on");
			} else {
				header.removeClass("scroll-on").addClass('start-style');
			}
		});
	});		
		
	//Animation
	
	$(document).ready(function() {
		$('body.hero-anime').removeClass('hero-anime');
	});

	//Menu On Hover
		
	$('body').on('mouseenter mouseleave','.nav-item',function(e){
			if ($(window).width() > 750) {
				var _d=$(e.target).closest('.nav-item');_d.addClass('show');
				setTimeout(function(){
				_d[_d.is(':hover')?'addClass':'removeClass']('show');
				},1);
			}
	});	
	
	//Switch light/dark
	
	$("#switch").on('click', function () {
		if ($("body").hasClass("dark")) {
			$("body").removeClass("dark");
			$("#switch").removeClass("switched");
		}
		else {
			$("body").addClass("dark");
			$("#switch").addClass("switched");
		}
	});  
	
})(jQuery); 

document.addEventListener('DOMContentLoaded', () => {
    const userId = document.querySelector("#userId").getAttribute("data-userId")
	const notificationIcon = document.querySelector("#notificationIconWrapper")
	const notificationModal = document.querySelector("#notificationModal")

	// 모달 외부 클릭 시 모달 닫기
	document.addEventListener('click', (event) => {
		if(notificationModal.classList.contains('show') &&
			!notificationModal.contains(event.target) &&
			!notificationIcon.contains(event.target)
		){
			notificationModal.classList.remove('show')
		}
	})

	// 상대적인 시간으로 변환
	const timeAgo = (timestamp) => {
		const now = new Date() // 현재 시간
		const diff = now - timestamp // 얼마나 시간이 지났는지

		const seconds = Math.floor(diff / 1000) 
		const minutes = Math.floor(seconds / 60)
		const hour = Math.floor(minutes / 60)
		const days = Math.floor(hour / 24)

		if(seconds < 60){
			return "방금 전"
		} else if(minutes < 60){
			return `${minutes}분 전`
		} else if(hour < 24){
			return `${hour}시간 전`
		} else if(days < 7){
			return `${days}일 전`
		} else {
			// 오래된 경우 날짜 포맷으로 표시
			const year = timestamp.getFullYear()
			const month = String(timestamp.getMonth() + 1).padStart(2, '0')
			const day = String(timestamp.getDate()).padStart(2, '0')

			return `${year}-${month}-${day}`
		}
	}

	// 알림 목록 UI 업데이트
	const renderNotificationList = (notifications) => {
		let output = ''
		notifications.forEach((notificationDto) => {
			// 상대적인 시간으로 변환
			const timeStamp = notificationDto.createdAt
			const date = timeAgo(timeStamp)

			// 읽음 여부 표시 클래스
			let readClass = notificationDto.isRead == 'N'? 'unread' : ''

			// type이 댓글 또는 답글인 경우 미리보기로 보여줄 내용
			let commentSnippet = ''

			// 알림 메시지
			let notificationText = ''
	
			if(notificationDto.type == 'LIKE'){
				notificationText = 
				`
					<strong>${notificationDto.senderId}</strong>님이 게시글을 좋아합니다.<br>
				`
			} else if(notificationDto.type == 'COMMENT'){
				if(notificationDto.dataJson){
					const parsedData = JSON.parse(notificationDto.dataJson)
					if(parsedData.commentSnippet){
						commentSnippet = parsedData.commentSnippet
					}
				}

				notificationText = 
				`
					<strong>${notificationDto.senderId}</strong>님이 새로운 댓글을 달았습니다:
					<span class="comment-preview-text">"${commentSnippet}"</span>
				`
			} else if(notificationDto.type == 'CHILD_COMMENT'){
				if(notificationDto.dataJson){
					const parsedData = JSON.parse(notificationDto.dataJson)
					if(parsedData.childcommentSnippet){
						commentSnippet = parsedData.childcommentSnippet
					}
				}

				notificationText = 
				`
					<strong>${notificationDto.senderId}</strong>님이 새로운 답글을 달았습니다:
					<span class="comment-preview-text">"${commentSnippet}"</span>
				`
			}

			output += 
			`	
				<li class="notification-item ${readClass}">
					<a href="${notificationDto.link}" data-notification-id="${notificationDto.notificationId}">
						<div class="notification-content">
							<span class="notification-main-text">${notificationText}</span>
						</div>
						<div class="notification-meta">
							<span class="notification-time">${date}</span>
							${readClass ? '<span class="new-badge">N</span>' : ''}
						</div>
					</a>
				</li>
			`
		})
		return output
	}

	// 안 읽은 메시지 총 개수 표시
	const badge = document.querySelector("#notificationBadge")
	const showUnreadCount = (unreadCount) => {
		if(unreadCount > 0){
			// 뱃지에 안 읽은 메시지 수 표시
			badge.style.display = 'block'
			badge.textContent = unreadCount

			// '모두 읽음' 버튼 표시
        	markAllAsReadBtn.style.display = 'block';
		} else {
			badge.style.display = 'none'
			badge.textContent = ''
		}
	}

	const markAllAsReadBtn = document.querySelector("#markAllAsReadBtn")
	// '모두 읽음' 버튼 클릭 이벤트 핸들러
	const markAllAsRead = () => {
		markAllAsReadBtn.addEventListener('click', () => {
			axios.patch('/notification/markAllAsRead')
				.then(response => {
					const isUpdated = response.data.allReadStatusesUpdated
					const unreadCount = response.data.unreadCount
					if(isUpdated){
						document.querySelectorAll(".unread").forEach((unreadElement) => {
							unreadElement.classList.remove('unread')
							showUnreadCount(unreadCount)
						})
					}
				})
				.catch(error => {
					console.error('error: ', error)
				})
		})
	}

	// 안 읽은 메시지가 있다면 '모두 읽기' 버튼 표시
	
	const showMarkAllAsReadButton = (unreadCount) => {
		if(unreadCount > 0){
			markAllAsReadBtn.style.display = 'block'
			markAllAsRead()
		} else {
			markAllAsReadBtn.style.display = 'none'
		}
	}

	// 알림 아이콘 표시
	if(userId && notificationIcon && notificationModal){
		// 로그인한 경우만 알림 아이콘 표시
		notificationIcon.style.display = 'inline-block'
		axios.get('/notification/getNotifications')
			.then(response => {
				// 최초 로드 시 읽지 않은 알림 개수만 가져오기
				const unreadCount = response.data.unreadCount
				showUnreadCount(unreadCount)
			})

			// 알림 데이터 가져오기
		notificationIcon.addEventListener('click', (event) => {
			event.stopPropagation()
			notificationModal.classList.toggle('show')
			
			if(notificationModal.classList.contains('show')){
				axios.get('/notification/getNotifications')
					.then(response => {
						const notifications = response.data.notificationDtos // List<>
						const unreadCount = response.data.unreadCount

						// 초기화
						const notificationListContainer  = notificationModal.querySelector(".notification-list") // <ul>
						notificationListContainer.innerHTML = ''

						// 알림이 없는 경우
						if(notifications.length == 0){
							notificationListContainer.innerHTML = '<li class="no-notifications">새로운 알림이 없습니다.</li>'
							showMarkAllAsReadButton(unreadCount)
							return
						}

						// 알림이 있는 경우
						showMarkAllAsReadButton(unreadCount)
						let output = renderNotificationList(notifications)
						const infoMessage = 
						`
							<li class="notification-info-message">최근 3일 이내의 알림만 표시됩니다.</li>
						`
						output += infoMessage

						notificationListContainer.innerHTML = output
						notificationModal.querySelector(".notification-footer").style.display = 'block' // '모든 알림 보기' 표시
						
						// notificationListContainer.appendChild(infoMessage)
						
						// 클릭 시 읽음 처리 
						const aElements = notificationModal.querySelectorAll(".notification-item a")
						aElements.forEach((a) => {
							a.addEventListener('click', (event) => {
								event.preventDefault()

								const liElement = a.closest(".notification-item")
								const notificationId = a.getAttribute("data-notification-id") // 알림 고유ID
								if(liElement.classList.contains("unread")){
									const data = {
										notificationId: notificationId
									}
									
									axios.patch('/notification/updateReadStatus', data)
										.then(response => {
											const updateResult = response.data.readStatusUpdated
											const unreadCount = response.data.unreadCount
											if(updateResult){
												liElement.classList.remove('unread')
												showUnreadCount(unreadCount)
											}
										})
										.catch(error => {
											console.error('error: ', error)
										})
								} 
								window.location.href = a.getAttribute('href')
							})
						})
				})
			}
		})

		// 모달 닫기 버튼 클릭 시 닫기
		const closeModalBtn = notificationModal.querySelector(".close-modal-btn")
		if(closeModalBtn){
			closeModalBtn.addEventListener('click', () => {
				notificationModal.classList.remove('show')
			})
		}
	} else {
		notificationIcon.style.display = 'none'	
	}

})
