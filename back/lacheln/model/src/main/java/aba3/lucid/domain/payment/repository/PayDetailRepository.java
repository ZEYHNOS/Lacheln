package aba3.lucid.domain.payment.repository;

import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailOptionEntity, Long> {
}
