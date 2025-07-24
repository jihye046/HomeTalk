/* 전역 변수
================================================== */
let chatList = document.querySelector("#chatList")
const path = document.querySelector("#contextPath").getAttribute("data-context-path")
let ws = null

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

/* 채팅방 내용 불러오기
================================================== */
window.getChatHistory = (roomId, userId) => {
	fetch(`/chat/getChatHistory?roomId=${roomId}`)
        .then(response => response.json())
        .then(data => {
            chatList.innerHTML = ''

            data.forEach(messageDto => {
                if(messageDto.sender == userId) {
                    // print(messageDto.sender, messageDto.content, 'me', 'msg', messageDto.regTime)	
					print(messageDto.senderUnickname, messageDto.content, 'me', 'msg',
						messageDto.regTime, messageDto.isRead, messageDto.messageId, messageDto.roomId)	
                } else {
                    // print(messageDto.sender, messageDto.content, 'other', 'msg', messageDto.regTime)
					print(messageDto.senderUnickname, messageDto.content, 'other', 'msg', 
						messageDto.regTime, messageDto.isRead, messageDto.messageId, messageDto.roomId)
				}
            })
			scrollList()
			updateReadStatusInDB(roomId, userId)
        })
        .catch(error => {
            console.error('error: ', error)
        })
}

/* 서버 연결 상태 출력
================================================== */
const log = (msg) => {
    console.log(`[${new Date().toLocaleTimeString()}] ${msg}`)
}

/* 채팅방을 나가는 경우
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
		ws = null
		log('웹소캣 연결 종료')
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
	// if(messageId) dataAttrs += `data-message-id="${messageId}"`
	// if(roomId) dataAttrs += `data-room-id="${roomId}"`

	let temp = 
	`
		<div class="item ${state} ${side}" ${dataAttrs}">
			<div>
				<div>${name}</div>
				<div style="background-color:#fff; border:0;">
					<img src="${path}/resources/images/emoticon/${msg}.png">
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

/* 화면에서 읽음 표시를 제거
================================================== */
const removeUnreadIndicator = (roomId) => {
	// 특정 대화방(roomId)의 내가 보낸 메시지들 중 '1' 표시를 제거
	const myMessagesInRoom = document.querySelectorAll(`.item.me[data-room-id="${roomId}"] .unread-indicator`)
	myMessagesInRoom.forEach((indicator) => {
		indicator.remove()
	})
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
	if(roomId){
		dataAttrs += `data-room-id="${roomId}"`
	}
	if(messageId == -1){
		dataAttrs += `data-temp-marker="${messageId}"`
	}
	
	let temp = 
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
window.connect2 = (roomId, otherUserId, userId) => {
	const msg = document.querySelector("#msg")
	const serverUrl = document.querySelector("#serverUrl").getAttribute("data-serverUrl")
	const unickName = document.querySelector("#userNickname").getAttribute("data-userNickname") // 로그인 사용자 닉네임

	window.name = userId

	if(ws && (ws.readyState == WebSocket.CONNECTING || ws.readyState == WebSocket.OPEN)){
		disconnect()
	}
	
	if(!ws || ws.readyState == WebSocket.CLOSED) {
		ws = new WebSocket(serverUrl)

		// 웹소캣 연결
		ws.onopen = () => {
			log('서버 연결 성공')

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

		// 서버로부터 수신
		ws.onmessage = (serverMsg) => {
			let message = JSON.parse(serverMsg.data)
			
			if(message.code == '1') {
				displayDate()
			} else if (message.code == '2') {
				// print('', `[${message.senderUnickname}]님이 나갔습니다.`, 'other', 'state', message.regTime)
			} else if (message.code == '3' || message.code == '4') {  
				const isMyMessage = (message.sender == userId)
				const side = isMyMessage ? 'me' : 'other'

				if(!isMyMessage){
					// 상대방이 보낸 메시지인 경우
					if(message.code == '3'){
						print(message.senderUnickname, message.content, side, 'msg', 
							message.regTime, message.isRead, message.messageId, message.roomId)
					} else if(message.code == '4'){
						printEmoticon(message.senderUnickname, message.content, side, 'msg', 
							message.regTime, message.isRead, message.messageId, message.roomId)
					}
					updateReadStatusInDB(roomId, userId)
				} else {
					// 내가 보낸 메시지인 경우
					if(message.messageId != -1 && message.messageId != null){
						const tempMessageElement = chatList.querySelector(
							`.item.me[data-temp-marker="-1"][data-room-id="${roomId}"]:last-child`
						)

						if(message.isRead == 'Y'){
							tempMessageElement.remove()
							if(message.code == '3'){
								print(message.senderUnickname, message.content, side, 'msg', 
									message.regTime, message.isRead, message.messageId, message.roomId)	
							} else if(message.code == '4'){
								printEmoticon(message.senderUnickname, message.content, side, 'msg', 
									message.regTime, message.isRead, message.messageId, message.roomId)
							}
						}
					} else {
						console.warn(`서버에서 받은 메시지의 ID가 유효하지 않습니다. messageId: ${message.messageId}`)
					}
				}
				scrollList()
				// print(message.senderUnickname, message.content, 'other', 'msg', message.regTime)	
			} 
			// else if (message.code == '4') {
			// 	printEmoticon(message.sender, message.content, 'other', 'msg', message.regTime)
			// }

			else if(message.code == '5'){
				if(message.roomId == roomId){
					removeUnreadIndicator(message.roomId)
				}
			}
		}

		// 웹소캣 종료
		ws.onclose = (event) => {
			log(`웹소켓 연결 종료. 코드: ${event.code}, 이유: ${event.reason}`)
			// 재연결 시도 로직 (3초 후 재연결)
			if(event.code != 1000){
				// 정상 종료가 아닌 경우만 재연결 시도
				setTimeout(() => {
					window.connect2(roomId, otherUserId, userId)
				}, 3000)
			}
		}

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
						msg.style.height = 'auto' // 메시지 입력창 높이 초기화
						msg.style.height = msg.scrollHeight + 'px'
						msg.focus()

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