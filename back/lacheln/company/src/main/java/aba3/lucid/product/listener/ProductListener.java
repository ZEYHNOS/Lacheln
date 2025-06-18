package aba3.lucid.product.listener;

import aba3.lucid.domain.payment.dto.PopularDto;
import aba3.lucid.domain.product.dto.ProductSnapshot;
import aba3.lucid.product.business.ProductBusiness;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductListener {

    private final ProductBusiness productBusiness;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "snapshot.request.queue")
    public void verifySnapshot(Message message, Channel channel) throws IOException {
        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            List<ProductSnapshot> productSnapshotList = (List<ProductSnapshot>) rabbitTemplate.getMessageConverter().fromMessage(message);
            log.info("ProductVerify productSnapshotList : {}", productSnapshotList);
            productBusiness.verifySnapshotListMatch(productSnapshotList);

            MessageProperties replyProps = new MessageProperties();
            replyProps.setCorrelationId(correlationId);

            Message replyMessage = rabbitTemplate.getMessageConverter().toMessage(true, replyProps);

            rabbitTemplate.send(replyTo, replyMessage);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

    @RabbitListener(queues = "popular.queue")
    public void popularListener(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody());
            log.info("json : {}", json);
            ObjectMapper objectMapper = new ObjectMapper();
            String raw = new String(message.getBody(), StandardCharsets.UTF_8);

            List<PopularDto> list = objectMapper.readValue(
                    objectMapper.readValue(raw, String.class),  // 1차 파싱해서 JSON 문자열 뽑기
                    new TypeReference<List<PopularDto>>() {}    // 2차 파싱
            );

            productBusiness.createPopularProduct(list);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("{}", e);
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
