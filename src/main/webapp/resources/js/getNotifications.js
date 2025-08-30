
document.addEventListener('DOMContentLoaded', () => {
    const notificationListJson = document.querySelector("#notificationAllList").getAttribute("data-notificationAllList")
    const container = document.querySelector(".noti-list") // <ul>
    const spinner = document.querySelector(".spinner-grow")
    const markAllAsReadBtnPage = document.querySelector("#markAllAsReadBtnPage")

    // 상대적인 시간으로 변환
    const timeAgo = (timestamp) => {
        const now = new Date()
        const diff = now - timestamp // 얼마나 시간이 지났는지

        const seconds = Math.floor(diff / 1000)
        const minutes = Math.floor(seconds / 60)
        const hours = Math.floor(minutes / 60)
        const days = Math.floor(hours / 24)

        if(seconds < 60){
            return "방금 전"
        } else if(minutes < 60){
            return `${minutes}분 전`
        } else if(hours < 60){
            return `${hours}시간 전`
        } else if(days < 7){
            return `${days}일 전`
        } else {
            const year = timestamp.getFullYear()
            const month = String(timestamp.getMonth() + 1).padStart(2, '0')
            const day = String(timestamp.getDate()).padStart(2, '0')

            return `${year}-${month}-${day}`
        }
    }

    // 알림 목록 UI 업데이트
    const renderNotificationList = (notificationList) => {
        let output = ''

        notificationList.forEach((notificationDto) => {
            // 상대적인 시간으로 변환
            const timestamp = new Date(notificationDto.createdAt)
            const date = timeAgo(timestamp)

            // 읽음 여부 표시 클래스
            let readClass = notificationDto.isRead == 'N' ? 'unread' : ''

            // type이 댓글인 경우 미리보기로 보여줄 내용
            let commentSnippet = ''

            // 알림 메시지
            let notificationText = ''

            if(notificationDto.type == 'LIKE'){
                notificationText = 
                `
                    <strong class="sender-name">${notificationDto.senderId}</strong>님이 게시글을 좋아합니다.
                `
            } else if(notificationDto.type == 'COMMENT'){
                const data = JSON.parse(notificationDto.dataJson)
                commentSnippet = data.commentSnippet 
                notificationText = 
                `
                    <strong class="sender-name">${notificationDto.senderId}</strong>님이 댓글을 달았습니다:<br/>
                    <span class="comment-preview-text">${commentSnippet}</span>
                `
            } else if(notificationDto.type == 'CHILD_COMMENT'){
                const data = JSON.parse(notificationDto.dataJson)
                commentSnippet = data.childcommentSnippet
                notificationText =
                `
                    <strong class="sender-name">${notificationDto.senderId}</strong>님이 답글을 달았습니다:<br/>
                    <span class="comment-preview-text">${commentSnippet}</span>
                `
            }

            output += 
            `
                <li class="noti-item ${readClass}">
					<a href="${notificationDto.link}" data-notification-id="${notificationDto.notificationId}">
						<div class="noti-content">
							<span class="noti-main-text">${notificationText}</span>
						</div>
						<div class="noti-meta">
							<span class="notification-time">${date}</span>
							${readClass ? '<span class="new-badge">N</span>' : ''}
						</div>
					</a>
				</li>
            `
        })

        return output
    }

    // 상단 메뉴바에 있는 알림 아이콘의 count 및 스타일 업데이트
	const badge = document.querySelector("#notificationBadge")
	const showUnreadCount = (unreadCount) => {
		if(unreadCount > 0){
			badge.style.display = 'block'
			badge.textContent = unreadCount
		} else {
			badge.style.display = 'none'
			badge.textContent = ''
		}
	}
    
    // '모두 읽음' 버튼 클릭 이벤트 핸들러
    const markAllRead = () => {
        markAllAsReadBtnPage.addEventListener('click', () => {
            const unreadElements = document.querySelectorAll(".unread")
            if(unreadElements.length > 0){
                axios.patch('/notification/markAllAsRead')
				.then(response => {
					const isUpdated = response.data.allReadStatusesUpdated
					const unreadCount = response.data.unreadCount
					if(isUpdated){
						unreadElements.forEach((unreadElement) => {
							unreadElement.classList.remove('unread')
							showUnreadCount(unreadCount)
						})
					}
				})
				.catch(error => {
					console.error('error: ', error)
				})
            } else {
                const noNotificationsMessage = document.querySelector("#noNotificationsMessage")
                const msg = '더 이상 읽을 알림이 없습니다.'
                noNotificationsMessage.textContent = msg
                noNotificationsMessage.classList.add('show')
                
                setTimeout(() => {
                    noNotificationsMessage.classList.remove('show')
                }, 3000)
            }
        })
    }

    // 클릭 시 읽음 처리
    const handleNotificationClick = () => {
        const aElements = container.querySelectorAll("a")
        aElements.forEach((aElement) => {
            aElement.addEventListener('click', (e) => {
                e.preventDefault()

                const liElement = aElement.closest(".noti-item")
                const notificationId = aElement.getAttribute("data-notification-id")
                const href = aElement.getAttribute("href")

                // 안 읽음 상태일 때만 서버로 읽음 처리 요청
                if(liElement.classList.contains('unread')){
                    const data = {
                        notificationId: notificationId,
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
                window.location.href = href
            })
        })
    }

    // 스피너 보여주기
    const showSpinner = () => {
        spinner.classList.add('show')
    }

    // 스피너 숨김
    const hideSpinner = () => {
        spinner.classList.remove('show')
    }

    // 다음 알림 데이터 불러오기
    const loadMoreNotifications = (page, observer) => {
        // 옵저버 종료 후 데이터 없음 알림 메시지
        const handleNoMoreNotifications = () => {
            if(!document.querySelector(".no-more-notifications")){
                const noMoreMessage = 
                `
                    <li class="no-more-notifications">더 이상 알림이 없습니다.</li>
                `
                container.insertAdjacentHTML('beforeend', noMoreMessage)
            }
            observer.disconnect()  
        }

        const params = {
            page: page,
            size: 10
        }

        showSpinner() // 요청 직접에 스피너 보이기
        axios.get('/notification/getAllNotifications/axios', { params })
            .then(response => {
                const notificationList = response.data
                if(notificationList.length > 0){
                    let output = renderNotificationList(notificationList)
                    container.insertAdjacentHTML('beforeend', output)

                    // 새 sentinel 생성 및 옵저버 연결
                    const newSentinel = document.createElement('div')
                    newSentinel.id = 'scroll-sentinel'
                    container.insertAdjacentElement('beforeend', newSentinel)
                    newSentinel.insertAdjacentElement('beforebegin', spinner)
                    observer.observe(newSentinel)

                    // 받은 데이터가 요청한 size보다 작으면 더 이상 데이터 없으므로 옵저버 종료
                    if(notificationList.length < params.size){
                        handleNoMoreNotifications()
                    }
                } else {
                    // 더 이상 불러올 알림이 없을 경우
                    handleNoMoreNotifications()
                }

                handleNotificationClick()
            })
            .catch(error => {
                console.error('error: ', error)
            })
            .finally(() => {
                hideSpinner()
            })
    }

    // 무한 스크롤
    const initInfiniteScrollObserver = () => {
        let page = 2
        const sentinel = document.querySelector("#scroll-sentinel")

        // 옵저버 등록
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if(entry.isIntersecting){
                    showSpinner()

                    setTimeout(() => {
                        loadMoreNotifications(page, observer)
                        page++
                    }, 1000)
                }
            })
        })

        observer.observe(sentinel)
    }

    const notificationList = JSON.parse(notificationListJson)
    container.innerHTML = '' // 초기화
    let output = ''
    
    if(notificationList.length > 0 && container){
        output = renderNotificationList(notificationList)
        output += 
        `
            <div id="scroll-sentinel"></div>
        `
        container.innerHTML = output

        initInfiniteScrollObserver() // 무한 스크롤
        handleNotificationClick() // 각 알림 클릭 시 읽음 처리
        markAllAsReadBtnPage.classList.remove('hidden') // '모두 읽기' 버튼 표시
        markAllRead() // 모두 읽기    
    } else {
        output = 
        `
            <li class="no-noti">알림 내역이 없습니다.</li>
        `
        container.innerHTML = output
        
        markAllAsReadBtnPage.classList.add('hidden') // '모두 읽기' 버튼 숨기기
    }
    
    
})