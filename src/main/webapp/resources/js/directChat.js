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
                    print(messageDto.senderUnickname, messageDto.content, 'me', 'msg', messageDto.regTime)	
                } else {
                    // print(messageDto.sender, messageDto.content, 'other', 'msg', messageDto.regTime)
                    print(messageDto.senderUnickname, messageDto.content, 'other', 'msg', messageDto.regTime)
                }
            })

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
	let message = {
		code: '2',
		sender: window.name,
		receiver: '',
		content: '',
		regdate: displayDate(),
		regTime: new Date().toLocaleTimeString("ko-KR", {hour: "2-digit", minute: "2-digit"})
	}

	if(ws && ws.readyState == WebSocket.OPEN) {
		ws.send(JSON.stringify(message))
	}
}

/* 이모티콘 출력
================================================== */
const printEmotion = (name, msg, side, state, time) => {
	let temp = 
	`
		<div class="item ${state} ${side}">
			<div>
				<div>${name}</div>
				<div style="background-color:#fff; border:0;">
					<img src="${path}/resources/images/emoticon/${msg}.png">
				</div>
				<div>${time}</div>
			</div>
		</div>
	`

	if(chatList){
		const div = document.createElement('div')
        div.innerHTML = temp
        chatList.append(div)
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
	setTimeout(scrollList, 100)
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

/* 채팅창 출력
================================================== */
const print = (name, msg, side, state, time) => {
	
	let temp = 
	`
		<div class="item ${state} ${side}">
			<div>
				<div>${name}</div>
				<div>${msg}</div>
			</div>
			<div>${time}</div>
		</div>
	`

	if(chatList) {
        const div = document.createElement('div')
        div.innerHTML = temp
        chatList.append(div)
	} else {
		alert('chatList 찾을 수 없음')
		return
	}
	
	scrollList()
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

		// 서버에서 클라이언트에게 전달한 메시지
		ws.onmessage = (serverMsg) => {
			let message = JSON.parse(serverMsg.data)
			
			if(message.code == '1') {
				/*
				if(message.sender == window.name) {
					print(message.sender, message.content, 'me', 'msg', message.regTime)
				} else {
					print(message.sender, message.content, 'other', 'msg', message.regTime)	
				}
				*/

				displayDate()
			} else if (message.code == '2') {
				// print('', `[${message.sender}]님이 나갔습니다.`, 'other', 'state', message.regTime)

				/* 입력창 비활성화 */
				// msg.disabled = true
				// msg.placeholder = '대화상대가 없습니다.'
				// msg.style.backgroundColor = '#f0f0f0'
			} else if (message.code == '3') {
				if(message.sender == window.name) {
					print(message.senderUnickname, message.content, 'me', 'msg', message.regTime)
				} else {
					print(message.senderUnickname, message.content, 'other', 'msg', message.regTime)	
				}
			} else if (message.code == '4') {
				printEmotion(message.sender, message.content, 'other', 'msg', message.regTime)
			}
		}

		// 웹소캣 종료
		ws.onclose = (event) => {
			log(`웹소켓 연결 종료. 코드: ${event.code}, 이유: ${event.reason}`)
			// 재연결 시도 로직 (3초 후 재연결)
			setTimeout(() => {
				window.connect2(roomId, otherUserId, userId)
			}, 3000)
		}

		// 기존 웹소캣에 연결된 리스너를 지움
		msg.removeEventListener('keydown', window.handleKeyDown)

		// 메시지 전송
		window.handleKeyDown = (event) => {
			if(event.key === 'Enter') {
				event.preventDefault()

				if(msg.value != null || msg.value != ""){
					let message = {
						code: '3',
						roomId: roomId,
						sender: window.name,
						receiver: otherUserId,
						content: msg.value,
						regdate: displayDate(),
						regTime: new Date().toLocaleTimeString("ko-KR", {hour: "2-digit", minute: "2-digit"})
					}

					if(msg.value.startsWith('/')) {
						message.code = '4'
					}

					if(ws && ws.readyState == WebSocket.OPEN){
						ws.send(JSON.stringify(message))

						// 메시지 전송 후 입력창 초기화
						msg.value = ''
						msg.style.height = 'auto' // 메시지 입력창 높이 초기화
						msg.style.height = msg.scrollHeight + 'px'
						msg.focus()

						if(message.code == '3') {
							print(unickName, message.content, 'me', 'msg', message.regTime)	
						} else if(message.code == '4') {
							printEmotion(unickName, '고양이', 'me', 'msg', message.regTime)	
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