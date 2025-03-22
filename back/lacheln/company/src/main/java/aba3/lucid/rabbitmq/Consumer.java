package aba3.lucid.rabbitmq;

import aba3.lucid.product.business.ProductBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

    private final ProductBusiness productBusiness;

    // 업체 큐의 메시지가 컨슘되는지 확인하기위해 로그 추가
    @RabbitListener(queues = "company")
    public void consume(Message message){
        log.info("consumer consumes message: {}",message);
        log.info("Body : {}", new String(message.getBody()));
    }

}