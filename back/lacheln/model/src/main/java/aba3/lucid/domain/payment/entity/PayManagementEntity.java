package aba3.lucid.domain.payment.entity;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 업체
    @JoinColumn(name = "cp_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 쿠폰
    @JoinColumn(name = "coupon_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon;

    // 사용자
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity user;

    // 상품명
    @Column(name = "pay_pd_name", columnDefinition = "VARCHAR(100)", nullable = false)
    private String payPdName;

    // 결제 수단
    @Column(name = "pay_tool", columnDefinition = "VARCHAR(50)", nullable = false)
    private String payTool;

    // 원가
    @Column(name = "pay_cost", nullable = false)
    private BigInteger payCost;

    // 할인 금액
    @Column(name = "pay_dc_price", nullable = false)
    private BigInteger payDcPrice;

    // 상태
    @Column(name = "pay_status", columnDefinition = "VARCHAR(20)", nullable = false)
    private String payStatus;

    // 환불금액
    @Column(name = "pay_refund_price", nullable = false)
    private BigInteger payRefundPrice;

    // 환불 금액
    @Column(name = "pay_refund_date", nullable = false)
    private LocalDateTime payRefundDate;

    // 환불 일자
    @Column(name = "pay_mileage", nullable = false)
    private BigInteger payMileage;

    // 아임포트 UID (결제 대행사가 보내주는 ID)
    @Column(name = "pay_imp_uid", columnDefinition = "VARCHAR(50)")
    private String payImpUid;

    @JsonIgnore
    @OneToMany(mappedBy = "payManagement", cascade = CascadeType.ALL)
    private List<PayDetailEntity> payDetailEntityList;


    public void refund() {
        this.payStatus = "REFUND";
        this.payRefundDate = LocalDateTime.now();
    }

}
