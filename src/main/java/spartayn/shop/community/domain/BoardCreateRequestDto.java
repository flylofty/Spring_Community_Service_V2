package spartayn.shop.community.domain;

import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

    /**
     * 게시글 작성 시 사용하는 DTO
     */

    private String title;
    private String contents;
    private String writer;
    private String password;
}
