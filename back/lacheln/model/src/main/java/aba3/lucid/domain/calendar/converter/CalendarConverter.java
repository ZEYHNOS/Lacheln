package aba3.lucid.domain.calendar.converter;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Converter
@AllArgsConstructor
public class CalendarConverter {

    private final CalendarDetailConverter calendarDetailConverter;


    public CalendarEntity toEntity(CalendarRequest request, CompanyEntity company) {
        return CalendarEntity.builder()
                .calDate(request.getDate())
                .company(company)
                .build();
    }

    public CalendarResponse toResponse(CalendarEntity entity) {
        return CalendarResponse.builder()
                .date(entity.getCalDate())
                .calendarDetailList(calendarDetailConverter.toResponseList(entity.getCalendarDetailEntity()))
                .build()
                ;
    }

    public CalendarDetailResponse toDetailResponse(CalendarDetailEntity entity) {
        return CalendarDetailResponse.builder()
                .title(entity.getCalDtTitle())
                .content(entity.getCalDtContent())
                .startTime(entity.getCalDtStart())
                .endTime(entity.getCalDtEnd())
                .color(entity.getCalDtColor())
                .manager(entity.getCalDtManager())
                .memo(entity.getCalDtMemo())
                .build()
                ;
    }

    public List<CalendarResponse> toResponseList(List<CalendarEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }

        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

    public List<CalendarDetailResponse>[] toResponseList(List<CalendarDetailEntity>[] calendarList) {
        if (calendarList == null || calendarList.length == 0) {
            return null;
        }

        List<CalendarDetailResponse>[] result = new List[calendarList.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayList<>();
        }

        for (int i = 1; i < calendarList.length; i++) {
            if (calendarList[i].isEmpty()) {
                continue;
            }

            result[i].addAll(calendarDetailConverter.toResponseList(calendarList[i]));
        }

        return result;
    }
}

