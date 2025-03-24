package aba3.lucid.scheduler;

import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredDataRemover {


    private final ProductService productService;

//          "0 0 * * * *" = 매 시간마다 실행한다.
//          "*/10 * * * * *" = 매 10초마다 실행한다.
//          "0 0 8-10 * * *" = 매일 8, 9, 10시에 실행한다
//          "0 0 6,19 * * *" = 매일 오전 6시, 오후 7시에 실행한다.
//          "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
//          "0 0 9-17 * * MON-FRI" = 오전 9시부터 오후 5시까지 주중(월~금)에 실행한다.
//          "0 0 0 25 12 ?" = every Christmas Day at midnight

    // 삭제 상태인 상품 1달 뒤 삭제 TODO (1달 뒤로 변경하기)
    @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
    public void productRemove() {
        productService.removeData();
    }

}
