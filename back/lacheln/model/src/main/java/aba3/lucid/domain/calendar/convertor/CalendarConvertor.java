package aba3.lucid.domain.calendar.convertor;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Converter
public class CalendarConvertor {
    private final CalendarDetailConvertor calendarDetailConvertor;

    public CalendarConvertor(CalendarDetailConvertor calendarDetailConvertor) {
        this.calendarDetailConvertor = calendarDetailConvertor;
    }
    //먼저 entity 만들고
    //그 후에, 각 CalendarDetailEntity(자식 엔티티)를 생성할 때 부모 엔티티를 함께 전달함
    public CalendarEntity toEntity(CalendarRequest request, CompanyEntity company) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .calDate(request.getDate())
                .company(company)
                .build();
        List<CalendarDetailEntity> detailEntities = request.getDetails().stream()
                .map(detailRequest -> calendarDetailConvertor.toEntity(detailRequest, calendarEntity))
                .collect(Collectors.toCollection(ArrayList::new));
        calendarEntity.setCalendarDetailEntity(detailEntities);
        return calendarEntity;
    }




    public CalendarResponse toResponse(CalendarEntity entity) {
        List<CalendarDetailResponse> detailResponses = calendarDetailConvertor.toResponseList(entity.getCalendarDetailEntity());

        return CalendarResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
                .details(detailResponses)
                .build();
    }

}

