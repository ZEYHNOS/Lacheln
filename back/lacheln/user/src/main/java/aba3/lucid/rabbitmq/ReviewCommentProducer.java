package aba3.lucid.rabbitmq;

import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCommentProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.review_comment}")
    private String exchange;

    @Value("${rabbitmq.routing.review_comment}")
    private String routingKey;

    public void sendReviewCommentEvent(ReviewCommentEventDto eventDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, eventDto);
    }
}
