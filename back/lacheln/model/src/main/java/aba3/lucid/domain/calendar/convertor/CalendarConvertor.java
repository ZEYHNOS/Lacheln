package aba3.lucid.domain.calendar.convertor;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Converter
public class CalendarConvertor {
    private final CalendarDetailConvertor calendarDetailConvertor;

    public CalendarConvertor(CalendarDetailConvertor calendarDetailConvertor) {
        this.calendarDetailConvertor = calendarDetailConvertor;
    }

    public CalendarEntity toEntity(CalendarRequest request, CompanyEntity companyEntity) {
        List<CalendarDetailEntity> detailEntities = request.getDetails().stream()
                .map(detailRequest -> calendarDetailConvertor.toEntity(detailRequest, null))
                .toList();

        return CalendarEntity.builder()
                .calDate(request.getDate())
                .company(companyEntity)
                .calendarDetailEntity(detailEntities)
                .build();
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

