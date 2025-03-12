package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
}
