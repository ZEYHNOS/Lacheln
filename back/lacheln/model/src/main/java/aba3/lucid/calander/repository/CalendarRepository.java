package aba3.lucid.calander.repository;

import aba3.lucid.calander.model.CalendarEntity;
import aba3.lucid.calander.model.CalendarId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, CalendarId> {
}
