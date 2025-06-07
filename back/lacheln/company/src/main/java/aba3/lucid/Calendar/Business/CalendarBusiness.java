package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.calendar.converter.CalendarConverter;
import aba3.lucid.domain.calendar.converter.CalendarDetailConverter;
import aba3.lucid.domain.calendar.dto.*;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {

    private final CompanyService companyService;
    private final CalendarService calendarService;

    private final CalendarConverter calendarConverter;
    private final CalendarDetailConverter calendarDetailConverter;

    public List<CalendarResponse> readAllByCpId(Long companyId) {
        List<CalendarEntity> calendarEntityList = calendarService.findAllByCpId(companyId);
        return calendarConverter.toResponseList(calendarEntityList);
    }

    public CalendarResponse readByCpIdAndDate(Long companyId, LocalDate date) {
        CalendarEntity calendar = calendarService.findByCpIdAndDateWithThrow(companyId, date);
        return calendarConverter.toResponse(calendar);
    }


    public CalendarResponse createCalendar(Long companyId, CalendarRequest request) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CalendarEntity calendar = calendarService.initCalendar(company, request);
        CalendarDetailEntity calendarDetail = calendarDetailConverter.toEntity(request.getDetails(), calendar);

        CalendarEntity savedCalendar = calendarService.createCalendar(calendar, calendarDetail);
        return calendarConverter.toResponse(savedCalendar);
    }
}
