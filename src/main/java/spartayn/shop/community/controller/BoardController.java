package spartayn.shop.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spartayn.shop.community.dto.*;
import spartayn.shop.community.security.UserDetailsImpl;
import spartayn.shop.community.service.BoardService;
import spartayn.shop.community.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 게시글 리스트 정보 전달
    @GetMapping("/api/boards")
    public List<BoardMainPageResponseDto> getBoards() {
        return boardService.getBoards();
    }

    // 게시글 저장
    @PostMapping("/api/boards")
    public Long saveBoard(@RequestBody BoardCreateRequestDto requestDto,
                          @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        /**
         * userDetails 가 null일 경우 처리하는 로직이 있으면 좋을 것 같음
         */
        return boardService.save(requestDto, userDetails);
    }

    // 특정 게시글 조회
    @GetMapping("/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return new BoardResponseDto(
                boardService.getBoard(id),
                commentService.getComments(id, null),
                null
        );
    }

    @GetMapping("/api/boards/{id}")
    public BoardResponseDto getUserBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return new BoardResponseDto(
                boardService.getBoard(id),
                commentService.getComments(id, userDetails),
                userDetails
        );
    }

    // 특정 게시글 수정
    @PutMapping("/api/boards/{id}")
    public Long updateBoard(@PathVariable Long id,
                            @RequestBody BoardContentsUpdateRequestDto requestDto) {

        return boardService.update(id, requestDto);
    }

    // 특정 게시글 삭제
    @DeleteMapping("/api/boards/{id}")
    public Long deleteBoard(@PathVariable Long id) {
        return boardService.delete(id);
    }
}
