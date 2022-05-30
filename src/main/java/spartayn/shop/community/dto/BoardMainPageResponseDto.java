package spartayn.shop.community.dto;

import lombok.Getter;
import spartayn.shop.community.domain.Board;

import java.time.format.DateTimeFormatter;

@Getter
public class BoardMainPageResponseDto {

    /**
     * 메인 페이지 정보 (제목, 작성자, 날짜) 전달 DTO
     */
    private String id;
    private String title;
    private String writer;
    private String createdAt;
    public BoardMainPageResponseDto(Board entity) {
        this.id = String.valueOf(entity.getId());
        this.title = entity.getTitle();
        this.writer = entity.getWriter();
        this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
