package aba3.lucid.domain.calendar.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarDto;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class CalendarDetailConverter {



    public CalendarDetailEntity toEntity(CalendarDetailRequest request, CalendarEntity calendar){

        return CalendarDetailEntity.builder()
                .calDtTitle(request.getTitle())
                .calDtContent(request.getContent())
                .calDtStart(request.getStartTime())
                .calDtEnd(request.getEndTime())
                .calDtColor(request.getColor())
                .calDtMemo(request.getMemo())
                .calendar(calendar)
                .build();
    }

    public CalendarDetailResponse toResponse(CalendarDetailEntity calendarDetail) {
        return CalendarDetailResponse.builder()
                .calDtId(calendarDetail.getCalDtId())
                .title(calendarDetail.getCalDtTitle())
                .content(calendarDetail.getCalDtContent())
                .startTime(calendarDetail.getCalDtStart())
                .endTime(calendarDetail.getCalDtEnd())
                .color(calendarDetail.getCalDtColor())
                .manager(calendarDetail.getCalDtManager())
                .memo(calendarDetail.getCalDtMemo())
                .build()
                ;
    }

    public List<CalendarDetailResponse> toResponseList(List<CalendarDetailEntity> calendarDetailEntityList) {
        return calendarDetailEntityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }


}
