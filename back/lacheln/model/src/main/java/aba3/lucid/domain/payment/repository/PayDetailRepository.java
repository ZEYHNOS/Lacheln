package aba3.lucid.domain.payment.repository;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailEntity, Long> {

    List<PayDetailEntity> findAllByCpId(Long cpId);

}
