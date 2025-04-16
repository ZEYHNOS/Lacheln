package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
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
import org.springframework.transaction.annotation.Transactional;


@Business
@Component
@RequiredArgsConstructor
public class TemporaryHolidayBusiness {
    private final TemporaryHolidayConvertor temporaryHolidayConvertor;
    private final TemporaryHolidayService temporaryHolidayService;
    private final CompanyRepository companyRepository;
    private final TemporaryHolidayRepository temporaryHolidayRepository;

    public TemporaryHolidayResponse createHoliday(TemporaryHolidayRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND,"업체를 찾을 수 없습니다." + cpId));

        TemporaryHolidayEntity temporaryHolidayEntity =  temporaryHolidayConvertor.toEntity(request, company);
        TemporaryHolidayEntity savedEntity = temporaryHolidayService.createTemporaryHoliday(temporaryHolidayEntity);
        return temporaryHolidayConvertor.toResponse(savedEntity);
    }

    @Transactional
    public TemporaryHolidayResponse updateTemporaryHoliday(TemporaryHolidayRequest request, long temHolidayId, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND,"업체를 찾을 수 없습니다." + cpId )
        );
        TemporaryHolidayEntity temporaryHolidayEntity = temporaryHolidayRepository.findByTemHolidayId(temHolidayId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "정보가 없습니다"));


        temporaryHolidayEntity.setThDate(request.getDate());
        temporaryHolidayEntity.setThReason(request.getReason());
        TemporaryHolidayEntity updatedEntity = temporaryHolidayRepository.save(temporaryHolidayEntity);
        return temporaryHolidayConvertor.toResponse(updatedEntity);



    }





}
