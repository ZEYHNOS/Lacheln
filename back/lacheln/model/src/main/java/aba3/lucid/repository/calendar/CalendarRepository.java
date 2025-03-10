package aba3.lucid.repository.calendar;


import aba3.lucid.domain.calendar.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long>
{

}
