package spartayn.shop.community.domain;

import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class BoardResponseDto {
    private String id;
    private String title;
    private String writer;
    private String createdAt;
    private String contents;

    public BoardResponseDto(Board entity) {
        this.id = String.valueOf(entity.getId());
        this.title = entity.getTitle();
        this.writer = entity.getWriter();
        this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.contents = entity.getContents();
    }
}
