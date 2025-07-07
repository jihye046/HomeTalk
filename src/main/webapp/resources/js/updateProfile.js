/* 전역 변수
================================================== */
const path = document.querySelector("#contextPath").getAttribute("data-context-path")

/* 수정 완료 메시지
================================================== */
const showProfileMessage = (msg) => {
    const profileMessage = document.querySelector("#profileMessage")
    profileMessage.textContent = msg
    profileMessage.classList.add('show')
    
    setTimeout(() => {
        profileMessage.classList.remove('show')
    }, 3000)
}

/* 드롭다운 메뉴 닫기
================================================== */
const closeDropdownMenu = () => {
    const dropDownMenu = document.querySelector("#dropdownMenu")
    dropDownMenu.style.display = 'none'
}

/* 드롭다운 메뉴 토글
================================================== */
const profileImage = document.querySelector("#profileImage")
const toggleDropdwon = () => {
    profileImage.addEventListener('click', function(){
        const dropDownMenu = document.querySelector("#dropdownMenu")
        if(dropDownMenu.style.display == 'none' || dropDownMenu.style.display == '') {
            dropDownMenu.style.display = 'block'
        } else {
            dropDownMenu.style.display = 'none'
        }
    })
}

/* 프로필 이미지 변경
================================================== */
    // 기본 이미지 선택 여부 플래그
let isCustomImageSelected = false
	// 아무 동작하지 않은 경우 감지 플래그
let event = false

    // 기본 이미지로 변경
const setDefaultImage = () => {
    const defaultImage = document.querySelector("#defaultImage")
    defaultImage.addEventListener('click', function() {
        profileImage.src = `${path}/resources/images/profile_default.png`
        event = true
        closeDropdownMenu()
        showProfileMessage('기본 프로필 이미지가 선택되었습니다.')
    })
}

    // 파일에서 사진 선택, 미리보기
const setProfileImageFromFile = () => {
    const fileImage = document.querySelector("#fileImage")
    const maxSize = 5 * 1024 * 1024 // 5MB
    const profileImageInput = document.querySelector("#profileImageInput")

    let selectedFile = null // 선택된 파일 저장

    fileImage.addEventListener('click', function() {
        if(profileImageInput) {
            // 파일 업로드
            const pond = FilePond.create(profileImageInput, {
                allowDrop: false, // 드래그 앤 드롭 사용 안함
                allowMultiple: false, // 여러 파일 선택 방지
                onaddfile: (error, file) => {
                    if(!error) {
                        if(file.file.size > maxSize) {
                            // 파일 크기 체크
                            alert('파일 크기는 5MB를 초과할 수 없습니다.')
                            pond.removeFile(file.id) // 업로드한 파일 제거
                        } else if (!file.file.type.startsWith('image/')) {
                            // 이미지 여부 체크
                            alert('이미지 파일만 업로드할 수 있습니다.')
                            pond.removeFile(file.id)
                        } else {
                            const imageUrl = URL.createObjectURL(file.file)
                            profileImage.src = imageUrl
                            selectedFile = file.file
                            event = true
                        }
                    } else {
                        alert('알 수 없는 오류가 발생했습니다.')
                    }
                }
            })
        } else {
            console.log('input 업로드 안됨')
        }
    })
    
    return () => selectedFile
}

/* 변경 버튼 클릭
================================================== */
const updateButton = document.querySelector("#updateButton")
const getSelectedFile = setProfileImageFromFile() // 클로저 함수 자체
const defaultImage = 'profile_default.png'
const currentProfileImage = document.querySelector("#profileImage").getAttribute("data-currentImage")
const isUploadedImage = currentProfileImage != defaultImage

updateButton.addEventListener('click', function(){
    const currentNickname = document.querySelector("#currentNickname").getAttribute("data-currentNickname")
    const newNickname = document.querySelector("#nickname").value.trim()
    const selectedFile = getSelectedFile() // 클로저 함수에 담긴 값

    // 닉네임 검사
    if(!newNickname) {
        alert('닉네임을 입력해주세요.')
        return
    }
    
    if(currentNickname != newNickname) {
    	event = true
    }
    
    // 아무 동작하지 않은 경우
    if(!event) {
    	alert('변경사항이 없습니다.')
    	return
    }

    // 파일 검사
    const formData = new FormData()
    
    if(currentNickname != newNickname && !selectedFile && isUploadedImage) {
        // 닉네임만 변경하는 경우
        formData.append('nickname', newNickname)
        
    } else if((currentNickname == newNickname) && !selectedFile) {
        // 프로필 이미지만 기본 이미지로 변경하는 경우
        isCustomImageSelected = false
        formData.append('isCustomImageSelected', isCustomImageSelected)
        formData.append('defaultImage', defaultImage)
        
    } else if((currentNickname == newNickname) && selectedFile) {
    	// 프로필 이미지만 업로드한 이미지로 변경하는 경우
        isCustomImageSelected = true
        formData.append('isCustomImageSelected', isCustomImageSelected)
        formData.append('profileImage', selectedFile)
        
    } else if((currentNickname != newNickname) && !selectedFile && !isUploadedImage) {
        // 닉네임 변경 + 기본 이미지로 변경하는 경우
		isCustomImageSelected = false
        formData.append('isCustomImageSelected', isCustomImageSelected)
		formData.append('defaultImage', defaultImage)        
		formData.append('nickname', newNickname)
		
    } else if((currentNickname != newNickname) && selectedFile) {
    	// 닉네임 변경 + 업로드한 이미지로 변경하는 경우
    	isCustomImageSelected = true
        formData.append('isCustomImageSelected', isCustomImageSelected)
        formData.append('profileImage', selectedFile)
        formData.append('nickname', newNickname)
        
    }
    
    return updateProfile(formData)
})

/* 프로필 이미지와 닉네임을 서버로 전송
================================================== */
const updateProfile = (formData) => {

	// formData 내용 확인
	/*
    for (let [key, value] of formData.entries()) {
        console.log(key, value);
    }
    */

    fetch('/user/updateProfile', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
       	if(data.filename) {
       		fileServing(data.filename)
       	}
        
        showProfileMessage(data.msg)
    })
    .catch(error => {
    	console.log('Error', error)
    })
}

/* 서버 서빙 설정
================================================== */
const fileServing = (filename) => {
    fetch(`/user/getProfileImage/${filename}`, {
        method: 'GET'
    })
    .then(response => {
        if(!response.ok) {
            throw new Error('File not found')
        }
        return response.blob() // 파일을 blob 형태로 받음
    })
    .then(blob => {
        const imageUrl = URL.createObjectURL(blob) // blob을 url로 변환(외부에서 로컬 파일에 접근이 불가능하기때문에 url로 변환)
        document.querySelector("#profileImage").src = imageUrl
    })
    .catch(error => {
        console.log('Error', error)
        showProfileMessage('오류 발생')
    })
}

/* 페이지 로드 시 실행될 함수
================================================== */
toggleDropdwon()
setProfileImageFromFile()
setDefaultImage()