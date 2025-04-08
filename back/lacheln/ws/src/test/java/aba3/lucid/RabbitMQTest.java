package aba3.lucid;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() {
        for (int i = 1; i <= 5; i++) {
            String message = "Test Message " + i;
            System.out.println("▶ Sending: " + message);
            rabbitTemplate.convertAndSend("company.exchange", "company", message);
        }
    }

}
