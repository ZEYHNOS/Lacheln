package aba3.lucid.listener;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.product.enums.ReviewCommentStatus;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.repository.ReviewCommentRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
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
    private final PayManagementRepository payManagementRepository;

    @RabbitListener(queues = "review.comment.queue")
    public void receive(ReviewCommentEventDto eventDto, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ReviewEntity review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow();
            CompanyEntity company = companyRepository.findById(eventDto.getCpId()).orElseThrow();
            PayManagementEntity pay = payManagementRepository.findById(eventDto.getPayId()).orElseThrow(null);
            ReviewCommentEntity comment = ReviewCommentEntity.builder()
                    .review(review)
                    .company(company)
                    .payManagement(pay)
                    .rvcContent("")
                    .rvcCreate(LocalDate.now())
                    .rvcStatus(ReviewCommentStatus.REPLY_NEEDED)
                    .build();
            reviewCommentRepository.save(comment);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            log.error("review_comment 오류 {}", e.getMessage(), e);
            channel.basicNack(deliveryTag,false, true);
        }
    }
}
