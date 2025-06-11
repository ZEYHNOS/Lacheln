package aba3.lucid.listener;

import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.comment.business.ReviewCommentBusiness;
import aba3.lucid.comment.service.ReviewCommentService;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewCommentListener {

    private final CompanyAlertService companyAlertService;
    private final ReviewCommentBusiness reviewCommentBusiness;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "review.comment.queue")
    public void receive(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ReviewCommentEventDto eventDto = (ReviewCommentEventDto) rabbitTemplate.getMessageConverter().fromMessage(message);
            reviewCommentBusiness.initBaseReviewComment(eventDto);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            log.error("review_comment 오류 {}", e.getMessage(), e);
            channel.basicNack(deliveryTag,false, true);

        }
    }

    @RabbitListener(queues = "comment.delete.queue")
    public void delete(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ReviewCommentEventDto eventDto = (ReviewCommentEventDto) rabbitTemplate.getMessageConverter().fromMessage(message);
            Long reviewId = eventDto.getReviewId();
            reviewCommentBusiness.deleteReviewCommentByReviewId(reviewId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("review-comment 삭제 오류 {}",e.getMessage(), e);
            channel.basicAck(deliveryTag, false);
        }
    }








}
