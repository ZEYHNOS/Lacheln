package aba3.lucid.domain.calendar.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.AllArgsConstructor;

import java.util.List;


@Converter
@AllArgsConstructor
public class CalendarUpdateConvertor {

    private final  CalendarDetailConvertor calendarDetailConvertor;


    public CalendarEntity toEntity(CalendarRequest request, CompanyEntity company, Long calId) {
        CalendarEntity updatedEntity = CalendarEntity.builder()
                .calDate(request.getDate())
                .company(company)
                .build();
        List<CalendarDetailEntity> detailEntities = request.getDetails().stream()
                .map(detailRequest -> calendarDetailConvertor.toEntity(detailRequest, updatedEntity))
                .toList();
        updatedEntity.setCalendarDetailEntity(detailEntities);
        return updatedEntity;
    }

    public CalendarResponse toResponse(CalendarEntity entity, CompanyEntity company, Long calId) {
        List<CalendarDetailResponse> detailResponses = calendarDetailConvertor.toResponseList(entity.getCalendarDetailEntity());

        return CalendarResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
                .details(detailResponses)
                .build();
    }

}
