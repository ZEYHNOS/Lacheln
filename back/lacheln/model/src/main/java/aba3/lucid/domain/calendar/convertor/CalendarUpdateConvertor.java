package aba3.lucid.domain.calendar.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.calendar.dto.*;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Converter
@AllArgsConstructor
public class CalendarUpdateConvertor {

    private final  CalendarUpdateDetailConvertor calendarUpdateDetailConvertor;


    public CalendarEntity toEntity(CalendarUpdateRequest request, CompanyEntity company, Long calId, CalendarEntity existingEntity) {
        // 요청에서 받은 날짜로 기존 엔티티의 날짜(calDate)를 업데이트함.
        existingEntity.setCalDate(request.getDate());
        // 요청에 해당하는 회사 정보를 엔티티에 설정함.
        existingEntity.setCompany(company);

        // 기존 엔티티의 CalendarDetailEntity 컬렉션을 가져옴.
        List<CalendarDetailEntity> currentDetails = existingEntity.getCalendarDetailEntity();
        //기존 요소들을 제거합니다.
        currentDetails.clear();

        // 엔티티의 모든 CalendarDetailEntity들을 CalendarDetailResponse로 변환함
        List<CalendarDetailEntity> newDetailEntities = new ArrayList<>();
        for (CalendarDetailRequest detailRequest : request.getDetails()) {

            CalendarDetailEntity newDetail = calendarUpdateDetailConvertor.toEntity(detailRequest, null);
            newDetail.setCalendar(existingEntity);
            currentDetails.add(newDetail);
        }
        return existingEntity;


    }

    public CalendarUpdateResponse  toResponse(CalendarEntity entity, CompanyEntity company, Long calId) {
        List<CalendarDetailResponse> detailResponses = entity.getCalendarDetailEntity().stream()
                .map(calendarUpdateDetailConvertor:: toResponse)
                .collect(Collectors.toList());

        return CalendarUpdateResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(company.getCpId())
                .details(detailResponses)
                .build();
    }

}
