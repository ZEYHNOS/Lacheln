package aba3.lucid.domain.schedule.repository;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeekdaysScheduleRepository extends JpaRepository<WeekdaysScheduleEntity, Long> {
    // 특정 회사의 스케줄을 전부 조회
    List<WeekdaysScheduleEntity> findAllByCompany_CpId(Long cpId);


    //특정 회사 + 특정 요일 스케줄 조회
    Optional<WeekdaysScheduleEntity> findByCompany_CpIdAndWsWeekdays(Long cpId, Weekdays weekdays );
    Optional<WeekdaysScheduleEntity> findByWsId(Long wsId);


    void deleteByWsId(Long wsId);
    List<WeekdaysScheduleEntity> findByWsWeekdays(Weekdays wsWeekdays);
}
