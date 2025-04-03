package aba3.lucid.domain.user.dto;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupResponse {
    private UserObject data;
    private String message;
}
