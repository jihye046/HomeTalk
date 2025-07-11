/* 전역 변수
================================================== */
    // 닉네임 상태 체크
let isNicknameChecked = false

    // 닉네임 상태 출력 메시지 태그
const nicknameCheckMessage = document.querySelector("#nicknameRequirement")

    // 회원가입 버튼
const joinBtn = document.querySelector("#joinBtn")                                 

/* 휴대폰 번호 입력 시 하이픈 자동 생성
================================================== */
const mobileInput = document.querySelector("#umobile")

mobileInput.addEventListener('input', (e) => {
    const oldValue = mobileInput.value
    const oldCursor = mobileInput.selectionStart

    // 숫자 이외 문자는 제거
    let numbers = oldValue.replace(/\D/g, '') 
	
    // 하이픈 포함한 새 값 생성
    let formatted = ''
    if(numbers.length <= 3){
        formatted = numbers
    } else if(numbers.length <= 7){
        formatted = numbers.slice(0, 3) + '-' + numbers.slice(3)
    } else {
        formatted = numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7, 11)
    }

    if(formatted != oldValue){
  	    // 새로운 input창 값  
        mobileInput.value = formatted

	    // 새로운 커서 위치
        let newCursor = oldCursor
        if(oldCursor == 4 || oldCursor == 9){
            newCursor++
        }

        mobileInput.setSelectionRange(newCursor, newCursor)
    }
})

/* 메시지 스타일
================================================== */
    // 성공
const setValidStyle = (el, message) => {
    el.textContent = message
    el.style.color = '#28a745'
}

    // 실패
const setInvalidStyle = (el, message) => {
    el.textContent = message
    el.style.color = '#ff6347'
}

/* 조건에 따라 회원가입 버튼 활성화/비활성화 처리
================================================== */
const updateJoinBtnState = () => {
    joinBtn.disabled = !(
        isNicknameChecked
    ) 
}

/* 닉네임 입력 시 리스너
================================================== */
const nicknameInput = document.querySelector("#unickName")

nicknameInput.addEventListener('input', _.debounce(() => {
    const nickname = nicknameInput.value.trim()

    if(nickname == ''){
        setInvalidStyle(nicknameCheckMessage, '닉네임을 입력해주세요.')
        isNicknameChecked = false
        updateJoinBtnState()
        return
    }

    // debounce: 입력이 멈춘 뒤 0.3초 후에 중복 검사 요청을 보내 서버 과부하 방지
    axios.get('/user/check-nickname-duplicate', {
    params: {checkNickanme: nickname}
    })
        .then(response => {
            if(response.data){
                setInvalidStyle(nicknameCheckMessage, '사용중인 닉네임입니다.')
                isNicknameChecked = false
            } else if(!(response.data)) {
                setValidStyle(nicknameCheckMessage, '사용 가능한 닉네임입니다.')
                isNicknameChecked = true
            }
            updateJoinBtnState()
        })
        .catch(error => {
            console.error('error: ', error)
        })
}, 300))
