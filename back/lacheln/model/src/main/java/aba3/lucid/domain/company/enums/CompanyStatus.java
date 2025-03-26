package aba3.lucid.domain.company.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyStatus {
//    WA("기다리는 상태"),
    ACTIVATE("활성화"),
    DEACTIVATE("비활성화"),
    CLOSED("폐업"),
    SUSPENSION("영업 정지"),
    ;

    private final String description;
}
