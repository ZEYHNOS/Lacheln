package aba3.lucid.repository.board;

import aba3.lucid.domain.board.PostViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewRepository extends JpaRepository<PostViewEntity, Long> {
}
