package aba3.lucid.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.user_to_company}")
    private String exchange;

    @Value("${spring.rabbitmq.routinguser_to_company_key}")
    private String routingKey;

    public void sendMessage(String message)   {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("User → Company 메시지 전송: " + message);
    }
}
