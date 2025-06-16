package aba3.lucid.alert.business;

import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.alert.converter.AlertConverter;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.sse.service.SseService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CompanyAlertBusiness {

    private final AlertConverter alertConverter;
    private final CompanyAlertService companyAlertService;
    private final CompanyService companyService;
    private final SseService sseService;

    private final RabbitTemplate rabbitTemplate;


    // 알림 생성하기
    public void alertRegister(CompanyAlertDto dto, Long companyId) {
        Validator.throwIfNull(dto);
        Validator.throwIfInvalidId(companyId);

        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CompanyAlertEntity entity = alertConverter.toEntity(dto, company);
        sseService.sendAlert(company.getCpId(), dto);

        companyAlertService.alertRegister(entity);
    }



    // 알림 삭제하기
    public void deleteAlert(List<Long> alertId) {
        companyAlertService.deleteByIdList(alertId);
    }

    // 알림 읽음
    public void readAlert(List<Long> alertId) {
        List<CompanyAlertEntity> entity = companyAlertService.findAllById(alertId);
        companyAlertService.readAlert(entity);
    }

    // 알림 리스트
    public List<MutualAlert> getAlertList(Long companyId) {
        Validator.throwIfInvalidId(companyId);

        return companyAlertService.getAlertList(companyId).stream()
                .map(alertConverter::toDto)
                .toList()
                ;
    }


    @RabbitListener(queues = "company", ackMode = "MANUAL", concurrency = "2")
    public void consume(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // JSON 변환 직접 수행
            CompanyAlertDto dto = (CompanyAlertDto) rabbitTemplate.getMessageConverter().fromMessage(message);
            
            CompanyEntity company = companyService.findByIdWithThrow(dto.getCompanyId());
            CompanyAlertEntity entity = alertConverter.toEntity(dto, company);

            companyAlertService.alertRegister(entity);
            sseService.sendAlert(company.getCpId(), dto);
            log.info("dto : {}", dto);

            // 정상 처리되었으므로 ACK
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("v: {}", e.getMessage(), e);

            // 메시지를 다시 큐에 넣고 재시도하도록 설정
            channel.basicAck(deliveryTag, false);
        }
    }
}
