let targetId;

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

//////////////////////////////////////////////////////////////////////////////////////////
///// 여기 아래에서부터 코드를 작성합니다. ////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////// 나의 펑션 ///////////////////////////////////////

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
                                        <textarea class="form-control" placeholder="Leave a comment here"
                                                  id="updateContents" style="height: 100px">
                                        ${response.contents}
                                        </textarea>
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
            // 여기 수정해야할 수도 있음!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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


/////////////////////////////// 나의 펑션 ///////////////////////////////////////

function execSearch() {

    $('#search-result-box').empty()

    let query = $('#query').val();
    // 2. 검색창 입력값을 검사하고, 입력하지 않았을 경우 focus.

    console.log(query);

    if (query === '') {
        alert("검색어를 입력해주세요.");
        $('#query').focus();
        return;
    }
    // 3. GET /api/search?query=${query} 요청
    $.ajax({
        type: "GET",
        url: `/api/search?query=${query}`,
        success: function (response) {
            for (const res of response) {
                // 4. for 문마다 itemDto를 꺼내서 HTML 만들고 검색결과 목록에 붙이기!
                let itemDto = res;
                let tempHtml = addHTML(itemDto);
                $('#search-result-box').append(tempHtml);
            }
        },
    });
}

function addHTML(itemDto) {
    /**
     * class="search-itemDto" 인 녀석에서
     * image, title, lprice, addProduct 활용하기
     * 참고) onclick='addProduct(${JSON.stringify(itemDto)})'
     */
    return `<div class="search-itemDto">
                <div class="search-itemDto-left">
                    <img src="${itemDto.image}" alt="">
                </div>
                <div class="search-itemDto-center">
                    <div>${itemDto.title}</div>
                    <div class="price">
                        ${numberWithCommas(itemDto.lprice)}
                        <span class="unit">원</span>
                    </div>
                </div>
                <div class="search-itemDto-right">
                    <img src="images/icon-save.png" alt="" onclick='addProduct(${JSON.stringify(itemDto)})'>
                </div>
            </div>`
}

function addProduct(itemDto) {

    // 파라미터로 전달될 때 알아서 JSON으로 바꾸고 나서 넣어줌

    /**
     * modal 뜨게 하는 법: $('#container').addClass('active');
     * data를 ajax로 전달할 때는 두 가지가 매우 중요
     * 1. contentType: "application/json",
     * 2. data: JSON.stringify(itemDto),
     */
    // 1. POST /api/products 에 관심 상품 생성 요청
    $.ajax({
        type: "POST",
        url: "/api/products",
        data: JSON.stringify(itemDto),
        contentType: "application/json",
        success: function (response) {
            // 2. 응답 함수에서 modal을 뜨게 하고, targetId 를 reponse.id 로 설정 (숙제로 myprice 설정하기 위함)
            $('#container').addClass('active');
            targetId = response.id;
        },
    })
}

function showProduct() {
    /**
     * 관심상품 목록: #product-container
     * 검색결과 목록: #search-result-box
     * 관심상품 HTML 만드는 함수: addProductItem
     */
    // 1. GET /api/products 요청
    $.ajax({
        type: 'GET',
        url: '/api/products',
        success: function (response) {
            // 2. 관심상품 목록, 검색결과 목록 비우기
            $('#product-container').empty();
            $('#search-result-box').empty();
            // 3. for 문마다 관심 상품 HTML 만들어서 관심상품 목록에 붙이기!
            for (const res of response) {
                let product = res;
                let tempHtml = addProductItem(product);
                $('#product-container').append(tempHtml);
            }
        },
    })
}

function addProductItem(product) {
    // link, image, title, lprice, myprice 변수 활용하기
    return `<div class="product-card" onclick="window.location.href='${product.link}'">
                <div class="card-header">
                    <img src="${product.image}"
                         alt="">
                </div>
                <div class="card-body">
                    <div class="title">
                        ${product.title}
                    </div>
                    <div class="lprice">
                        <span>${numberWithCommas(product.lprice)}</span>원
                    </div>
                    <div class="isgood ${product.lprice <= product.myprice ? '' : 'none'}">
                        최저가
                    </div>
                </div>
            </div>`;
}

function setMyprice() {
    /**
     * 숙제! myprice 값 설정하기.
     * 1. id가 myprice 인 input 태그에서 값을 가져온다.
     * 2. 만약 값을 입력하지 않았으면 alert를 띄우고 중단한다.
     * 3. PUT /api/product/${targetId} 에 data를 전달한다.
     *    주의) contentType: "application/json",
     *         data: JSON.stringify({myprice: myprice}),
     *         빠뜨리지 말 것!
     * 4. 모달을 종료한다. $('#container').removeClass('active');
     * 5, 성공적으로 등록되었음을 알리는 alert를 띄운다.
     * 6. 창을 새로고침한다. window.location.reload();
     */
}