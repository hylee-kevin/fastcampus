

const noteListDiv = document.querySelector(".note-list");
const dest = 'http://192.168.56.103:3000';

// let noteID = 1;

function Note(title, date, nickname, password, content, id) {
  this.title = title;
  this.date = date;
  this.id = id;
  this.nickname = nickname;
  this.password = password;
  this.content = content;
}

// Add eventListeners
// 저장된 페이퍼 로딩, 버튼 클릭에 대한 이벤트리스너 추가
function eventListeners() {
  document.getElementById("add-note-btn").addEventListener("click", addNewNote);

  noteListDiv.addEventListener("click", clickBtn);
}

eventListeners();


// get item from storage
// 저장된 페이퍼들을 불러오는 함수
function getDataFromStorage() {
  return localStorage.getItem("notes") ? JSON.parse(localStorage.getItem("notes")) : [];
}

// add a new note in the list
function addNewNote() {
  const title = document.getElementById("title");
  const date = document.getElementById("date");
  const nickname = document.getElementById("nickname");
  const password = document.getElementById("password");
  const content = document.getElementById("content");
  if (validateInput(title, date, nickname, password, content)) {
    axios.post(dest, {
      title: title.value,
      date: date.value,
      nickname: nickname.value,
      password: password.value,
      content: content.value
    }).then((res) => {
      window.location.reload();
    });
  }
}


//  input validation
function validateInput(title, date, nickname, password, content) {
  if (title.value !== "" && date.value !== "" && nickname.value !== "" && password.value !== "" && content.value !== "") {
    return true;
  } else {
    if (title.value === "") {
      title.classList.add("warning");
      title.placeholder = "Please input a text.";
    }
    if (date.value === "") {
      date.classList.add("warning");
      date.placeholder = "Please input a text.";
    }
    if (nickname.value === "") {
      nickname.classList.add("warning");
      nickname.placeholder = "Please input a text.";
    }
    if (password.value === "") {
      password.classList.add("warning");
      password.placeholder = "Please input a text.";
    }
    if (content.value === "") {
      content.classList.add("warning");
      content.placeholder = "Please input a text.";
    }
  }
  setTimeout(() => {
    title.classList.remove("warning");
    date.classList.remove("warning");
    nickname.classList.remove("warning");
    password.classList.remove("warning");
    content.classList.remove("warning");
    title.placeholder = "";
    date.placeholder = "";
    nickname.placeholder = "";
    password.placeholder = "";
    content.placeholder = "";

  }, 1600);
}


// create a new note div
function createNote(noteItem) {
  const div = document.createElement("div");
  div.classList.add("note-item");
  div.id = noteItem.id;
  div.innerHTML = `
    <h2>made by createNote of index.js</h2>
    <h3>${noteItem.title}</h3>
    <p>${noteItem.date}<p>
    <h3>${noteItem.nickname}</h3>
    <p>${noteItem.content}</p>
    <button type = "button" class = "btn delete-note-btn">
    <span><i class = "fas fa-trash"></i></span>
    Delete
    </buttton>
  `;
  noteListDiv.appendChild(div);
}

// btn click functions
function clickBtn(e) {

  // 삭제 버튼 클릭
  if (e.target.classList.contains("delete-note-btn")) {
    const delete_btn = e.target;
    const note_item = delete_btn.parentElement;
    
    // 이미 버튼을 누른 상태라면 모달창 닫기
    if (checkClicked(delete_btn)) {
      deleteModal(delete_btn);
    }

    // 버튼을 누르면 비밀번호 입력 모달창 생성
    else {
      const div = document.createElement("div");
      div.classList.add("modal");
      div.innerHTML = `
        <div>비밀번호를 입력하세요.</div>
        <div class="err-msg">　</div>
        <form method="post" action="/${note_item.id}">
        <input type="text" class="corrpw" name="corrpw" />
        <button type="submit" class="input-pw-btn">확인</button>
        <button type="button" class="delete-note-btn clicked">취소</button>
        </form>
      `;
      note_item.appendChild(div);
      delete_btn.classList.add("clicked");
    }
  }

  else if (e.target.classList.contains("input-pw-btn")) {
    const pw_input = e.target.previousElementSibling.value;
    const note_id = e.target.closest(".note-item").id;
    axios.delete(dest, {
      data: {
        id: note_id,
        password: pw_input
      }
    }).then((res) => {
      // 200
      // console.log(res);
      // window.location.reload();
    }).catch((err) => {
      // 401
      if (err.response.status == 401) {
        e.target.previousElementSibling.previousElementSibling.innerText = "비밀번호가 틀렸습니다.";
      } else if (err.response.status == 406) {
        // 406
        window.location.reload();
      }
    })
  }
}

function checkClicked(target) {
  if (target.classList.contains("clicked")) return true;
  else return false;
}

// delete를 두 번 누르거나 modal 창의 취소를 누르면 모달창 제거
function deleteModal(delete_btn) {
  if (delete_btn.closest(".modal") != null) {
    delete_btn.parentElement.previousSibling.classList.remove("clicked");
    delete_btn.closest(".modal").remove();
  }
  else {
    delete_btn.nextSibling.remove();
    delete_btn.classList.remove("clicked");
  }
}

