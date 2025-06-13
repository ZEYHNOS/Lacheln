package aba3.lucid.domain.user.dto;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.domain.user.enums.AccountStatusEnum;
import aba3.lucid.domain.user.enums.GenderEnum;
import aba3.lucid.domain.user.enums.TierEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserInfoDto {
    private String id;
    private String name;
    private String email;
    private LocalDate joinDate;
    private String phone;
    private String nickname;
    private AccountStatusEnum status;
    private GenderEnum gender;
    private TierEnum tier;
}
