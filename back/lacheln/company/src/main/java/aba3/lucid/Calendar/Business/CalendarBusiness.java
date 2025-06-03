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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {
    private final CalendarService calendarService;
    private final CalendarConverter calendarConverter;
    private final CompanyService companyService;
    private final CalendarDetailConverter calendarDetailConverter;

    public CalendarResponse createCalendar(CalendarRequest request, Long cpId) {
        CompanyEntity company = companyService.findByIdWithThrow( cpId);


        //  CalendarEntity 생성 (Convertor 사용)
        CalendarEntity calendarEntity = calendarConverter.toEntity(request, company);

        //CalendarEntity 생성 및 연결
        List<CalendarDetailEntity> details = new ArrayList<>();
        CalendarDetailEntity detailEntity = calendarDetailConverter.toEntity(request.getDetails(),calendarEntity);
        detailEntity.setCalendar(calendarEntity);
        details.add(detailEntity);

        calendarEntity.setCalendarDetailEntity(details);  // setter로 리스트 연결

        CalendarEntity savedEntity  = calendarService.createCalendar(calendarEntity);

        return calendarConverter.toResponse(savedEntity);

    }



    public CalendarResponse updateCalendar(CalendarRequest request, Long cpId, Long calId) {
        CompanyEntity company = companyService.findByIdWithThrow( cpId);

        CalendarEntity calendar = calendarService.findById(calId);
        calendar.setCalDate(request.getDate());
        calendar.setCompany(company);

        // 기존 상세(List) 삭제
        List<CalendarDetailEntity> details = calendar.getCalendarDetailEntity();
        details.clear();

        //새 상세 엔티티로 변환 및 추가
        CalendarDetailEntity newDetail = calendarDetailConverter.toEntity(request.getDetails(),calendar);
        newDetail.setCalendar(calendar);
        details.add(newDetail);

        //저장
        CalendarEntity updated = calendarService.updateCalendar(calendar);
        return calendarConverter.toResponse(updated);
    }


}
