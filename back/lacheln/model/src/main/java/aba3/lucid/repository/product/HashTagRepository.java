package aba3.lucid.repository.product;

import aba3.lucid.domain.product.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashtagEntity, Long> {
}
