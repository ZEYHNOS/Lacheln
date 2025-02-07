package aba3.lucid.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyStatus {

    ACTIVATE("활성화"),
    DEACTIVATE("비활성화"),
    CLOSURE("폐업"),
    BUSINESS_SUSPENSION("정지")

    ;

    private String status;
}
