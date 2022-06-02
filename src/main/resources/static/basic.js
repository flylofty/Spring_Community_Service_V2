let commentId;
$(document).ready(function () {

    if ($.cookie('token')) {
        let tempHtml = `<a id="logout-text" href="javascript:{}" onclick="userLogout()">로그아웃</a>`
        $('#header').append(tempHtml)
    } else {
        let tempHtml = `<a id="login-text" href="javascript:{}" onclick="window.location.href='/api/user/loginView'">로그인</a>`
        $('#header').append(tempHtml)
    }

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

        if ($.cookie('token')) {
            $.ajaxSetup({
                headers:{
                    'Authorization': $.cookie('token')
                }
            })

            $('div.nav-see').removeClass('active');
            $('div.nav-search').addClass('active');

            $('#board-area').removeClass('active').hide();
            $('#see-area').hide();

            $('#write-area').show();

        } else {
            alert('로그인이 필요한 서비스 입니다.')
            let isLogin = confirm('로그인 하시겠습니까?');

            if (isLogin) {
                window.location.href = '/api/user/loginView';
            }
        }
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

function userLogout() {
    let isLogout = confirm('정말 로그아웃 하시겠습니까?');

    if (isLogout === false) {
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/api/user/logout`,
        success: function (response) {
            $.removeCookie('token')
            $.removeCookie('token')
            window.location.reload();
        }, error: function () {
            console.log("실패!")
        },
    });

}

function saveComment() {

    if ($.cookie('token')) {
        let contents = $('#floatingTextarea2').val();

        if (contents === '') {
            alert("댓글 내용을 입력해주세요");
            return;
        }

        let board = document.getElementById('comment').dataset.id;

        let data = { 'id': board, 'contents': contents };

        $.ajax({
            type: 'POST',
            url: `/api/comments`,
            headers: {
                'Authorization': $.cookie('token')
            },
            data : JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                alert(response.message);
                $('#floatingTextarea2').empty();
                window.location.reload();
            },
        });


    } else {
        alert('로그인이 필요한 서비스 입니다.')
        let isLogin = confirm('로그인 하시겠습니까?');
        if (isLogin) {
            window.location.href = '/api/user/loginView';
        }
    }
}

function getBoard(id) {

    if ($.cookie('token')) {
        $.ajax({
            type: 'GET',
            url: `/api/boards/${id}`,
            headers: {
                'Authorization': $.cookie('token')
            },
            success: function (response) {

                $('div.nav-see').removeClass('active');
                $('div.nav-search').removeClass('active');
                $('#see-area').hide();
                $('#write-area').hide();

                let tempHTML = `<h1 id="title">${response.title}</h1>
                                <h5 id="writer">작성자 : ${response.writer}</h5>
                                <h5 id="write-date">작성일 : ${response.date}</h5>
                                <div id="board-btn">
                                    <button onclick="openUpdateModal()" type="button" class="btn btn-outline-primary">수 정</button>
                                    <button onclick="deleteBoard(${response.id})" type="button" class="btn btn-outline-danger">삭 제</button>
                                </div>
                                <hr id="board-hr">
                                <div id="board-content">${response.contents}</div>
                        
                                <div id="container" class="popup-container">
                                    <div class="popup">
                                        <h1>게시글 수정</h1>
                        
                                        <div class="form-floating">
                                            <textarea class="form-control" placeholder="Leave a comment here" id="updateContents" style="height: 100px"></textarea>
                                            <label for="updateContents">수정 내용</label>
                                        </div>
                        
                                        <div id="write-btn2">
                                            <button onclick="updateBoard(${response.id})" type="button" class="btn btn-outline-primary">수 정</button>
                                            <button onclick="closeUpdateModal()" type="button" class="btn btn-outline-danger">취 소</button>
                                        </div>
                                    </div>
                                </div>`

                $('#board-info').empty().append(tempHTML);

                if (`${response.mine}` === 'false') {
                    $('#board-btn').hide()
                }

                let comment = document.getElementById('comment');
                comment.dataset.id = `${response.id}`;

                $('#comments').empty();
                for (const comment of response['comments']) {
                    let tempHtml;
                    if (`${comment.mine}` === 'true') {
                        tempHtml = `<div id="comment-info">
                                        <div id="comment-writer">${comment.writer}</div>
                                        <div id="comment-date">${comment.date}</div>
                                        <div id="comment-btn">
                                            <button onclick="openCommentUpdateModal(${comment.id})" type="button" class="btn btn-outline-primary">수 정</button>
                                            <button onclick="deleteComment(${comment.id})" type="button" class="btn btn-outline-danger">삭 제</button>
                                        </div>
                                    </div>
                                    <div id="comment-contents">${comment.contents}</div>
                                    <hr/>`;
                    } else {
                        tempHtml = `<div id="comment-info">
                                        <div id="comment-writer">${comment.writer}</div>
                                        <div id="comment-date">${comment.date}</div>
                                    </div>
                                    <div id="comment-contents">${comment.contents}</div>
                                    <hr/>`;
                    }

                    $('#comments').append(tempHtml);
                }

                let modalHtml = `<div id="container2" class="popup-container">
                                    <div class="popup">
                                        <h1>댓글 수정</h1>
                        
                                        <div class="form-floating">
                                            <textarea class="form-control" placeholder="Leave a comment here" id="updateComment" style="height: 100px"></textarea>
                                            <label for="updateComment">수정 내용</label>
                                        </div>
                        
                                        <div id="write-btn5">
                                            <button onclick="updateComment()" type="button" class="btn btn-outline-primary">수 정</button>
                                            <button onclick="closeCommentUpdateModal()" type="button" class="btn btn-outline-danger">취 소</button>
                                        </div>
                                    </div>
                                </div>`

                $('#comments').append(modalHtml);
                $('#board-area').show();
            },
        });
    } else { // 토큰이 없는 경우
        $.ajax({
            type: 'GET',
            url: `/boards/${id}`,
            success: function (response) {
                $('div.nav-see').removeClass('active');
                $('div.nav-search').removeClass('active');
                $('#see-area').hide();
                $('#write-area').hide();

                let tempHTML = `<h1 id="title">${response.title}</h1>
                                <h5 id="writer">작성자 : ${response.writer}</h5>
                                <h5 id="write-date">작성일 : ${response.date}</h5>
                                <hr id="board-hr">
                                <div id="board-content">${response.contents}</div>`

                $('#board-info').empty().append(tempHTML);

                $('#comments').empty();
                for (const comment of response['comments']) {

                    let tempHtml = `<div id="comment-info">
                                        <div id="comment-writer">${comment.writer}</div>
                                        <div id="comment-date">${comment.date}</div>
                                    </div>
                                    <div id="comment-contents">${comment.contents}</div>
                                    <hr/>`;

                    $('#comments').append(tempHtml);
                }
                $('#board-area').show();
            },
        });
    }
}

function deleteComment(id) {

    let isDelete = confirm('정말 삭제하시겠습니까?');

    if (isDelete === false) {
        return;
    }

    $.ajax({
        type: 'DELETE',
        url: `/api/comments/${id}`,
        headers: {
            'Authorization': $.cookie('token')
        },
        success: function (response) {
            alert("댓글을 삭제했습니다")
            window.location.reload();
        },
    });
}

function updateComment() {
    let contents = $('#updateComment').val();

    if (contents === '') {
        alert("수정 내용을 입력해주세요.");
        return;
    }

    let data = {'contents': contents };

    $.ajax({
        type: 'PUT',
        url: `/api/comments/${commentId}`,
        data: JSON.stringify(data),
        headers: {
            'Authorization': $.cookie('token')
        },
        contentType: "application/json",
        success: function (response) {
            alert("댓글을 수정했습니다")
            window.location.href = '/';
        },
    })
}

function deleteBoard(id) {

    let isDelete = confirm('정말 삭제하시겠습니까?');

    if (isDelete === false) {
        return;
    }

    $.ajax({
        type: 'DELETE',
        url: `/api/boards/${id}`,
        headers: {
            'Authorization': $.cookie('token')
        },
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
    });
}

function updateBoard(id) {

    let contents = $('#updateContents').val();

    if (contents === '') {
        alert("수정 내용을 입력해주세요.");
        return;
    }

    let data = {'contents': contents };

    $.ajax({
        type: 'PUT',
        url: `/api/boards/${id}`,
        data: JSON.stringify(data),
        headers: {
            'Authorization': $.cookie('token')
        },
        contentType: "application/json",
        success: function (response) {
            if (response === id) {
                alert("내용을 수정했습니다~");
                // $('#updatePassword').empty();
                $('#updateContents').empty();
                window.location.reload();
            } else {
                alert("나중에 다시 시도해주세요.");
            }
        },
    })
}

function closeCommentUpdateModal() {
    $('#container2').removeClass('active');
}

function openCommentUpdateModal(id) {
    commentId = id;
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
    // let writer = $('#InputWriter').val();
    // let password = $('#InputPassword1').val();
    // let rePassword = $('#InputPassword2').val();
    let contents = $('#contents').val();
    let result = saveValidation(title, contents);

    if (result !== '') {
        alert(result);
        return
    }

    let data = { 'title': title, 'contents': contents };

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
    // $('#InputWriter').empty();
    // $('#InputPassword1').empty();
    // $('#InputPassword2').empty();
    $('#contents').empty();
}

function saveValidation(t, c) {

    if (t === '') {
        return "제목을 입력해주세요.";
    }

    // if (w === '') {
    //     return "작성자를 입력해주세요.";
    // }
    //
    // if (p === '') {
    //     return "비밀번호를 입력해주세요.";
    // }
    //
    // if (r === '') {
    //     return "확인 비밀번호를 입력해주세요.";
    // }
    //
    // if (p !== r) {
    //     return "비밀번호가 일치하지 않습니다."
    // }

    if (c === '') {
        return "내용을 입력해주세요."
    }

    return '';
}
