package aba3.lucid.Calendar.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarEntity createCalendar(CalendarEntity calendarEntity) {
        return calendarRepository.save(calendarEntity);
    }

    @Transactional
    public CalendarEntity updateCalendar(CalendarEntity updatedEntity, Long calId) {
        CalendarEntity existing = calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "캘린더 ID를 찾을 수 없습니다"));
        existing.setCalDate(updatedEntity.getCalDate());
        existing.setCalendarDetailEntity(updatedEntity.getCalendarDetailEntity());
        return calendarRepository.save(existing);

    }

    

}
