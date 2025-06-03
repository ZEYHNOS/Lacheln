package aba3.lucid.domain.payment.dto;

import aba3.lucid.domain.product.enums.DressSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailBlockResponse {
    private Long pdId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 드레스 사이즈 별로 예약 되어 있는것
    private Map<Long, DressDetail> dressQuantity;

    // 메이크 업 담당자 이름
    private String managerName;

    // 드레스 개수 및 시작시간 종료시간
    @Builder
    public static class DressDetail {
        private Integer dressQuantity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private DressSize dressSize;
    }
}