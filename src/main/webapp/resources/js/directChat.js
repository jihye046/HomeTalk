/* 전역 변수
================================================== */
let chatList = document.querySelector("#chatList")
const path = document.querySelector("#contextPath").getAttribute("data-context-path")

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
let ws = ''
window.connect2 = (roomId, otherUserId, userId) => {
	const msg = document.querySelector("#msg")
	const serverUrl = document.querySelector("#serverUrl").getAttribute("data-serverUrl")
	const unickName = document.querySelector("#userNickname").getAttribute("data-userNickname") // 로그인 사용자 닉네임

	window.name = userId
	
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
				print('', `[${message.sender}]님이 나갔습니다.`, 'other', 'state', message.regTime)

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
		window.addEventListener('beforeunload', function(event){
			event.preventDefault()
			disconnect()
		})

		// 메시지 전송
		window.handleKeyDown = (event) => {
			if(event.key === 'Enter') {
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

				ws.send(JSON.stringify(message))
				msg.value = ''
				msg.focus()

				if(message.code == '3') {
					print(unickName, message.content, 'me', 'msg', message.regTime)	
				} else if(message.code == '4') {
					printEmotion(unickName, '고양이', 'me', 'msg', message.regTime)	
				}
			}
		}
		msg.addEventListener('keydown', window.handleKeyDown)
	}
}