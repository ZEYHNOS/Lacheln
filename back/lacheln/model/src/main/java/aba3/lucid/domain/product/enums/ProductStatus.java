package aba3.lucid.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    // 각개 상품(패키지 전환 불가능)
    ACTIVE("활성화"),

    INACTIVE("비활성화"),

    // 패키지 등록 시 (활성화 상태로 변경 불가능)
    PACKAGE("패키지 전용"),

    // 비활성화 상태 -> 삭제, 삭제를 하기 위해서는 모든 예약이 완료되어야한다.
    REMOVE("삭제");


    private final String description;
}
