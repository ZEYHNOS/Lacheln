package aba3.lucid.Payment;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="pay_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payManagementId;

    private String userId;

    private long pdId;

    private String couponId;

    private String payTool;

    private String payDate;

    private String payCost; //원가

    private String payDisPrice; //할인 금액

    private String payStatus; //납부 취소

    private String payRefundPrice; //환불 금액

    private String payRefundDate;  //환불 일자



}
