package aba3.lucid.repository.schedule;


import aba3.lucid.domain.schedule.WeekdaysScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdaysScheduleRepository extends JpaRepository<WeekdaysScheduleEntity, Long> {

}
