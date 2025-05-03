package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.RegularHolidayConvertor;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.repository.RegularHolidayRepository;
import aba3.lucid.schedule.Service.RegularHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
@Component
public class RegularHolidayBusiness {
    private final RegularHolidayRepository regularHolidayRepository;
    private final RegularHolidayConvertor regularHolidayConvertor;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final RegularHolidayService regularHolidayService;


    public RegularHolidayResponse createHoliday(RegularHolidayRequest request,Long cpId) {
        Validator.throwIfNull(request);
        Validator.throwIfNull(cpId);
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        //새로운 entity 만들고 업체 해당하기
        RegularHolidayEntity entity = regularHolidayConvertor.toEntity(request, cpId);
        entity.setCompany(company);  ///?????
        RegularHolidayEntity savedEntity = regularHolidayService.createRegularHoliday(entity);
        return regularHolidayConvertor.toResponse(savedEntity);

    }


    public RegularHolidayResponse updateHoliday( long cpId, long regHolidayId,RegularHolidayRequest request ) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(cpId);
        //업체가 존재하는지 확인
        companyService.findByIdWithThrow(cpId);

        RegularHolidayEntity updatedEntity = regularHolidayService.updateRegularHoliday(request, cpId);
        return regularHolidayConvertor.toResponse(updatedEntity);

    }





}
