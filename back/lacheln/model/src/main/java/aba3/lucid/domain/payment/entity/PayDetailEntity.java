package aba3.lucid.domain.payment.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "pay_detail")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PayDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payDetailId;

    @JoinColumn(name = "pay_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PayManagementEntity payManagement;

    // 업체
    @Column(name = "cp_id")
    private Long cpId;

    // 상품ID
    @Column(name = "pd_id")
    private Long pdId;

    // 쿠폰
    @Column(name = "coupon_id")
    private Long couponId;

    // 상품 이름
    @Column(name = "pay_pd_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String productName;

    // 가격
    @Column(name = "pay_cost", nullable = false)
    private BigInteger payCost;

    // 할인된 가격
    @Column(name = "pay_dc_price", nullable = false)
    private BigInteger payDcPrice;

    // 작업 시작 시간
    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate;

    // 소요 시간(분)
    @Column(name = "task_time", nullable = false)
    private Integer taskTime;

    @JsonIgnore
    @OneToMany(mappedBy = "payDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayDetailOptionEntity> payDetailOptionEntityList;

    public void updatePayDetailOptionEntity(List<PayDetailOptionEntity> payDetailOptionEntityList) {
        if (payDetailOptionEntityList == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        this.payDetailOptionEntityList = payDetailOptionEntityList;
    }

}
