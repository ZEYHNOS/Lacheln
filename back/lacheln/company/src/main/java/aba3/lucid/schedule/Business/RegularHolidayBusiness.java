package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.converter.RegularHolidayConverter;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.schedule.Service.RegularHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Business
@RequiredArgsConstructor
@Component
public class RegularHolidayBusiness {
    private final RegularHolidayConverter regularHolidayConverter;
    private final CompanyService companyService;
    private final RegularHolidayService regularHolidayService;


    public RegularHolidayResponse createHoliday(RegularHolidayRequest request,Long cpId) {
        Validator.throwIfNull(request);
        Validator.throwIfNull(cpId);
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        //새로운 entity 만들고 해당 업체에 맞줘기
        RegularHolidayEntity entity = regularHolidayConverter.toEntity(request, cpId);
        entity.setCompany(company);
        RegularHolidayEntity savedEntity = regularHolidayService.createRegularHoliday(entity);
        return regularHolidayConverter.toResponse(savedEntity);

    }


    public RegularHolidayResponse updateHoliday( long cpId, long regHolidayId,RegularHolidayRequest request ) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(cpId, regHolidayId);
        //업체가 존재하는지 확인
        companyService.findByIdWithThrow(cpId);
        regularHolidayService.findByThrowId(regHolidayId);
        RegularHolidayEntity updatedEntity = regularHolidayService.updateRegularHoliday(request, regHolidayId);
        return regularHolidayConverter.toResponse(updatedEntity);

    }





}
