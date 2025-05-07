package aba3.lucid.domain.payment.repository;

import aba3.lucid.domain.payment.entity.PayManagementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayManagementRepository extends JpaRepository<PayManagementEntity, String> {

    List<PayManagementEntity> findAllByUser_userId(String userId);

}
