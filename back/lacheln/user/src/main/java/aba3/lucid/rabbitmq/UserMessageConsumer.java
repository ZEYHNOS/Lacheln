package aba3.lucid.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserMessageConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.company_queue}")
    public void receiveMessage(Message message)  {
        System.out.println(message.getMessageProperties());
    }
}
