package aba3.lucid.domain.calendar.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Converter
@RequiredArgsConstructor
public class CalendarDetailConvertor {

    public List<CalendarDetailResponse> toResponseList(List<CalendarDetailEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

    public CalendarDetailEntity toEntity(CalendarDetailRequest request, CalendarEntity calendar){
        return CalendarDetailEntity.builder()
                .calDtTitle(request.getTitle())
                .calDtContent(request.getContent())
                .calDtStart(LocalDateTime.of(2025, 04, 17, 0, 0, 0))
                .calDtEnd(LocalDateTime.of(2025, 04, 17, 0, 0, 0))
                .calDtColor(request.getColor())
                .calDtMemo(request.getMemo())
                .calendar(calendar)
                .build();
    }

    public CalendarDetailResponse toResponse(CalendarDetailEntity entity) {
        return CalendarDetailResponse.builder()
                .calDtId(entity.getCalDtId())
                .title(entity.getCalDtTitle())
            .content(entity.getCalDtContent())
                .startTime(LocalDateTime.from(entity.getCalDtStart()))
                .endTime(LocalDateTime.from(entity.getCalDtEnd()))
                .color(entity.getCalDtColor())
                .manager(entity.getCalDtManager())
                .memo(entity.getCalDtMemo())
                .build();
    }


}
