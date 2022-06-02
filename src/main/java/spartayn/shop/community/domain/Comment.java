package spartayn.shop.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import spartayn.shop.community.dto.CommentSaveRequestDto;
import spartayn.shop.community.dto.CommentUpdateRequestDto;
import spartayn.shop.community.security.UserDetailsImpl;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String contents;

    public Comment(CommentSaveRequestDto requestDto, UserDetailsImpl userDetails) {
        this.boardId = requestDto.getId();
        this.contents = requestDto.getContents();
        this.writer = userDetails.getUsername();
    }

    public void updateContents(CommentUpdateRequestDto requestDto) {
        contents = requestDto.getContents();
    }
}
