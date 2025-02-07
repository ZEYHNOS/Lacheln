package aba3.lucid.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    // 상품 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdId;

    // 업체 Id(외래키)
    @Column(nullable=false)
    private Long cpId;

    // 상품명
    @Column(nullable=false)
    private String name;

    // 상품정보
    @Column(nullable=false)
    private String info;
    
    // 가격
    @Column(nullable=false)
    private int price;

    // 할인율
    @Column(nullable=false)
    private int dcRate;

    // 재고
    @Column(nullable=false)
    private int stock;

    // 등록 날짜
    @Column(nullable=false)
    private LocalDateTime regDate;

    // 수정 날짜
    @Column(nullable=false)
    private LocalDateTime modifyDate;

    // 상태
    @Column(nullable=false)
    private String status;

    // 이벤트 유무
    @Column(nullable=false)
    private String event;

    // 추천 상품
    @Column(nullable=false)
    private String rec;

    // 상품 정보 제공 고시
    @Column(nullable=false)
    private String information;

    // 최대 주문 수량
    @Column(nullable=false)
    private int max;

    // 패키지 전용
    @Column(nullable=false)
    private String packageOnly;

    // 카테고리
    @Column(nullable=false)
    private String category;

}
