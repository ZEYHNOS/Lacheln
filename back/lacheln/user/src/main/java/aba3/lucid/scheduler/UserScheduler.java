package aba3.lucid.scheduler;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.payment.service.PayDetailService;
import aba3.lucid.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final PayDetailService payDetailService;
    private final ReviewService reviewService;

    @Scheduled(cron = "0 0 19 * * *", zone = "Asia/Seoul")
    public void createReview() {
        List<PayDetailEntity> payDetailEntityList = payDetailService.replyNeedUserList();
        reviewService.create(payDetailEntityList);
    }


}
