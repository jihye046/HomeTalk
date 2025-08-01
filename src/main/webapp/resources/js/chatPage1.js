/* 전역 변수
================================================== */
let chatList = document.querySelector("#chatList")
let roomList = document.querySelector("#roomList")
let chatRecords = document.querySelector(".chat-records")
const searchTextInput = document.querySelector("#searchTextInput")
const path = document.querySelector("#contextPath").getAttribute("data-context-path")
let ws = null

/* 채팅 아이콘 - 안읽은 메시지 총 개수
================================================== */
const getUnreadMessageTotalCount = () => {
	const userId = document.querySelector("#userId").getAttribute("data-userId")
	
	fetch(`${path}/chat/getUnreadMessageTotalCount?receiver=${userId}`)
	.then(response => response.json())
	.then(data => {
		const badge = document.querySelector(".badge")
		if(data > 0){
			badge.innerHTML = `<div id="notificationBadge" class="notification-badge">${data}</div>`
		} else {
			badge.innerHTML = ''
		}
	})	
	.catch(error => {
		console.error('error: ', error)
	})
} 

/* 채팅 아이콘 클릭시 모달창 열기 
================================================== */
const loadChatRooms = () => {
	document.querySelector('#chatModal').style.display = 'flex'
	roomList.innerHTML = ''
	searchTextInput.value = ''
	getRoomList()
}

/* 채팅 목록 가져오기
================================================== */
const getRoomList = () => {
	const userId = document.querySelector("#userId").getAttribute("data-userId")
	
	fetch(`${path}/chat/getRoomList?userId=${userId}`)
	.then(response => response.json())
	.then(data => {
		roomList.innerHTML = ''
		
		if(data.rooms.length > 0){
			// 채팅방이 있는 경우
			data.rooms.forEach(chatRoomDto => {
				printRoomList(chatRoomDto)
			})
			openChatRoom(data.serverUrl)
		} else {
			// 채팅방이 없는 경우
			roomList.innerHTML = `<div class="no-chat-message">등록된 채팅이 없습니다.</div>`
		}

	})
	.catch(error => {
		console.error('Error:', error)
	})
}

/* 검색 목록 가져오기
================================================== */
const searchUser = () => {
	const searchText = searchTextInput.value
	const userId = document.querySelector("#userId").getAttribute("data-userId")

	roomList.innerHTML = ''		
	if(searchText != '') {
		fetch(`${path}/chat/getRoomList?userId=${userId}&searchText=${searchText}`)
		.then(response => response.json())
		.then(data => {
			data.rooms.forEach(chatRoomDto => {
				printRoomList(chatRoomDto)
			})
			openChatRoom(data.serverUrl)
		})
		.catch(error => {
			console.error('Error:', error)
		})
	} else {
		getRoomList()
	}
}

/* 검색창 엔터키 리스너
================================================== */
const handleSearchOnEnter = () => {
	searchTextInput.addEventListener('keydown', (event) => {
		if(event.key == 'Enter') {
			searchUser()
		}
	})
}

/* 채팅 목록 클릭시 채팅방 열기
================================================== */
const openChatRoom = (serverUrl) => {
	document.querySelectorAll(".chat-room").forEach(room => {
		room.addEventListener('click', function() {
			const roomId = this.getAttribute('data-room-id')
			const otherUserNickname = this.querySelector(".userNickname").textContent 				// 상대방 닉네임
			const otherUser = this.querySelector(".otherUserId").getAttribute("data-otherUserId")   // 상대방 id
			const imageUrl = this.querySelector("img").src 											// 상대방 프로필 이미지URL
			const unreadBadge = this.querySelector(".message-badge")
			const userId = document.querySelector("#userId").getAttribute("data-userId") 			// 본인 ID

			hideUnreadBadge(unreadBadge, roomId, userId)
			getChatHistory(roomId, imageUrl, otherUserNickname, userId)
			// connect2(roomId, otherUserId, userId)
			connect2(serverUrl, roomId, otherUser, userId)
		})
	})
}

/* 웹소켓 종료
================================================== */
const disconnect = () => {
	if(ws && (ws.readyState == WebSocket.OPEN || ws.readyState == WebSocket.CONNECTING)) {
		let message = {
			code: '2',
			sender: window.name,
			receiver: '',
			content: '',
			regdate: displayDate(),
			regTime: new Date().toLocaleTimeString("ko-KR", {hour: "2-digit", minute: "2-digit"})
		}

		ws.send(JSON.stringify(message))
		ws.close()
		ws = ''
		log('웹소캣 연결 종료')
	}
}

/* 화면에서 읽음 표시를 제거
================================================== */
const removeUnreadIndicator = (roomId) => {
	// 특정 대화방(roomId)의 내가 보낸 메시지들 중 '1' 표시를 제거
	const myMessagesInRoom = document.querySelectorAll(`.item.me[data-room-id="${roomId}"] .unread-indicator`)
	myMessagesInRoom.forEach((indicator) => {
		indicator.remove()
	})
}

/* 서버에 읽음 상태를 업데이트
================================================== */
const updateReadStatusInDB = (roomId, userId) => {
	fetch(`${path}/chat/setIsRead`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			roomId: roomId,
			receiver: userId
		})
	})
	.then((response) => {
		if(response.ok){
			// console.log(`${userId}님이 '${roomId}' 대화방의 메시지를 읽었습니다.`)
		} else {
			console.error('failed to update')
		}
	})
	.catch(error => {
		console.error('error: ', error)
	})	
}

/* 웹소캣
================================================== */
const connect2 = (serverUrl, roomId, otherUserId, userId) => {
	const msg = document.querySelector("#msg")
	const unickName = document.querySelector("#userNickname").getAttribute("data-userNickname") // 로그인 사용자 닉네임

	window.name = userId

	// 이미 연결된 웹소켓이 있다면 먼저 끊음
	if(ws && (ws.readyState == WebSocket.CONNECTING || ws.readyState == WebSocket.OPEN)){
		disconnect()
	}

	if(!ws || ws.readyState == WebSocket.CLOSED) {
		ws = new WebSocket(serverUrl)

		// 웹소캣 연결
		ws.onopen = () => {
			log('웹소캣 연결 성공')

			let message = {
				code: '1', 
				//roomId: roomId,
				sender: window.name, 
				receiver: otherUserId, 
				content: '', 
				regdate: displayDate(),
				regTime: new Date().toLocaleTimeString("ko-KR", {hour: "2-digit", minute: "2-digit"})
			}
			ws.send(JSON.stringify(message))
			msg.focus()
		}

		// 서버로부터 수신한 메시지
		ws.onmessage = (serverMsg) => {
			let message = JSON.parse(serverMsg.data)

			// 일반 메시지 처리(code: 1~4)
			if(message.code == '1') {
				// 연결 초기화 및 과거 메시지 수신
				displayDate()
			} else if (message.code == '2') {
				// 유저 나감 알림
				//print('', `[${message.senderUnickname}]님이 나갔습니다.`, 'other', 'state', message.regTime)
			} else if (message.code == '3' || message.code == '4') {
				// 이 메시지는 서버에서 DB 저장 후 다시 모든 클라이언트(나 자신 포함)에게 보내진 것
				const isMyMessage = (message.sender == userId)
				const side = isMyMessage ? 'me' : 'other'
				
				// 상대방으로부터 받은 메시지를 읽은 경우 서버에 '읽음' 상태 업데이트 요청
				// 이 요청이 성공하면 서버가 Sender에게 code:'5'를 보내게 됨
				if(isMyMessage){
					// 내가 보낸 메시지인 경우
					if(message.messageId != -1 && message.messageId != null){
						// 1. 이전에 -1로 표시했던 임시 메시지를 찾아서 삭제
						// 현재 방(roomId)에서, 내가 보낸 (.item.me) 메시지 중 data-temp-marker="-1"을 가진 요소를 찾음
						// 가장 최근에 보낸 메시지를 업데이트할 것이므로, 마지막 요소를 찾음
						const tempMessageElement = chatList.querySelector(
							`.item.me[data-temp-marker="-1"][data-room-id="${roomId}"]:last-child`
						) // 임시 메시지를 찾음('1' 표시된 메시지)

						if(tempMessageElement){
							tempMessageElement.remove()
						}

						if(message.code == '3'){
							print(message.senderUnickname, message.content, side, 'msg', 
								message.regTime, message.isRead, message.messageId, message.roomId)	
						} else if(message.code == '4'){
							printEmoticon(message.senderUnickname, message.content, side, 'msg', 
								message.regTime, message.isRead, message.messageId, message.roomId)
						}

						/* 
						if(message.isRead == 'N'){
							// 메시지를 안 읽었으면 임시 메시지('1'표시된 메시지) 삭제하지 않음
							// 새로운 print('1' 해제된 메시지 그림) 호출하지 않음
						} else if(message.isRead == 'Y'){
							// 메시지를 읽었으면 임시 메시지('1'표시된 메시지) 삭제함
							// 새로운 print('1' 해제된 메시지 그림) 호출
							tempMessageElement.remove()
							if(message.code == '3'){
								print(message.senderUnickname, message.content, side, 'msg', 
									message.regTime, message.isRead, message.messageId, message.roomId)	
							} else if(message.code == '4'){
								printEmoticon(message.senderUnickname, message.content, side, 'msg', 
									message.regTime, message.isRead, message.messageId, message.roomId)
							}
						}
						*/
					} else {
						console.warn(`서버에서 받은 메시지의 ID가 유효하지 않습니다. messageId: ${message.messageId}`)
					}
				} else {
					// 상대방이 보낸 메시지인 경우
					if (message.code == '3') {
						print(message.senderUnickname, message.content, side, 'msg', 
							message.regTime, message.isRead, message.messageId, message.roomId)
					} else if (message.code == '4') {
						printEmoticon(message.senderUnickname, message.content, side, 'msg', 
							message.regTime, message.isRead, message.messageId, message.roomId)
					}
					updateReadStatusInDB(roomId, userId)
				}
				scrollList()
			}
			else if(message.code == '5'){
				// 보낸 사람(Sender)이 받는 '읽음' 알림
				// console.log("읽음 확인(코드 5) 수신", message) // roomId, code 수신
				if(message.roomId == roomId){ // 현재 보고 있는 방의 알림이라면
					removeUnreadIndicator(message.roomId) // 해당 방의 모든 '1' 표시 제거
					getUnreadMessageTotalCount() // 전체 안 읽은 메시지 뱃지 업데이트
				}
			}
		}

		// 웹소캣 종료
		ws.onclose = (event) => {
			log(`웹소켓 연결 종료. 코드: ${event.code}, 이유: ${event.reason}`)
			// 재연결 시도 로직 (3초 후 재연결)
			if(event.code  != 1000){
				// 정상 종료가 아닌 경우만 재연결 시도
				setTimeout(() => {
					connect2(serverUrl, roomId, otherUserId, userId)
				}, 3000)
			}
		}
		// window.addEventListener('beforeunload', function(event){
		// 	event.preventDefault()
		// })

		// 기존 웹소캣 리스너 제거
		msg.removeEventListener('keydown', window.handleKeyDown)

		// 메시지 전송 이벤트 핸들러
		window.handleKeyDown = (event) => {
			if(event.key === 'Enter') {
				event.preventDefault()

				if(msg.value.trim() != ""){
					let message = {
						code: msg.value.startsWith('/') ? '4' : '3',
						roomId: roomId,
						sender: window.name,
						receiver: otherUserId,
						content: msg.value,
						regdate: displayDate(),
						regTime: new Date().toLocaleTimeString("ko-KR", {hour: "2-digit", minute: "2-digit"})
					}

					if(ws && ws.readyState == WebSocket.OPEN){
						ws.send(JSON.stringify(message))

						// 메시지 전송 후 입력창 초기화
						msg.value = ''
						msg.style.height = 'auto'
						msg.style.height = msg.scrollHeight + 'px'
						msg.focus()
						
						// 메시지를 보낸 직후 화면에 즉시 표시하고 '1'을 표시
						// 이때 실제 messageId는 없으므로 null, 대신 tempMessageId를 전달합
						if(message.code == '3') {
							print(unickName, message.content, 'me', 'msg', 
								message.regTime, 'N', -1, roomId)	
						} else if(message.code == '4') {
							printEmoticon(unickName, '고양이', 'me', 'msg', 
								message.regTime, 'N', -1, roomId)	
						}
					} else {
						log("메시지를 보낼 수 없습니다. 웹소캣이 열려있지 않습니다.")
					}
				}
			}
		}
		// 새로운 리스너 연결
		msg.addEventListener('keydown', window.handleKeyDown)

		// "입력 중"의 입력창 높이 동적 조절
		msg.addEventListener('input', () => {
			msg.style.height = 'auto'
			msg.style.height = msg.scrollHeight + 'px'
		})
	}
}

/* 각 채팅방 안읽음 표시 없애기
================================================== */
const hideUnreadBadge = (unreadBadge, roomId, userId) => {
	if(unreadBadge) {
		fetch(`${path}/chat/setIsRead`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				roomId: roomId,
				receiver: userId
			})
		})
		.then(response => {
			if(response.ok) {
				unreadBadge.style.display = 'none'
				getUnreadMessageTotalCount()
			} else {
				console.error('failed to update')
			}
		})
		.catch(error => {
			console.error('error: ', error)
		})
	}
}

/* 채팅방 내용 불러오기
================================================== */
const getChatHistory = (roomId, imageUrl, otherUserNickname, userId) => {
	fetch(`${path}/chat/getChatHistory?roomId=${roomId}`)
		.then(response => response.json())
		.then(data => {
			chatList.innerHTML = ''
			document.querySelector('.chat-rooms').classList.add('shrink')
			document.querySelector('.chat-records').classList.add('show')

			data.forEach(messageDto => {
				if(messageDto.sender == userId) {
					print(messageDto.senderUnickname, messageDto.content, 'me', 'msg',
						messageDto.regTime, messageDto.isRead, messageDto.messageId, messageDto.roomId)	
				} else {
					print(messageDto.senderUnickname, messageDto.content, 'other', 'msg', 
						messageDto.regTime, messageDto.isRead, messageDto.messageId, messageDto.roomId)
				}
			})
			printHeader(imageUrl, otherUserNickname)
			scrollList()
			updateReadStatusInDB(roomId, userId)
		})
		.catch(error => {
			console.error('error: ', error)
		})
}

/* 메시지 전송
================================================== */
function sendMessage() {
    const input = document.querySelector('.chat-input input')
    const message = input.value.trim()
    if (message !== "") {
        const newMessage = document.createElement('div')
        newMessage.classList.add('chat-bubble', 'chat-bubble-right')
        newMessage.textContent = message
        document.querySelector('.chat-records').appendChild(newMessage)
        input.value = ""
    }
}

/* 모달 밖 클릭 시 닫기
================================================== */	
window.onclick = function(event) {
	const galleryModal = document.querySelector('.gallery-modal')
	const chatModal = document.querySelector('#chatModal')
    
	// 사진 갤러리 모달 닫기
    if (event.target == galleryModal) {
        galleryModal.style.display = 'none'
    } 
	// 채팅 모달 닫기
	else if (event.target == chatModal) {
		disconnect()
		chatModal.style.display = 'none'
    } 
	// 채팅 목록 영역 클릭 시 웹소캣 연결 해제
	else if(event.target == document.querySelector(".chat-rooms")){
		disconnect()
	} 
	
	// 공통) chat-room 스타일 복구
	if (event.target == chatModal || event.target == document.querySelector(".chat-rooms")){
		document.querySelector('.chat-rooms').classList.remove('shrink')
		document.querySelector('.chat-records').classList.remove('show')
	}
}

/* 새로운 내용이 추가되면 가장 아래로 스크롤
================================================== */
const scrollList = () => {
	if(chatList) {
		chatList.scrollTop = chatList.scrollHeight
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
}

/* 채팅방 상대정보(header) 출력
================================================== */
const printHeader = (uprofileImage, otherUserNickname) => {
	const header = document.querySelector("#header")
	header.innerHTML = 
	`
		<div class="header">
			<img src=${uprofileImage} alt="image" class="user-avatar">
			<span class="userNickname" data-userNickname="${otherUserNickname}">${otherUserNickname}</span>
		</div>
	`
}

/* 채팅방 목록 출력
================================================== */
const printRoomList = (chatRoomDto) => {
	const messageDto = chatRoomDto["messageDto"]
	const otherUserId = chatRoomDto["otherUserId"]
	const otherUserNickname = chatRoomDto["otherUserNickname"]
	const uprofileImage = chatRoomDto["uprofileImage"]
	const unreadMessageCount = chatRoomDto["unreadMessageCount"]
	const today = displayDate()
	const messageTime = (messageDto.regdate == today) ? messageDto.regTime : messageDto.regdate
	
	let temp = 
	`
		<div class="chat-room" data-room-id=${messageDto.roomId}>
			<img src="${uprofileImage}" alt="image" class="user-avatar"/>
			<div class="chat-room-info">

            	<span class="otherUserId" data-otherUserId="${otherUserId}"></span>

            	<span class="userNickname">${otherUserNickname}</span>
            	<span class="last-message">${messageDto.content}</span>
				<span class="chat-time">${messageTime}</span>	
        	</div>
	`
        	
	if(unreadMessageCount != 0) {
		temp += `<span class="message-badge">${unreadMessageCount}</span>`
	}
	
	temp += `</div>`
	
	if(roomList){
        const div = document.createElement('div')
        div.innerHTML = temp
        roomList.append(div)
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
	
	printHeader(uprofileImage, otherUserNickname)
	scrollList()
}

/* 채팅 일자 출력
================================================== */
const printDate = (regdate) => {
	let temp = 
	`
		<div class="date-wrapper">
			<div class="date">${regdate}</div>
		</div>
	`
	
	if(chatList){
        const div = document.createElement('div')
        div.innerHTML = temp
        chatList.append(div)
		scrollList()
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
}

/* 채팅창 출력
================================================== */
const print = (name, msg, side, state, time, isReadStatus, messageId, roomId) => {
	let displayOne = false
	if(side == 'me' && isReadStatus == 'N'){ // 내가 보낸 메시지가 아직 안 읽혔을 때만 '1' 표시
		displayOne = true
	}

	let dataAttrs = ''
	if(messageId != -1 && messageId != null){
		dataAttrs += `data-message-id="${messageId}"`
	} 
	if(roomId) {
		dataAttrs += ` data-room-id="${roomId}"`
	}
	if(messageId == -1){
		dataAttrs += ` data-temp-marker="${messageId}"`
	}

	let	temp =
	`
		<div class="item ${state} ${side}" ${dataAttrs}">
			<div>
				<div>${name}</div>
				<div>${msg}</div>
			</div>
			<div class="message-time-wrapper">
				${displayOne ? `<span class="unread-indicator">1</span>` : ''}
				<span>${time}</span>
			</div>
		</div>
	`
				
	if(chatList) {
        const div = document.createElement('div')
        div.innerHTML = temp
        chatList.append(div)
		scrollList()
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
}

/* 이모티콘 출력
================================================== */
const printEmoticon = (name, msg, side, state, time, isReadStatus, messageId, roomId) => {
	let displayOne = false
	if(side == 'me' && isReadStatus == 'N'){
		displayOne = true
	}

	let dataAttrs = ''
	if(messageId != -1 && messageId != null){
		dataAttrs += `data-message-id="${messageId}"`
	} 
	if(roomId) {
		dataAttrs += ` data-room-id="${roomId}"`
	}
	if(messageId == -1){
		dataAttrs += ` data-temp-marker="${messageId}"`
	}

	let temp = 
	`
		<div class="item ${state} ${side}" ${dataAttrs}">
			<div>
				<div>${name}</div>
				<div style="background-color:#fff; border:0;">
					<img src="${path}/chat/getEmoticonImage/${msg}.png">
				</div>
			</div>
			<div class="message-time-wrapper">
				${displayOne ? `<span class="unread-indicator">1</span>` : ''}
				<span>${time}</span>
			</div>
		</div>
	`

	if(chatList){
		const div = document.createElement('div')
        div.innerHTML = temp
        chatList.append(div)
		setTimeout(scrollList, 100)
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
}

/* 오늘 일자 표시
================================================== */
window.displayDate = () => {
	const dateDisplay = document.querySelector("#dateDisplay")
	if(!dateDisplay) return

	const today = new Date()
	const year = today.getFullYear()
	const month = ("0" + (today.getMonth() + 1)).slice(-2)
	const date = ("0" + today.getDate()).slice(-2)
	const dayList = ["일", "월", "화", "수", "목", "금", "토"]
	const day = dayList[today.getDay()]

	dateDisplay.innerText = `${year}.${month}.${date} (${day})`
	return `${year}년 ${month}월 ${date}일 ${day}요일`
}

/* 서버 연결 상태 출력
================================================== */
const log = (msg) => {
    console.log(`[${new Date().toLocaleTimeString()}] ${msg}`)
}

/* 페이지 로드 시 실행될 함수
================================================== */
window.onload = function() {
	getUnreadMessageTotalCount()
	handleSearchOnEnter()
	
	// 클릭 이벤트 전파 차단
	chatRecords.addEventListener('click', function(event) {
		event.stopPropagation(); 
	})
}