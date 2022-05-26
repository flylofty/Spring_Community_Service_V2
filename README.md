# 개인 과제 이유, DB Table 및 API 문서

# Why?

### 1. 수정, 삭제 API의 request 방식 (param, query, body)

- @PathVariable로 삭제하려는 게시글의 id를 얻고
@RequestBody를 통해 수정하거나 삭제하려는 게시글의 수정 내용과 비밀번호를 얻음

### 2. 요청에 따른 request 사용

- 원하는 정보를 어떻게 가공할 지 구분하고 게시글 조회 시 GET, 저장 시 POST, 수정 시 PUT, 삭제 시 DELETE를 사용하였음

### 3. RESTful한 API에 관한 고민

- 구현 시작 전에 아래에 보는 것과 같이 API 설계를 먼저 시작함
- URL의 테이블에서 볼 수 있듯이 DB 테이블은 단 하나이고 그에 대한 정보를 요청 받아 전달하기 때문에
요청에 “/boards” URL로 리소스를 식별하게 하고 각각을 조회, 저장, 수정, 삭제에 걸맞게 행위에 관한 메소드를 지정하였음

---

[board_table](https://www.notion.so/30dd0b84773543de908e4cb43a4d26c4)

[API_Documentation](https://www.notion.so/497752e273484339a6fedb9756b6af20)