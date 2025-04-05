package aba3.lucid.domain.calendar.convertor;


import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import org.springframework.stereotype.Component;

@Component
public class CalendarConvertor {

    public CalendarEntity toEntity(CalendarRequest request) {
        if(request == null) {
            return null;
        }

        return CalendarEntity.builder()
                .calDate(request.getDate())
                .build();
    }

    public CalendarResponse toResponse(CalendarEntity entity) {
        if(entity == null) {
            return null;
        }

        return CalendarResponse.builder()
                .calId(entity.getCalId())
                .calDate(entity.getCalDate())
                .companyId(entity.getCompany().getCpId())
//                .details(List<CalendarDetailResponse>))
                .build();

    }





}
