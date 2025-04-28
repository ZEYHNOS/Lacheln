package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PayManagementResponse {

    private String payId;
    private String userId;
    private String payTool;
    private BigInteger payTotalPrice;
    private BigDecimal payDcPrice;
    private String payStatus;
    private BigInteger payRefundPrice;
    private LocalDateTime payRefundDate;
    private BigInteger payMileage;
    private String payImpUid;
    private LocalDateTime paidAt;

    private List<PayDetailResponse> payDetails;




}
