package aba3.lucid.domain.calendar.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Converter
@RequiredArgsConstructor
public class CalendarDetailConvertor {



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

    public CalendarDetailResponse toResponse(CalendarDetailEntity entity) {
        return CalendarDetailResponse.builder()
                .title(entity.getCalDtTitle())
                .content(entity.getCalDtContent())
                .startTime(entity.getCalDtStart())
                .endTime(entity.getCalDtEnd())
                .color(entity.getCalDtColor())
                .manager(entity.getCalDtManager())
                .memo(entity.getCalDtMemo())
                .build();
    }


}
