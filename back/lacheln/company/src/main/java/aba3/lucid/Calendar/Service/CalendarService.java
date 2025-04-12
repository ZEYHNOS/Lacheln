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

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarDetailRepository calendarDetailRepository;
    private final CalendarConvertor calendarConvertor;
    private final CalendarDetailConvertor calendarDetailConvertor;

    @Transactional
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

    public CalendarEntity updateCalendar(CalendarEntity updatedEntity, Long calId) {
        System.out.println("SerCalendarId"+ calId);
        CalendarEntity existing = calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "캘린더 ID를 찾을 수 없습니다"));
        existing.setCalDate(updatedEntity.getCalDate());
        existing.setCalendarDetailEntity(updatedEntity.getCalendarDetailEntity());
        return calendarRepository.save(existing);

    }

    public Optional<CalendarEntity> findById(Long calId) {
        return calendarRepository.findById(calId);
    }




    }
