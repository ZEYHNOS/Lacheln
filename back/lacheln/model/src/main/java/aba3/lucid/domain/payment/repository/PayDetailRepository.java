package aba3.lucid.domain.payment.repository;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailEntity, Long> {

    List<PayDetailEntity> findAllByCpId(Long cpId);

    @Query("SELECT d " +
           "FROM PayDetailEntity d " +
           "JOIN d.payManagement m " +
           "WHERE d.pdId = :pdId " +
           "AND d.startDatetime >= :start AND d.startDatetime < :end " +
           "AND m.payStatus = 'PAID'")
    List<PayDetailEntity> findByPdIdAndDateTime(
            @Param("pdId") Long pdId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM PayDetailEntity d " +
            "JOIN d.payManagement m " +
            "WHERE d.pdId = :pdId " +
            "AND NOT (d.endDatetime < :start OR d.startDatetime > :end) " +
            "AND m.payStatus = 'PAID'")
    boolean existsByPdIdAndDateTimes(
            @Param("padId") Long pdId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<PayDetailEntity> findAllByStartDatetimeBetweenAndPayManagement_PayStatus(LocalDateTime start, LocalDateTime end, PaymentStatus status);

    List<PayDetailEntity> findAllByPayManagement_User(UsersEntity user);


    @Query(value = """
            SELECT d.pd_id
            FROM pay_detail d
            JOIN pay_management m ON d.pay_id = m.pay_id
            WHERE d.start_datetime BETWEEN :start AND :end
            AND m.pay_status = 'PAID'
            GROUP BY pd_id
            ORDER BY COUNT(pd_id) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Long> findTop10BestSellingPdIds(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
