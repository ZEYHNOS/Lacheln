package aba3.lucid.product.listener;

import aba3.lucid.domain.product.dto.ProductSnapshot;
import aba3.lucid.product.service.ProductService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.MessageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductListener {

    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "snapshot.queue.request")
    public void verifySnapshot(Message message, Channel channel) throws IOException {
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            List<ProductSnapshot> productSnapshotList = (List<ProductSnapshot>) rabbitTemplate.getMessageConverter().fromMessage(message);
            log.info("ProductVerify productSnapshotList : {}", productSnapshotList);
            productService.verifySnapshotListMatch(productSnapshotList);

            MessageProperties replyProps = new MessageProperties();
            replyProps.setCorrelationId(correlationId);

            Message replyMessage = rabbitTemplate.getMessageConverter().toMessage(true, replyProps);

            rabbitTemplate.send(replyTo, replyMessage);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
