package aba3.lucid.domain.schedule.repository;


import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdaysScheduleRepository extends JpaRepository<WeekdaysScheduleEntity, Long> {

}
