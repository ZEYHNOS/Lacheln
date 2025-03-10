package aba3.lucid.repository.payment;

import aba3.lucid.domain.payment.PayDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailEntity, Long> {
}
