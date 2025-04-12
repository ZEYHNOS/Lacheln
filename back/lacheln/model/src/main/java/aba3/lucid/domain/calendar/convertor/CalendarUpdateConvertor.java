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
        existingEntity.setCalDate(request.getDate());
        existingEntity.setCompany(company);


        List<CalendarDetailEntity> detailEntities = new ArrayList<>();
        for(CalendarDetailRequest detailRequest : request.getDetails()) {
            CalendarDetailEntity existingDetail = findExistingDetail(existingEntity, detailRequest);
            CalendarDetailEntity updatedDetail = calendarUpdateDetailConvertor.toEntity(detailRequest, existingDetail);
            detailEntities.add(updatedDetail);

            updatedDetail.setCalendar(existingEntity);
            detailEntities.add(updatedDetail);

        }
        existingEntity.setCalendarDetailEntity(detailEntities);
        return existingEntity;


    }

    private CalendarDetailEntity findExistingDetail(CalendarEntity existingEntity, CalendarDetailRequest detailRequest) {
        return null;
    }

    public CalendarUpdateResponse  toResponse(CalendarEntity entity, CompanyEntity company, Long calId) {
        List<CalendarDetailResponse> detailResponses = entity.getCalendarDetailEntity().stream()
                .map(calendarUpdateDetailConvertor::toResponse)
                .collect(Collectors.toCollection(ArrayList::new));

        return CalendarUpdateResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
                .details(detailResponses)
                .build();
    }

}
