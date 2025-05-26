package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponse {
    private UserDto user;
}
