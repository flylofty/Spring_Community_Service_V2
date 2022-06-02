package spartayn.shop.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {
    private String contents;

    public CommentUpdateRequestDto(String contents) {
        this.contents = contents;
    }
}
