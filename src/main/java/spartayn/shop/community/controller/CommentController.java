package spartayn.shop.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spartayn.shop.community.dto.CommentResponseDto;
import spartayn.shop.community.dto.CommentSaveRequestDto;
import spartayn.shop.community.dto.CommentUpdateRequestDto;
import spartayn.shop.community.security.UserDetailsImpl;
import spartayn.shop.community.service.CommentService;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody CommentSaveRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(OK)
                .body(new CommentResponseDto(
                        "OK",
                        "댓글을 정상적으로 등록했습니다",
                        commentService.saveComment(requestDto, userDetails))
                );
    }

    @PutMapping("/api/comments/{id}")
    public Long updateComment(@PathVariable Long id,
                              @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.updateComment(id, requestDto);
    }

    @DeleteMapping("/api/comments/{id}")
    public Long deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
