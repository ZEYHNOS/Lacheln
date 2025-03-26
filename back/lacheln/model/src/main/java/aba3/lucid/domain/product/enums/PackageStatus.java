package aba3.lucid.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageStatus {

    // 방장이 등록했을 때
    PUBLIC("공개"),

    // 준비가 안됐을 때(관리자 전용, 업체 초기 상태)
    PRIVATE("비공개"),

    // 기간 만료
    REMOVE("삭제")
    ;

    private final String description;
}
