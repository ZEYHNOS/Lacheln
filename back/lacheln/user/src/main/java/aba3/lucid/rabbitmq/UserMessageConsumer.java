package aba3.lucid.rabbitmq;

import aba3.lucid.alert.UserAlertService;
import aba3.lucid.domain.alert.converter.AlertConverter;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.sse.UserSseService;
import aba3.lucid.user.service.UserService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final UserAlertService userAlertService;
    private final UserService userService;
    private final AlertConverter alertConverter;
    private final UserSseService sseService;

    @RabbitListener(queues = "user.queue")
    public void receiveMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // JSON 변환 직접 수행
            UserAlertDto dto = (UserAlertDto) rabbitTemplate.getMessageConverter().fromMessage(message);

            log.info("dto : {}", dto);
            UsersEntity user = userService.findByIdWithThrow(dto.getUserId());
            UserAlertEntity userAlertEntity = alertConverter.toEntity(dto, user);

            userAlertService.alertRegister(userAlertEntity);
            sseService.sendAlert(user.getUserId(), dto);

            // 정상 처리되었으므로 ACK
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);

            // 메시지를 다시 큐에 넣고 재시도하도록 설정
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
