/* 전역 변수
================================================== */
const userId = document.querySelector("#userId").getAttribute("data-userId")
const searchBox = document.querySelector("#searchOption")
const searchInput = document.querySelector("#searchInput")
let sortType = ''

/* 정렬 후 게시글 목록 업데이트
================================================== */
const updatePostList = (list) => {
    let output = ''
    list.forEach((dto, index) => {
        output += `
            <tr>
                <td>${index + 1}</td>
                <td>
                    ${dto.bName}
                </td>
                <td class="post-title-cell">
                    <a href="/board/detailBoard?bId=${dto.bId}&
                        bGroup=${dto.bGroup}">
                        ${dto.bTitle}
                    </a>
                    <i class="fa-regular fa-comment-dots"></i> 
                    <span class="commentCount">${dto.commentCount}</span>
                </td>
                <td>${dto.bDate}</td>
                <td>${dto.bHit}</td>
            </tr>
        `
    })

    return output
}

/* 게시글 비동기 조회 (검색/정렬)
================================================== */
const fetchPosts = () => {
    const params = {
        searchGubun: searchBox.value,
        searchText: searchInput.value,
        sortType: sortType
    }

    axios.get('/user/getUserPosts/axios', {params})
        .then(response => {
            const tbody = document.querySelector("#postTableBody")
            const output = updatePostList(response.data)
            tbody.innerHTML = output
        })
        .catch(error => {
            console.error('error: ', error)
        })
}

/* 페이지 로드 시 실행
================================================== */
document.addEventListener('DOMContentLoaded',  () => {
    // 정렬 버튼 클릭 이벤트
    document.querySelectorAll(".sortBtn").forEach(button => {
        button.addEventListener('click', (e) => {
            sortType = e.target.id

            // 모든 버튼에서 active 스타일 제거
            document.querySelectorAll(".sortBtn").forEach(button => {
                button.classList.remove("btn-dark")
            })
            
            // 클릭한 버튼에만 active 스타일 적용
            button.classList.add("btn-dark")

            fetchPosts()
        })
    })

    // 검색 버튼 클릭 시 게시글 조회
    document.querySelector("#searchButton").addEventListener('click', () => {
        fetchPosts()
    })

    // 검색창 엔터 시 게시글 조회
    document.querySelector("#searchInput").addEventListener('keydown', (e) => {
        if(e.key == 'Enter'){
            e.preventDefault()
            fetchPosts()
        }
    })
})