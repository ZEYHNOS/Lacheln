package aba3.lucid.domain.calendar.repository;

import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarDetailRepository extends JpaRepository<CalendarDetailEntity, Long> {
    List<CalendarDetailEntity> findByCalendar_calId(Long calId);


}
