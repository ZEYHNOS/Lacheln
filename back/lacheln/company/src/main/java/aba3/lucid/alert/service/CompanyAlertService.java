package aba3.lucid.alert.service;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyAlertRepository;
import aba3.lucid.domain.packages.entity.PackageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyAlertService {

    private final CompanyAlertRepository companyAlertRepository;

    private final RabbitTemplate rabbitTemplate;

    // 알림 등록
    public void alertRegister(CompanyAlertEntity entity) {
        companyAlertRepository.save(entity);
    }



    // 알림 읽음
    @Transactional
    public void readAlert(List<CompanyAlertEntity> entityList) {
        entityList.forEach(CompanyAlertEntity::readAlert);
        companyAlertRepository.saveAll(entityList);
    }

    // 알림 삭제(id)
    @Transactional
    public void deleteByIdList(List<Long> cpAlertId) {
        companyAlertRepository.deleteAllById(cpAlertId);
    }


    // 알림 리스트
    public List<CompanyAlertEntity> getAlertList(Long companyId) {
        return companyAlertRepository.findAllByCompany_CpIdAndCpAlertRead(companyId, BinaryChoice.N);
    }


    // id를 통해 알림 찾기
    public CompanyAlertEntity findByIdWithThrow(Long cpAlertId) {
        return companyAlertRepository.findById(cpAlertId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public List<CompanyAlertEntity> findAllById(List<Long> cpAlertId) {
        return companyAlertRepository.findAllById(cpAlertId);
    }

    // 업체를 구독한 사용자에게 알림 보내기
    // TODO 업체 구독 로직 작성 후 만들기
    public void sendAlertToSubscribedUsers(Long companyId, String title, String content) {

    }

    // 업체에게 알림 보내기
    public void sendAlertCompany(List<CompanyAlertDto> dtoList) {
        for (CompanyAlertDto dto : dtoList) {
            rabbitTemplate.convertAndSend("company.exchange", "company", dto);
        }
    }

    public void sendAlertCompany(CompanyAlertDto dto) {
        rabbitTemplate.convertAndSend("company.exchange", "company", dto);
    }

    public void sendAlertCompany(PackageEntity entity) {
        List<CompanyEntity> companies = List.of(
                entity.getPackAdmin(),
                entity.getPackCompany1(),
                entity.getPackCompany2()
        );

        List<CompanyAlertDto> alertDtoList = companies.stream()
                .map(company -> CompanyAlertDto.invitationPackage(company, entity))
                .toList();

        sendAlertCompany(alertDtoList);
    }


}
