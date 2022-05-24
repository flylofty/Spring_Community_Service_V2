package spartayn.shop.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spartayn.shop.community.domain.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private BoardRepository boardRepository;

    // 게시글 리스트 정보 전달
    @GetMapping("/api/boards")
    public List<Board> getBoards() {
        return boardRepository.getMainPageBoards();
    }

    // 게시글 저장
    @PostMapping("/api/boards")
    public Long saveBoard(@RequestBody BoardCreateRequestDto requestDto) {
        Board board = new Board(requestDto);
        return boardRepository.save(board).getId();
    }

    // 특정 게시글 조회
    @GetMapping("/api/boards/{id}")
    public Board getBoard(@PathVariable Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );

        return board;
    }

    // 특정 게시글 수정
    @PutMapping("/api/boards/{id}")
    public Long updateBoard(@PathVariable Long id,
                            @RequestBody BoardContentsUpdateRequestDto requestDto) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );

        if (board.checkPassword(requestDto.getPassword())) {
            board.updateContents(requestDto);
            return board.getId();
        }

        return 0L;
    }

    // 특정 게시글 삭제
    @DeleteMapping("/api/board/{id}")
    public Long deleteBoard(@PathVariable Long id,
                            @RequestBody BoardDeleteRequestDto requestDto) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );

        if (board.checkPassword(requestDto.getPassword())) {
            boardRepository.deleteById(id);
            return id;
        }

        return 0L;
    }
}
