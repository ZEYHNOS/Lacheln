package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatusEnum {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    SLEEPING("휴면"),
    SUSPENDED("정지");

    private final String accountStatus;
}
