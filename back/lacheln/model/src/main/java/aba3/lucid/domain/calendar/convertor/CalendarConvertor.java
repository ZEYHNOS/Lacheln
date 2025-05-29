package aba3.lucid.domain.calendar.convertor;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Converter
@AllArgsConstructor
public class CalendarConvertor {

    private final CalendarDetailConvertor calendarDetailConvertor;


    public CalendarEntity toEntity(CalendarRequest request, CompanyEntity company) {
        return CalendarEntity.builder()
                .calDate(request.getDate())
                .company(company)
                .build();
    }



    // 캘린더 + 상세(일정) 응답
    public CalendarResponse toResponse(CalendarEntity entity) {
        // 상세 리스트 → response용 list로 변환
        List<CalendarDetailResponse> detailResponses = entity.getCalendarDetailEntity() == null ?
                new ArrayList<>():
                entity.getCalendarDetailEntity().stream()
                        .map(calendarDetailConvertor::toResponse)
                        .collect(Collectors.toList());

        return CalendarResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
                .details(detailResponses)
                .build();
    }

}

