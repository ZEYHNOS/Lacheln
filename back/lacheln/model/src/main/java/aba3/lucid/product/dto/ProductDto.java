package aba3.lucid.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductDto {
    // 상품 id
    private Long pdId;

    // 이름 id
    private String name;

    // 상품 정보
    private String info;

    // 상품 가격
    private int price;

    // 상품 할인율
    private int dcRate;

    // 상품 재고
    private int stock;

    // 상품 등록 날짜
    private LocalDateTime regDate;

    // 상품 수정 날짜
    private LocalDateTime modifyDate;

    // 상품 상태
    private String status;

    // 상품 이벤트 유무
    private String event;

    // 상품 추천 여부
    private String rec;

    // 상품 정보 제공 고시
    private String information;

    // 상품 최대 주문 수량
    private int max;

    // 상품 패키지 유무
    private String packageOnly;

    // 상품 카테 고리
    private String category;
}
