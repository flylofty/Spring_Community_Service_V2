package spartayn.shop.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartayn.shop.community.domain.Comment;
import spartayn.shop.community.domain.CommentRepository;
import spartayn.shop.community.dto.CommentSaveRequestDto;
import spartayn.shop.community.dto.CommentUpdateRequestDto;
import spartayn.shop.community.dto.CommentsRequestDto;
import spartayn.shop.community.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentsRequestDto> getComments(Long boardId, UserDetailsImpl userDetails) {
        List<CommentsRequestDto> list = commentRepository.findAllByBoardIdOrderByCreatedAt(boardId).stream()
                .map(CommentsRequestDto::new)
                .collect(Collectors.toList());

        // 너무 못 작성한 코드;;
        for (CommentsRequestDto requestDto : list) {
            requestDto.setMyComment(userDetails);
        }

        return list;
    }

    @Transactional
    public Long saveComment(CommentSaveRequestDto requestDto, UserDetailsImpl userDetails) {
        return commentRepository.save(new Comment(requestDto, userDetails)).getBoardId();
    }

    @Transactional
    public Long updateComment(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );

        comment.updateContents(requestDto);
        return comment.getBoardId();
    }

    @Transactional
    public Long deleteComment(Long id) {
        /**
         * 참... 코드 개선해야할 점이 많다ㅠㅠ
         */
        Long boardId = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        ).getBoardId();
        commentRepository.deleteById(id);
        return boardId;
    }
}
