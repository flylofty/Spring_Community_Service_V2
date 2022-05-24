package spartayn.shop.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spartayn.shop.community.domain.*;
import spartayn.shop.community.service.BoardService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    // 게시글 리스트 정보 전달
    @GetMapping("/api/boards")
    public List<BoardMainPageResponseDto> getBoards() {
        return boardService.getBoards();
    }

    // 게시글 저장
    @PostMapping("/api/boards")
    public Long saveBoard(@RequestBody BoardCreateRequestDto requestDto) {
        return boardService.save(requestDto);
    }

    // 특정 게시글 조회
    @GetMapping("/api/boards/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 특정 게시글 수정
    @PutMapping("/api/boards/{id}")
    public Long updateBoard(@PathVariable Long id,
                            @RequestBody BoardContentsUpdateRequestDto requestDto) {

        return boardService.update(id, requestDto);
    }

    // 특정 게시글 삭제
    @DeleteMapping("/api/boards/{id}")
    public Long deleteBoard(@PathVariable Long id,
                            @RequestBody BoardDeleteRequestDto requestDto) {

        return boardService.delete(id, requestDto);
    }
}
