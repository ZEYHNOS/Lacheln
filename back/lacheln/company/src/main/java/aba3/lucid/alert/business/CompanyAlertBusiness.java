package aba3.lucid.alert.business;

import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.convertor.CompanyAlertConverter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CompanyAlertBusiness {

    private final CompanyAlertConverter companyAlertConverter;
    private final CompanyAlertService companyAlertService;
    private final CompanyService companyService;


    // 알림 생성하기
    public void alertRegister(MutualAlert dto, Long companyId) {
        Validator.throwIfNull(dto);
        Validator.throwIfInvalidId(companyId);

        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CompanyAlertEntity entity = companyAlertConverter.toEntity(dto, company);

        companyAlertService.alertRegister(entity);
    }



    // 알림 삭제하기
    public void deleteAlert(Long alertId) {
        Validator.throwIfInvalidId(alertId);

        companyAlertService.deleteById(alertId);
    }

    // 알림 읽음
    public void readAlert(Long alertId) {
        Validator.throwIfInvalidId(alertId);

        CompanyAlertEntity entity = companyAlertService.findByIdWithThrow(alertId);
        companyAlertService.readAlert(entity);
    }

    // 알림 리스트
    public List<MutualAlert> getAlertList(Long companyId) {
        Validator.throwIfInvalidId(companyId);

        return companyAlertService.getAlertList(companyId).stream()
                .map(companyAlertConverter::toDto)
                .toList()
                ;
    }


    @RabbitListener(queues = "company")
    public void consume(CompanyAlertDto dto){
        Validator.throwIfNull(dto);

        log.info("Producer : {}", dto);
        CompanyEntity company = companyService.findByIdWithThrow(dto.getCompanyId());
        CompanyAlertEntity entity = companyAlertConverter.toEntity(dto, company);
        companyAlertService.alertRegister(entity);
    }
}
