package aba3.lucid.domain.payment.repository;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailEntity, Long> {

    List<PayDetailEntity> findAllByCpId(Long cpId);

    @Query("SELECT p FROM PayDetailEntity p WHERE p.scheduleDate >= :start AND p.scheduleDate < :end")
    List<PayDetailEntity> findByPdIdAndDateTime(
            @Param("pdId") Long pdId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
