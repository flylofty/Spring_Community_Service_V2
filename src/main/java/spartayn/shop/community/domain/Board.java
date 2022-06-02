package spartayn.shop.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import spartayn.shop.community.dto.BoardContentsUpdateRequestDto;
import spartayn.shop.community.dto.BoardCreateRequestDto;
import spartayn.shop.community.security.UserDetailsImpl;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Board extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String writer;

//    @Column(nullable = false)
//    private String password;

    // 게시글 작성
    public Board(BoardCreateRequestDto requestDto, UserDetailsImpl userDetails) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.writer = userDetails.getUsername();
    }

    // 게시글 내용 수정
    public void updateContents(BoardContentsUpdateRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    // 비밀번호 일치 확인
//    public boolean checkPassword(String inputPassword) {
//        return password.equals(inputPassword);
//    }
}
