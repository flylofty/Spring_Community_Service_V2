package spartayn.shop.community.dto;

import lombok.Getter;
import spartayn.shop.community.domain.Comment;
import spartayn.shop.community.security.UserDetailsImpl;

import java.time.format.DateTimeFormatter;

@Getter
public class CommentsRequestDto {

    private String id;
    private String writer;
    private String contents;
    private String date;

    private boolean mine;

    public CommentsRequestDto(Comment entity) {
        this.id = String.valueOf(entity.getId());
        this.writer = entity.getWriter();
        this.contents = entity.getContents();
        this.date = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    public void setMyComment(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            mine = false;
        } else {
            mine = userDetails.getUser() != null && writer.equals(userDetails.getUsername());
        }
    }
}
