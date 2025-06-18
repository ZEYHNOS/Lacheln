package aba3.lucid.domain.calendar.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.calendar.dto.*;
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

    public CalendarDetailEntity toEntity(CalendarReservation request, CalendarEntity calendar){
        return CalendarDetailEntity.builder()
                .calDtTitle(request.getTitle())
                .calDtContent(request.getContent())
                .calDtStart(request.getStart())
                .calDtEnd(request.getEnd())
                .calDtColor(Color.BLACK)
                .calDtMemo(String.format("%s님이 %s상품, %s 옵션을 예약했습니다.\n전화번호 : %s\n특이사항 : %s",
                        request.getUserName(),
                        request.getProductName(),
                        request.getOptionDtoList() == null ? "" : request.getOptionDtoList().toString(), // TODO 리팩토링
                        request.getPhoneNum(),
                        request.getMemo()))
                .calendar(calendar)
                .calDtManager(request.getManagerName())
                .payDtId(request.getPayDtId())
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
        if (calendarDetailEntityList == null || calendarDetailEntityList.isEmpty()) {
            return null;
        }

        return calendarDetailEntityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }


}
