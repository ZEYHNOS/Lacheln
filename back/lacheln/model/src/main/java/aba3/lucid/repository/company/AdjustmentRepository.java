package aba3.lucid.repository.company;


import aba3.lucid.domain.payment.AdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentRepository extends JpaRepository <AdjustmentEntity, Long> {

}
