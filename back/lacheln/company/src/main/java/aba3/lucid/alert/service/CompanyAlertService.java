package aba3.lucid.alert.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.repository.CompanyAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyAlertService {

    private final CompanyAlertRepository companyAlertRepository;

    private final RabbitTemplate rabbitTemplate;

    // 알림 등록
    public void alertRegister(CompanyAlertEntity entity) {
        companyAlertRepository.save(entity);
    }

    // 알림 읽음
    public void readAlert(CompanyAlertEntity entity) {
        entity.readAlert();

        companyAlertRepository.save(entity);
    }

    // 알림 삭제(id)
    public void deleteById(Long cpAlertId) {
        companyAlertRepository.deleteById(cpAlertId);
    }


    // 알림 리스트
    public List<CompanyAlertEntity> getAlertList(Long companyId) {
        return companyAlertRepository.findAllByCompany_CpId(companyId);
    }


    // id를 통해 알림 찾기
    public CompanyAlertEntity findByIdWithThrow(Long cpAlertId) {
        return companyAlertRepository.findById(cpAlertId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    // 업체를 구독한 사용자에게 알림 보내기
    // TODO 업체 구독 로직 작성 후 만들기
    public void sendAlertToSubscribedUsers(Long companyId, String title, String content) {

    }

    // 업체에게 알림 보내기
    public void sendAlertCompany(CompanyAlertDto dto) {
        log.info("Consumer : {}", dto);
        rabbitTemplate.convertAndSend("", "company", dto);
    }


}
