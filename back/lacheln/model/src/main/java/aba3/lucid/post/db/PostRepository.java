package aba3.lucid.post.db;

import aba3.lucid.post.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Long, PostEntity> {
}
