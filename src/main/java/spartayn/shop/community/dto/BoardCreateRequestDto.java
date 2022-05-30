package spartayn.shop.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequestDto {

    /**
     * 게시글 작성 시 사용하는 DTO
     */

    private String title;
    private String contents;
    private String writer;
    private String password;

    public BoardCreateRequestDto(String title, String contents, String writer, String password) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.password = password;
    }
}
