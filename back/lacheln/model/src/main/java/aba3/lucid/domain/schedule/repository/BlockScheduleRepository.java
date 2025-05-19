package aba3.lucid.domain.schedule.repository;

import aba3.lucid.domain.schedule.entity.BlockScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlockScheduleRepository extends JpaRepository<BlockScheduleEntity, Long> {
    // 년, 월, 일로 Schedule 하나 찾기
    @Query("SELECT s FROM BlockScheduleEntity s " +
            "WHERE s.pdId = :pdId " +
            "AND s.startTime BETWEEN :startTime AND :endTime")
    List<BlockScheduleEntity> findByPdIdAndDate(
            @Param("pdId") Long pdId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // 상품ID와 시작시간, 종료시간으로 필터링하여 삭제
    @Modifying
    @Query("DELETE FROM BlockScheduleEntity s " +
            "WHERE s.pdId = :pdId " +
            "AND s.startTime = :startTime " +
            "AND s.endTime = :endTime")
    void deleteByPdIdAndDateTime(
            @Param("pdId") Long pdId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT s FROM BlockScheduleEntity s " +
           "WHERE s.pdId = :pdId " +
           "AND :startTime BETWEEN s.startTime AND s.endTime")
    List<BlockScheduleEntity> findByPdIdAndStartTime(
            @Param("pdId") Long pdId,
            @Param("startTime") LocalDateTime startTime);
}
