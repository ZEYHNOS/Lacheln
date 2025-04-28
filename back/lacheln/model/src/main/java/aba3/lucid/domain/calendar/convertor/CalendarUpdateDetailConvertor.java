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
        // 시작 시간(startTime)을 설정합니다.
        // 요청(request)에 startTime이 있으면 사용하고, 그렇지 않으면 기존 detail에서 가져오거나
        // 둘 다 없으면 기본 값(LocalDateTime.of(2025, 04, 17, 0, 0, 0))을 사용함.
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
        // 시작 시간을 LocalTime으로 변환합니다.
        LocalTime startTime = entity.getCalDtStart() != null
                ? LocalTime.from(entity.getCalDtStart())
                : null;
        // 종료 시간을 LocalTime으로 변환합니다.
        LocalTime endTime = entity.getCalDtEnd() != null
                ? LocalTime.from(entity.getCalDtEnd())
                : null;
        // 빌더 패턴을 사용하여 CalendarDetailResponse 객체를 생성합니다.
        return CalendarDetailResponse.builder()
                .calDtId(entity.getCalDtId())
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

