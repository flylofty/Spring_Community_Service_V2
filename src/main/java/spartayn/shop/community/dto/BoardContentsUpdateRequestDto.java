package spartayn.shop.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardContentsUpdateRequestDto {
    private String contents;

    public BoardContentsUpdateRequestDto(String contents) {
        this.contents = contents;
    }
}
