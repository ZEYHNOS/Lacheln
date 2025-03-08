package aba3.lucid.domain.product;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.CompanyEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Getter
@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    // 상품 id(기본키)
    @Id
    @Column(name = "pd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdId;

    // 업체
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 상품명
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String pdName;

    // 상품 가격
    @Column(nullable = false)
    private BigInteger pdPrice;

    // 상품 재고()
    @Column(nullable = false)
    private int pdStock;

    // 상품 상태
    @Column(nullable = false, columnDefinition = "CHAR(20)")
    @Enumerated(EnumType.STRING)
    private ProductStatus pdStatus;

    // 상품 추천 여부
    @Column(nullable = false, columnDefinition = "CHAR(1)")
    @Enumerated(EnumType.STRING)
    private BinaryChoice pdRec;

    // 상품 소요 시간
    @Column(nullable = false)
    private int pdTaskTime;

    // 상품 색상
    @Column(nullable = false)
    private String pdColor;

    // 상품 설명(블로그처럼 이미지, 동영상을 업체가 자유롭게 저장)
    @Column(name = "pd_description", columnDefinition = "LONGTEXT")
    private String pdDescription;

    // 옵션 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OptionEntity> opList;


    // 상품 태그 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<HashtagEntity> hashtagList;

}
