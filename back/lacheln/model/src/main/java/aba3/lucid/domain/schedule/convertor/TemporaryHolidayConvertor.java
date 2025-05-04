package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayRequest;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayResponse;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TemporaryHolidayConvertor {

    public TemporaryHolidayEntity toEntity(TemporaryHolidayRequest request, CompanyEntity company) {
        if(request == null){
            return null;
        }

        return TemporaryHolidayEntity.builder()
                .company(company)
                .thDate(LocalDate.of(2025,04,15))
                .thReason(request.getReason())
                .build();
    }

    public TemporaryHolidayResponse toResponse(TemporaryHolidayEntity entity) {
        if(entity == null){
            return null;
        }
        return TemporaryHolidayResponse.builder()
                .id(entity.getTemHolidayId())
                .cpId(entity.getCompany().getCpId())
                .date(entity.getThDate())
                .reason(entity.getThReason())
                .build();
    }

    public void updateEntity(TemporaryHolidayEntity entity, TemporaryHolidayRequest request) {
        entity.setThDate(request.getDate());
        entity.setThReason(request.getReason());

    }

}
