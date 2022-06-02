package spartayn.shop.community.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    private Long id;
    private String contents;

    public CommentSaveRequestDto(Long id, String contents) {
        this.id = id;
        this.contents = contents;
    }
}
