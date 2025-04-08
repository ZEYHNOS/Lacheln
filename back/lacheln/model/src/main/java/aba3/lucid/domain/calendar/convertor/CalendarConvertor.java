package aba3.lucid.domain.calendar.convertor;


import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class CalendarConvertor {

    public CalendarEntity toEntity(CalendarRequest request) {
        if(request == null) {
            return null;
        }

        List<CalendarDetailEntity>  detailEntities  = Optional.ofNullable(request.getDetails())
                .orElse(Collections.emptyList())
                .stream()
                .map(detailRequest -> {
                    CalendarDetailEntity detailEntity =  CalendarDetailEntity.builder()
                                .calDtTitle(detailRequest.getTitle())
                                .calDtContent(detailRequest.getContent())
                                .calDtStart(detailRequest.getStartTime())
                                .calDtEnd(detailRequest.getEndTime())
                                .calDtColor(detailRequest.getColor())
                                .calDtMemo(detailRequest.getMemo())
                                .calDtManager(detailRequest.getManager())
                                .build();
                return detailEntity;
        })
        .collect(Collectors.toList());

       return CalendarEntity.builder()
                .calDate(request.getDate())
                .calendarDetailEntity(detailEntities)
                .build();

    }

    public CalendarResponse toResponse(CalendarEntity entity) {
        if (entity == null) {
            return null;
        }

        List<CalendarDetailResponse> detailResponseList = Optional.ofNullable(entity.getCalendarDetailEntity())
                .orElse(Collections.emptyList())
                .stream()
                .map(detailEntity -> CalendarDetailResponse.builder()
                        .calDtId(detailEntity.getCalDtId())
                        .title(detailEntity.getCalDtTitle())
                        .content(detailEntity.getCalDtContent())
                        .starTime(detailEntity.getCalDtStart())
                        .endTime(detailEntity.getCalDtEnd())
                        .color(detailEntity.getCalDtColor())
                        .manager(detailEntity.getCalDtManager())
                        .memo(detailEntity.getCalDtManager())
                        .build())
                .collect(Collectors.toList());



        return CalendarResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
                .details(detailResponseList)
                .build();

    }





}
