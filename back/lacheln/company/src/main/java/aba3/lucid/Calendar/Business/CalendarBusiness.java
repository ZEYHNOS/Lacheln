package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.calendar.converter.CalendarConverter;
import aba3.lucid.domain.calendar.converter.CalendarDetailConverter;
import aba3.lucid.domain.calendar.dto.*;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {

    private final CompanyService companyService;
    private final CalendarService calendarService;
    private final CompanyAlertService companyAlertService;

    private final CalendarConverter calendarConverter;
    private final CalendarDetailConverter calendarDetailConverter;

    @Transactional(readOnly = true)
    public List<CalendarDetailResponse>[] readAllByCpIdAndDate(Long companyId, Integer year, Integer month) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        YearMonth yearMonth = YearMonth.of(year, month);
        List<CalendarEntity> calendarList = calendarService.findCompanyCalendarByYearMonth(company, yearMonth);
        List<CalendarDetailEntity>[] detailArrayList = calendarService.sortedCalendarSchedule(calendarList, yearMonth);
        return calendarConverter.toResponseList(detailArrayList);
    }



    public CalendarResponse createCalendar(Long companyId, CalendarRequest request) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CalendarEntity calendar = calendarService.initCalendar(company, request.getDate());
        CalendarDetailEntity calendarDetail = calendarDetailConverter.toEntity(request.getDetails(), calendar);

        CalendarEntity savedCalendar = calendarService.createCalendar(calendar, calendarDetail);
        return calendarConverter.toResponse(savedCalendar);
    }

    @Transactional
    public CalendarDetailResponse updateCalendar(Long companyId, Long calDtId, CalendarDetailRequest calendarRequest) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CalendarDetailEntity calendarDetail = calendarService.findDetailIdWithThrow(calDtId);
        CalendarDetailEntity updatedCalendarDetail = calendarService.updateCalendarDetail(calendarDetail, calendarRequest, company);
        return calendarDetailConverter.toResponse(updatedCalendarDetail);
    }

    public void deleteCalendarDetail(Long calDtId, Long companyId) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CalendarDetailEntity calendarDetail = calendarService.findDetailIdWithThrow(calDtId);
        calendarService.deleteCalendarDetail(calendarDetail, company);
    }

    @Transactional
    public void userReservation(CalendarReservation dto) {
        CompanyEntity company = companyService.findByIdWithThrow(dto.getCompanyId());
        CalendarEntity calendar = calendarService.initCalendar(company, dto.getStart().toLocalDate());
        CalendarDetailEntity calendarDetail = calendarDetailConverter.toEntity(dto, calendar);
        calendarService.createCalendar(calendar, calendarDetail);
        companyAlertService.sendAlertCompany(CompanyAlertDto.builder()
                .companyId(company.getCpId())
                .sentTime(LocalDateTime.now())
                .text("예약 알림")
                .accessUrl("/company/schedule")
                .type("예약")
                .isRead(BinaryChoice.N)
                .build());
    }
}
