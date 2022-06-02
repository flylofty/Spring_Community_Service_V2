package spartayn.shop.community.dto;

import lombok.Getter;
import spartayn.shop.community.domain.Board;
import spartayn.shop.community.domain.Comment;
import spartayn.shop.community.security.UserDetailsImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class BoardResponseDto {
    private String id;
    private String title;
    private String writer;
    private String date;
    private String contents;
    private Boolean mine;
    private List<CommentsRequestDto> comments;

    public BoardResponseDto(Board entity, List<CommentsRequestDto> comments, UserDetailsImpl userDetails) {
        this.id = String.valueOf(entity.getId());
        this.title = entity.getTitle();
        this.writer = entity.getWriter();
        this.date = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.contents = entity.getContents();
        this.comments = comments;

        if (userDetails == null) {
            this.mine = false;
        } else {
            this.mine = userDetails.getUser() != null && writer.equals(userDetails.getUsername());
        }
    }
}
