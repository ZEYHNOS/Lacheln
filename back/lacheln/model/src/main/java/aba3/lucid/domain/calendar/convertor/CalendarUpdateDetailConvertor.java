package aba3.lucid.domain.calendar.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Converter
@AllArgsConstructor
public class CalendarUpdateDetailConvertor {

    public CalendarDetailEntity toEntity(CalendarDetailRequest request, CalendarDetailEntity existingDetail){
        LocalDateTime startTime =request.getStartTime() !=null
                ? request.getStartTime()
                :(existingDetail != null && existingDetail.getCalDtStart() != null
                ? existingDetail.getCalDtStart()
                :LocalDateTime.of(2025, 04, 17, 0, 0, 0));


        LocalDateTime endTime = request.getEndTime() != null
                ? request.getEndTime()
                : (existingDetail != null && existingDetail.getCalDtEnd() != null
                ? existingDetail.getCalDtEnd()
                : LocalDateTime.of(2025, 04, 17, 0, 0, 0));

        return CalendarDetailEntity.builder()
                .calDtTitle(request.getTitle())
                .calDtContent(request.getContent())
                .calDtStart(startTime)
                .calDtEnd(endTime)
                .calDtColor(request.getColor())
                .calDtMemo(request.getMemo())
                .build();
    }

    public CalendarDetailResponse toResponse(CalendarDetailEntity entity){

        LocalTime startTime = entity.getCalDtStart() != null
                ? LocalTime.from(entity.getCalDtStart())
                : null;
        LocalTime endTime = entity.getCalDtEnd() != null
                ? LocalTime.from(entity.getCalDtEnd())
                : null;
        return CalendarDetailResponse.builder()
                .calDtId(entity.getCalDtId())
                .title(entity.getCalDtTitle())
                .content(entity.getCalDtContent())
                .startTime(startTime)
                .endTime(endTime)
                .color(entity.getCalDtColor())
                .manager(entity.getCalDtManager())
                .memo(entity.getCalDtMemo())
                .build();
    }
}

