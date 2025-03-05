package aba3.lucid.domain.payment;


import aba3.lucid.domain.company.CompanyEntity;
import aba3.lucid.domain.coupon.CouponEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.product.ProductEntity;
import aba3.lucid.domain.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="pay_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayManagementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private long payId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @Column(name = "pay_tool", length = 50, nullable = false)
    private String payTool; // 결제 수단 (간편 결제, Visa 등)

    @Column(name = "pay_schedule", nullable = false)
    private LocalDateTime paySchedule;

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate;

    @Column(name = "pay_cost", nullable = false)
    private long payCost; //원가

    @Column(name = "pay_dc_price", nullable = false)
    private long payDcPrice; //할인 금액 (기본값 0) //기본형 long 필드는 자동으로 0L 설정된다

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", columnDefinition = "char(20)", nullable = false)
    private PaymentStatus payStatus; // 납부 취소

    @Column(name = "pay_refund_price", nullable = false)
    private long payRefundPrice; //환불 금액(기본값 0)  기본형 long 필드는 자동으로 0L 설정된다

    @Column(name = "pay_refund_date", nullable = false)
    private LocalDateTime payRefundDate;  //환불 일자

    @Column(name = "pay_millage", nullable = false)
    private long payMillage;
}
