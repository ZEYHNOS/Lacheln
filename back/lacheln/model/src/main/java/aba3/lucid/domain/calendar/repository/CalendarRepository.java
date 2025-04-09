package aba3.lucid.domain.calendar.repository;


import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long>
{
    List<CalendarEntity> findByCalDate(LocalDate calDate);
    List<CalendarEntity> findByCompany_cpId(Long cpId);

}
