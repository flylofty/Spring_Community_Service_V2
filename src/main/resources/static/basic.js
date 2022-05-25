$(document).ready(function () {
    $('#close').on('click', function () {
        $('#container').removeClass('active');
    })

    $('.nav div.nav-see').on('click', function () {
        $('div.nav-see').addClass('active');
        $('div.nav-search').removeClass('active');

        $('#board-area').removeClass('active').hide();
        $('#see-area').show();
        $('#write-area').hide();
    })

    $('.nav div.nav-search').on('click', function () {
        $('div.nav-see').removeClass('active');
        $('div.nav-search').addClass('active');

        $('#board-area').removeClass('active').hide();
        $('#see-area').hide();
        $('#write-area').show();
    })

    $('#see-area').show();
    $('#write-area').hide();
    $('#board-area').hide();

    showBoards();
})

function clearModalForm() {
    $('#updatePassword').empty();
    $('#updateContents').empty();
    $('#deletePassword').empty();
}

function getBoard(id) {
    $.ajax({
        type: 'GET',
        url: `/api/boards/${id}`,
        success: function (response) {

            console.log(response);

            $('div.nav-see').removeClass('active');
            $('div.nav-search').removeClass('active');
            $('#see-area').hide();
            $('#write-area').hide();

            let tempHtml = `<h1 id="title">${response.title}</h1>
                            <h5 id="writer">작성자 : ${response.writer}</h5>
                            <h5 id="createdAt">작성일 : ${response.createdAt}</h5>
                            <div id="board-btn">
                                <button onclick="openUpdateModal()" type="button" class="btn btn-outline-primary">수 정</button>
                                <button onclick="openDeleteModal()" type="button" class="btn btn-outline-danger">삭 제</button>
                            </div>
                            <hr id="board-hr">
                            <div id="board-content">${response.contents}</div>
                            
                            <div id="container" class="popup-container">
                                <div class="popup">
                                    <h1>게시글 수정</h1>
                                    <div class="mb-3" id="update-input">
                                        <input type="password" class="form-control" id="updatePassword" placeholder="비밀번호">
                                    </div>
                                    
                                    <div class="form-floating">
                                        <textarea class="form-control" placeholder="Leave a comment here" id="updateContents" style="height: 100px">${response.contents}</textarea>
                                        <label for="updateContents">수정 내용</label>
                                    </div>
                                    
                                    <div id="write-btn">
                                        <button onclick="updateBoard(${response.id})" type="button" class="btn btn-outline-primary">수 정</button>
                                        <button onclick="closeUpdateModal()" type="button" class="btn btn-outline-danger">취 소</button>
                                    </div>
                                </div>
                            </div>

                            <div id="container2" class="popup-container">
                                <div class="popup">
                                    <h1>게시글 삭제</h1>
                                    <div class="mb-3" id="update-input">
                                        <input type="password" class="form-control" id="deletePassword" placeholder="비밀번호">
                                    </div>
                                    <div id="write-btn">
                                        <button onclick="deleteBoard(${response.id})" type="button" class="btn btn-outline-primary">삭 제</button>
                                        <button onclick="closeDeleteModal()" type="button" class="btn btn-outline-danger">취 소</button>
                                    </div>
                                </div>
                            </div>`

            $('#board-area').empty().append(tempHtml).show();
        },
    })
}

function deleteBoard(id) {

    let password = $('#deletePassword').val();

    if (password === '') {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    let data = {'password':password};

    $.ajax({
        type: 'DELETE',
        url: `/api/boards/${id}`,
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            if (response === id) {
                alert("게시글을 삭제했습니다~");
                $('#deletePassword').empty();
                window.location.reload();
            } else {
                alert("비밀번호를 확인해 주세요!");
            }
        },
    })
}

function updateBoard(id) {

    let password = $('#updatePassword').val();

    if (password === '') {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    let contents = $('#updateContents').val();

    if (contents === '') {
        alert("수정 내용을 입력해주세요.");
        return;
    }

    let data = {'password':password, 'contents': contents};

    $.ajax({
        type: 'PUT',
        url: `/api/boards/${id}`,
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            if (response === id) {
                alert("내용을 수정했습니다~");
                $('#updatePassword').empty();
                $('#updateContents').empty();
                window.location.reload();
            } else {
                alert("비밀번호를 확인해 주세요!");
            }
        },
    })
}

function closeDeleteModal() {
    $('#container2').removeClass('active');
}

function openDeleteModal() {
    $('#container2').addClass('active');
}

function closeUpdateModal() {
    $('#container').removeClass('active');
}

function openUpdateModal() {
    $('#container').addClass('active');
}

function showBoards() {
    $.ajax({
        type: 'GET',
        url: '/api/boards',
        success: function (response) {
            $('#board-box').empty();
            // $('#search-result-box').empty();

            for (const board of response) {
                let tempHtml = `<tr>
                                    <th scope="row">${board.id}</th>
                                    <td onclick="getBoard(${board.id})" id="board-td">${board.title}</td>
                                    <td>${board.writer}</td>
                                    <td>${board.createdAt}</td>
                                </tr>`;
                $('#board-box').append(tempHtml);
            }
        },
    })
}

function saveBoard() {

    let title = $('#InputTitle').val();
    let writer = $('#InputWriter').val();
    let password = $('#InputPassword1').val();
    let rePassword = $('#InputPassword2').val();
    let contents = $('#contents').val();
    let result = saveValidation(title, writer, password, rePassword, contents);

    if (result !== '') {
        alert(result);
        return
    }

    let data = {'title': title, 'contents': contents, 'writer': writer, 'password':password};

    $.ajax({
        type: "POST",
        url: `/api/boards`,
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            clearForm();
            window.location.reload();
        },
    });
}

function clearForm() {
    $('#InputTitle').empty();
    $('#InputWriter').empty();
    $('#InputPassword1').empty();
    $('#InputPassword2').empty();
    $('#contents').empty();
}

function saveValidation(t, w, p, r, c) {

    if (t === '') {
        return "제목을 입력해주세요.";
    }

    if (w === '') {
        return "작성자를 입력해주세요.";
    }

    if (p === '') {
        return "비밀번호를 입력해주세요.";
    }

    if (r === '') {
        return "확인 비밀번호를 입력해주세요.";
    }

    if (p !== r) {
        return "비밀번호가 일치하지 않습니다."
    }

    if (c === '') {
        return "내용을 입력해주세요."
    }

    return '';
}
