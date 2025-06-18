package aba3.lucid.domain.payment.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.enums.CompanyCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // 상품 이름
    @Column(name = "pay_pd_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String productName;

    // 상품 이미지 Url
    @Column(name = "pay_image_url", nullable = false)
    private String imageUrl;

    // 쿠폰
    @Column(name = "coupon_name")
    private String couponName;

    // 가격
    @Column(name = "pay_cost", nullable = false)
    private BigInteger payCost;

    // 할인된 가격
    @Column(name = "pay_dc_price", nullable = false)
    private BigInteger payDcPrice;

    // 작업 시작 시간
    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;
    
    // 작업 종료 시간
    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    // 소요 시간(분)
    @Column(name = "task_time", nullable = false)
    private LocalTime taskTime;

    // 카테고리
    @Column(name = "category", nullable = false)
    private CompanyCategory category;

    // 담당자
    @Column(name = "manager")
    private String manager;

    @JsonIgnore
    @OneToMany(mappedBy = "payDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayDetailOptionEntity> payDetailOptionEntityList;

    public void updatePayDetailOptionEntity(List<PayDetailOptionEntity> payDetailOptionEntityList) {
        if (payDetailOptionEntityList == null) {
            return;
        }

        this.payDetailOptionEntityList = payDetailOptionEntityList;
    }

    public void updateManager(String manager) {
        this.manager = manager;
    }

}
