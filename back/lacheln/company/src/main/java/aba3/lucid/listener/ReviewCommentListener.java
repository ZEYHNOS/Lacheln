package aba3.lucid.listener;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.product.enums.CommentStatus;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.repository.ReviewCommentRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.comment.business.ReviewCommentBusiness;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewCommentListener {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final CompanyRepository companyRepository;
    private final ReviewCommentBusiness reviewCommentBusiness;

    @RabbitListener(queues = "review.comment.queue")
    public void receive(ReviewCommentEventDto eventDto, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ReviewEntity review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow();
            CompanyEntity company = companyRepository.findById(eventDto.getCpId()).orElseThrow();
            ReviewCommentEntity comment = ReviewCommentEntity.builder()
                    .reviewId(eventDto.getReviewId())
                    .company(company)
                    .rvcContent("")
                    .rvcCreate(LocalDate.now())
                    .rvcStatus(CommentStatus.REPLY_NEEDED)
                    .build();
            reviewCommentRepository.save(comment);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            log.error("review_comment 오류 {}", e.getMessage(), e);
            channel.basicNack(deliveryTag,false, true);
        }
    }

    @RabbitListener(queues = "comment.delete.queue")
    public void delete(ReviewCommentEventDto eventDto, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            Long reviewId = eventDto.getReviewId();
            reviewCommentBusiness.deleteReviewComment(reviewId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("review-comment 삭제 오류 {}",e.getMessage(), e);
            channel.basicAck(deliveryTag, false);
        }
    }








}
