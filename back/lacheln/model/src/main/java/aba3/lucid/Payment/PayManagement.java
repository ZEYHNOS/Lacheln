package aba3.lucid.Payment;


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
public class PayManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private long payId;

    @Column(name = "user_id", columnDefinition = "char(36)", nullable = false)
    private String userId;  // 추후 ManyToOne으로 수정

    @Column(name = "pd_id", nullable = false )
    private long pdId;  // 추후 ManyToOne으로 수정

    @Column(name = "coupon_id", columnDefinition = "char(15)", nullable = false )
    private String couponId;

    @Column(name = "pay_tool", length = 50, nullable = false)
    private String payTool; // 결제 수단 (간편 결제, Visa 등)

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate;

    @Column(name = "pay_cost", nullable = false)
    private long payCost; //원가

    @Column(name = "pay_dc_price", nullable = false)
    private long payDcPrice; //할인 금액 (기본값 0) //기본형 long 필드는 자동으로 0L 설정된다


    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", columnDefinition = "char(20)", nullable = false)
    private PayStatus payStatus; //납부 취소

    @Column(name = "pay_refund_price", nullable = false)
    private long payRefundPrice; //환불 금액(기본값 0)  기본형 long 필드는 자동으로 0L 설정된다

    @Column(name = "pay_refund_date", nullable = false)
    private LocalDateTime payRefundDate;  //환불 일자



}
