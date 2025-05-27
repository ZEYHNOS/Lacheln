package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.TemporaryHolidayConvertor;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayRequest;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayResponse;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import aba3.lucid.schedule.Service.TemporaryHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Business
@Component
@RequiredArgsConstructor
public class TemporaryHolidayBusiness {
    private final TemporaryHolidayConvertor temporaryHolidayConvertor;
    private final TemporaryHolidayService temporaryHolidayService;
    private final CompanyService companyService;

    public TemporaryHolidayResponse createHoliday(TemporaryHolidayRequest request, Long cpId) {
        Validator.throwIfInvalidId(cpId);
        Validator.throwIfNull(request);
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        TemporaryHolidayEntity temporaryHolidayEntity =  temporaryHolidayConvertor.toEntity(request, company);
        temporaryHolidayEntity.setCompany(company);
        TemporaryHolidayEntity savedEntity = temporaryHolidayService.createTemporaryHoliday(temporaryHolidayEntity);
        return temporaryHolidayConvertor.toResponse(savedEntity);
    }


    public TemporaryHolidayResponse updateTemporaryHoliday(TemporaryHolidayRequest request, long temHolidayId, Long cpId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(cpId,temHolidayId);
        companyService.findByIdWithThrow(cpId);
        temporaryHolidayService.findByIdWithThrow(temHolidayId);
        TemporaryHolidayEntity updatedEntity = temporaryHolidayService.updateTemporaryHoliday(request,temHolidayId);
        return temporaryHolidayConvertor.toResponse(updatedEntity);


    }





}
