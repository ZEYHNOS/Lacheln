package aba3.lucid.payment.schduler;

import aba3.lucid.payment.business.PaymentBusiness;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopularScheduler {

    private final PaymentBusiness paymentBusiness;

//    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
@Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void createPopularProduct() throws JsonProcessingException {
        paymentBusiness.createPopularProductList();
    }

}
