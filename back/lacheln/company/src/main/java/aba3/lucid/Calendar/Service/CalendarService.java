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


    public CalendarEntity findById(Long calId) {
        return calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 캘린더입니다." + calId));
    }

    @Transactional
    public CalendarEntity updateCalendar(CalendarEntity calendar) {
        return calendarRepository.save(calendar);
    }




    }
