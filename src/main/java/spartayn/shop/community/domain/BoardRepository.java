package spartayn.shop.community.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 작성 시간 내림차순 정보 얻기
    List<Board> findAllByOrderByModifiedAtDesc();
}
