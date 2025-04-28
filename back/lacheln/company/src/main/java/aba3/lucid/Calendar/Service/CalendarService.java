package aba3.lucid.Calendar.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.convertor.CalendarConvertor;
import aba3.lucid.domain.calendar.convertor.CalendarDetailConvertor;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarDetailRepository;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarDetailRepository calendarDetailRepository;
    private final CalendarConvertor calendarConvertor;
    private final CalendarDetailConvertor calendarDetailConvertor;


    public CalendarEntity createCalendar(CalendarEntity calendarEntity) {
        List<CalendarDetailEntity> calendarDetailEntities = calendarEntity.getCalendarDetailEntity();
        if (calendarDetailEntities != null && !calendarDetailEntities.isEmpty()) {
            List<CalendarDetailEntity> savedDetails = calendarDetailEntities.stream()
                    .map(calendarDetailRepository::save)
                    .toList();
            calendarEntity.setCalendarDetailEntity(savedDetails);
        }

        return calendarRepository.save(calendarEntity);
    }


    public CalendarEntity findById(Long calId) {
        return calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Calendar ID not found: " + calId));
    }

    public CalendarEntity updateCalendar(CalendarEntity calendar) {
        return calendarRepository.save(calendar);
    }




    }
