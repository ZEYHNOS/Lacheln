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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CompanyAlertBusiness {

    private final CompanyAlertConverter companyAlertConverter;
    private final CompanyAlertService companyAlertService;
    private final CompanyService companyService;

    private final ObjectMapper objectMapper;


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


    @RabbitListener(queues = "company", ackMode = "MANUAL", concurrency = "5")
    public void consume(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 🚀 JSON 변환 직접 수행
            CompanyAlertDto dto = objectMapper.readValue(message.getBody(), CompanyAlertDto.class);

            log.info("✅ Received Message: {}", dto);

            // 정상 처리되었으므로 ACK
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("❌ Error processing message: {}", e.getMessage(), e);

            // 메시지를 다시 큐에 넣고 재시도하도록 설정
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
