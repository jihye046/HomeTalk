/* 태그 입력창
================================================== */
const tagInput = document.querySelector("#tagInput") // 실제 태그 입력창
const allTagJsonStr = document.querySelector("#allTagJsonList").getAttribute("data-allTagJsonList")
const allTagList = JSON.parse(allTagJsonStr)

const tagify = new Tagify(tagInput, {
  maxTags: 10,
  // 드롭다운 자동완성 글자 수
  dropdown: {
    enabled: 1,
  },
  // ghost-text 비활성화
  autoComplete: {
    enabled: false
  },
  // 자동완성 목록
  whitelist: allTagList
})

/* 게시글 등록 시 에러 발생하는 경우 작성했던 내용들 다시 붙여주기
================================================== */
const errorMessage = document.querySelector("#errorMessage").getAttribute("data-errorMessage")
if(errorMessage){
  alert(errorMessage)

  // 게시글 제목 다시 붙여넣기
  const errorbCTitle = document.querySelector("#errorbTitle").getAttribute("data-errorbTitle")
  document.querySelector(".bTitleInput").value = errorbCTitle

  // 게시글 내용 다시 붙여넣기
  const errorbContent = document.querySelector("#errorbContent").getAttribute("data-errorbContent")
  document.querySelector("#editor").innerHTML = errorbContent

  // 지도 다시 붙여넣기
  const errorbAddress = document.querySelector("#errorbAddress").getAttribute("data-errorbAddress")
  if(errorbAddress){
    const inputAdd = document.querySelector("#inputAdd")
    inputAdd.value = errorbAddress
  }

  // 태그 다시 붙여넣기
  const errortagJsonStr = document.querySelector("#errorTags").getAttribute("data-errortagJsonList")
  if(errortagJsonStr){
    const tagList = JSON.parse(errortagJsonStr)
    tagify.addTags(tagList)
  }
}

/* ckeditor
================================================== */
let editor;
 
 // 글 작성
ClassicEditor
  .create(document.querySelector('#editor'), {
    // CKEditor configuration options
    extraPlugins: [MyCustomUploadAdapterPlugin]
  })
  .then( newEditor => {
    editor = newEditor
  })
  .catch( error => {
    console.error( error )
  })

function MyCustomUploadAdapterPlugin(editor) {
  editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
      return new UploadAdapter(loader)
  }
}

/* 주소 검색
================================================== */
// 검색한 주소를 [input]에 set
const searchedAdd = () => {
    new daum.Postcode({
        oncomplete: function(data) {
          const addr = data.address // 최종 주소 변수
          document.querySelector('#inputAdd').value = addr
        }
    }).open()
}

