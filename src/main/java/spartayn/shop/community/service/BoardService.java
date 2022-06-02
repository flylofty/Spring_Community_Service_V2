package spartayn.shop.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartayn.shop.community.domain.*;
import spartayn.shop.community.dto.BoardContentsUpdateRequestDto;
import spartayn.shop.community.dto.BoardCreateRequestDto;
import spartayn.shop.community.dto.BoardDeleteRequestDto;
import spartayn.shop.community.dto.BoardMainPageResponseDto;
import spartayn.shop.community.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    // 메인 페이지 (제목, 작성자, 날짜) 응답 리스트
    @Transactional(readOnly = true)
    public List<BoardMainPageResponseDto> getBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(BoardMainPageResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public Board getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        return board;
    }

    @Transactional
    public Long save(BoardCreateRequestDto requestDto,
                     UserDetailsImpl userDetails) {
        return boardRepository.save(new Board(requestDto, userDetails)).getId();
    }

    // 특정 게시글 내용 수정
    @Transactional
    public Long update(Long id, BoardContentsUpdateRequestDto requestDto) {
        Board board = getBoard(id);
        board.updateContents(requestDto);
        return id;
    }

    // 특정 게시글 삭제
    @Transactional
    public Long delete(Long id) {
        boardRepository.deleteById(id);
        return id;
    }
}
