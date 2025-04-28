package aba3.lucid.domain.payment.entity;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name="pay_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayManagementEntity {

    // 결제 내역 ID(MerchantUid)
    @Id
    @Column(name = "pay_id", columnDefinition = "VARCHAR(105)")
    private String payId;

    // 사용자
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity user;

    // 결제 수단
    @Column(name = "pay_tool", columnDefinition = "VARCHAR(50)", nullable = false)
    private String payTool;

    // 원가
    @Column(name = "pay_total_price", nullable = false)
    private BigInteger payTotalPrice;

    // 할인 금액
    @Column(name = "pay_dc_price", nullable = false)
    private BigDecimal payDcPrice;

    // 상태
    @Column(name = "pay_status", columnDefinition = "VARCHAR(20)", nullable = false)
    private String payStatus;

    // 환불금액
    @Column(name = "pay_refund_price")
    private BigInteger payRefundPrice;

    // 환불 금액
    @Column(name = "pay_refund_date")
    private LocalDateTime payRefundDate;

    // 환불 일자
    @Column(name = "pay_mileage", nullable = false)
    private BigInteger payMileage;

    // 아임포트 UID (결제 대행사가 보내주는 ID)
    @Column(name = "pay_imp_uid", columnDefinition = "VARCHAR(50)")
    private String payImpUid;

    // 결제 일시
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @JsonIgnore
    @OneToMany(mappedBy = "payManagement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayDetailEntity> payDetailEntityList;


    public void refund() {
        this.payStatus = "REFUND";
        this.payRefundDate = LocalDateTime.now();
    }

    public void updatePayDetailEntityList(List<PayDetailEntity> payDetailEntityList) {
        if (payDetailEntityList == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        this.payDetailEntityList = payDetailEntityList;
    }


}
