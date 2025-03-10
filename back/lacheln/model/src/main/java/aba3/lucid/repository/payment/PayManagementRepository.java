package aba3.lucid.repository.payment;

import aba3.lucid.domain.payment.PayManagementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayManagementRepository extends JpaRepository<PayManagementEntity, Long> {
}
