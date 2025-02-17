package aba3.lucid.product;

import aba3.lucid.company.CompanyEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class productEntity {

    // 상품 id(기본키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdId;

    // 업체 id(외래키)
    @Column(name = "cpId")
    private long cpId;

    // 상품명
    @Column(nullable = false)
    private String pdName;

    // 상품 가격
    @Column(nullable = false)
    private int pdPrice;

    // 상품 재고()
    @Column(nullable = false)
    private int pdStock;

    // 상품 상태
    @Column(nullable = false)
    private String pdStatus;

    // 상품 추천 여부
    @Column(nullable = false)
    private String pdRec;

    // 상품 카테고리
    // TODO Enum 제작(스, 드, 메)
    @Column(nullable = false)
    private String pdCategory;

    // 상품 이용 시간
    @Column(nullable = false)
    private int pdTasktime;

    // 상품 색상
    @Column(nullable = false)
    private String pdColor;
}
