package aba3.lucid.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserMessageConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.queue.company_queue}")
    public void receiveMessage(String message)  {
        System.out.println(message);
    }
}
