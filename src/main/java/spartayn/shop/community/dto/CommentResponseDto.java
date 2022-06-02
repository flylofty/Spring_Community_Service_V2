package spartayn.shop.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String status;
    private String message;
    private String boardId;

    public CommentResponseDto(String status, String message, Long boardId) {
        this.status = status;
        this.message = message;
        this.boardId = String.valueOf(boardId);
    }
}
