package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.calendar.convertor.CalendarConvertor;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {

    private final CalendarService calendarService;

    private final CalendarConvertor calendarConvertor;

    public CalendarResponse createCalendar(CalendarRequest request, Long companyId) {
        Validator.throwIfInvalidId(companyId);
        CalendarEntity calendarEntity = calendarConvertor.toEntity(request);

        CalendarEntity savedEntity = calendarService.createCalendar(calendarEntity);
        return calendarConvertor.toResponse(savedEntity);

    }
    public CalendarResponse updateCalendar(Long calId, CalendarRequest request, Long companyId) {
        CalendarEntity updatedCalendar = calendarConvertor.toEntity(request);
        CalendarEntity updatedEntity = calendarService.updateCalendar(updatedCalendar, calId);

        return calendarConvertor.toResponse(updatedEntity);

    }


}
