package aba3.lucid.calendarDetail.repository;

import aba3.lucid.calendarDetail.model.CalendarDetailEntity;
import aba3.lucid.calendarDetail.model.CalendarDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarDetailRepository extends JpaRepository<CalendarDetailEntity, CalendarDetailId> {
}
