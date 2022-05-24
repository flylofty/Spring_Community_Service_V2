package spartayn.shop.community.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b.id, b.title, b.writer, b.createdAt FROM Board b ORDER BY b.createdAt DESC")
    List<Board> getMainPageBoards();
}
